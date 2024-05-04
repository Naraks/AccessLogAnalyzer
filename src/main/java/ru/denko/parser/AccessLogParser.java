package ru.denko.parser;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.denko.utils.StringUtils;

public class AccessLogParser implements LogParser {

    protected static final Logger log = LogManager.getLogger(AccessLogParser.class);

    public static final String DATE_FORMAT = "dd/MM/yyyy:HH:mm:ss Z";

    /**
     * Example: 192.168.32.181 - - [14/06/2017:16:47:02 +1000] "PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1" 200 2 44.510983 "-" "@list-item-updater" prio:0
     */
    @Override
    public LogEntry parseEntry(String logEntry) throws ParseException {
        String[] infos = logEntry.split(StringUtils.EMPTY_STRING);
        LogEntry entry;
        try {
            entry = LogEntry.builder()
                            .ip(infos[0].trim())
                            .time(parseDateTime(infos[3], infos[4]))
                            .httpMethod(parseHttpMethod(infos[5]))
                            .url(infos[6])
                            .protocol(parseProtocol(infos[7]))
                            .statusCode(parseStatusCode(infos[8]))
                            .responseTime(parseResponseTime(infos[10]))
                            .tag(parseTag(infos[12]))
                            .priority(parsePriority(infos[13]))
                            .build();
        } catch (RuntimeException exception) {
            log.error("{}: {}", exception.getClass(), exception.getMessage());
            log.error(Arrays.toString(exception.getStackTrace()));
            throw new ParseException("Unable to parse log entry: " + logEntry, 0);
        }

        return entry;
    }

    private String parseTag(String tag) {
        return tag.replace("\"", "").trim();
    }

    private double parseResponseTime(String responseTime) {
        return Double.parseDouble(responseTime);
    }

    private int parseStatusCode(String statusCode) {
        return Integer.parseInt(statusCode);
    }

    private String parseProtocol(String protocol) {
        return protocol.replace("\"", "").trim();
    }

    private HttpMethod parseHttpMethod(String method) {
        String formattedMethod = method.replace("\"", "").trim().toUpperCase();
        return HttpMethod.valueOf(formattedMethod);
    }

    private LocalDateTime parseDateTime(String time, String offset) {
        String dateTime = time.replace("[", "") + " " + offset.replace("]", "");
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_FORMAT);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTime, format);
        return LocalDateTime.ofInstant(zonedDateTime.toInstant(), ZoneOffset.UTC);
    }

    private int parsePriority(String priority) {
        String formattedPriority = priority.split(PRIORITY_PREFIX)[1];
        return Integer.parseInt(formattedPriority);
    }

}
