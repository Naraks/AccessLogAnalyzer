package ru.denko.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.Month;
import org.junit.jupiter.api.Test;

class AccessLogParserTest {

    private final AccessLogParser accessLogParser = new AccessLogParser();

    @Test
    void parseValidEntry() {
        String entry = "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c "
            + "HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0";

        LogEntry logEntry;
        try {
            logEntry = accessLogParser.parseEntry(entry);
        } catch (ParseException e) {
            logEntry = null;
        }

        assertNotNull(logEntry);
        assertEquals("192.168.32.181", logEntry.getIp());
        assertEquals(LocalDateTime.of(2017, Month.JUNE, 14, 6, 47, 2), logEntry.getTime());
        assertEquals(HttpMethod.PUT, logEntry.getHttpMethod());
        assertEquals("/rest/v1.4/documents?zone=default&_rid=6076537c", logEntry.getUrl());
        assertEquals("HTTP/1.1", logEntry.getProtocol());
        assertEquals(200, logEntry.getStatusCode());
        assertEquals(44.510983, logEntry.getResponseTime());
        assertEquals("@list-item-updater", logEntry.getTag());
        assertEquals(0, logEntry.getPriority());
    }

    @Test
    void parseInvalidEntry() {
        String entry = "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c "
            + "HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio";
        String expectedMessage = "Unable to parse log entry: 192.168.32.181 - - "
            + "[14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" "
            + "200 2 44.510983 \"-\" \"@list-item-updater\" prio";

        ParseException thrown = assertThrows(ParseException.class, () -> accessLogParser.parseEntry(entry));

        assertEquals(expectedMessage, thrown.getMessage());
    }

}
