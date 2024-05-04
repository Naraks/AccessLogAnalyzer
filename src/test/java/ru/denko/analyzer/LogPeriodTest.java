package ru.denko.analyzer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import ru.denko.parser.LogEntry;
import ru.denko.settings.Settings;

class LogPeriodTest {

    @Test
    void testTrim1() {
        LogPeriod period = new LogPeriod(Settings.of(80.0, 100.0));
        period.addEntry(LogEntry.builder().ip("0").time(LocalDateTime.now()).responseTime(110.0).statusCode(200).build());
        period.addEntry(LogEntry.builder().ip("1").time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());
        period.addEntry(LogEntry.builder().ip("2").time(LocalDateTime.now()).responseTime(70.0).statusCode(500).build());
        period.addEntry(LogEntry.builder().ip("3").time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());
        period.addEntry(LogEntry.builder().ip("4").time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());

        period.trim();

        assertEquals(3, period.getEntries().size());
    }

    @Test
    void testTrim2() {
        LogPeriod period = new LogPeriod(Settings.of(80.0, 100.0));
        period.addEntry(LogEntry.builder().ip("0").time(LocalDateTime.now()).responseTime(110.0).statusCode(200).build());
        period.addEntry(LogEntry.builder().ip("1").time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());
        period.addEntry(LogEntry.builder().ip("2").time(LocalDateTime.now()).responseTime(70.0).statusCode(500).build());

        period.trim();

        assertEquals(3, period.getEntries().size());
    }

    @Test
    void removeLastEntry() {
        LogPeriod period = new LogPeriod(Settings.of(80.0, 100.0));
        period.addEntry(LogEntry.builder().ip("0").time(LocalDateTime.now()).responseTime(110.0).statusCode(200).build());
        period.addEntry(LogEntry.builder().ip("1").time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());
        period.addEntry(LogEntry.builder().ip("2").time(LocalDateTime.now()).responseTime(70.0).statusCode(500).build());

        assertEquals(33, Math.round(period.getAvailabilityLevel()));

        period.removeLastEntry();

        assertEquals(2, period.getEntries().size());
        assertEquals(50, Math.round(period.getAvailabilityLevel()));
    }

    @Test
    void addEntry1() {
        LogPeriod period = new LogPeriod(Settings.of(80.0, 100.0));
        period.addEntry(LogEntry.builder().ip("0").time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());
        period.addEntry(LogEntry.builder().ip("1").time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());
        period.addEntry(LogEntry.builder().ip("2").time(LocalDateTime.now()).responseTime(70.0).statusCode(500).build());

        assertEquals(1, period.getEntries().size());
    }

    @Test
    void addEntry2() {
        LogPeriod period = new LogPeriod(Settings.of(80.0, 100.0));
        period.addEntry(LogEntry.builder().ip("0").time(LocalDateTime.now()).responseTime(110.0).statusCode(200).build());
        period.addEntry(LogEntry.builder().ip("1").time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());

        assertEquals(2, period.getEntries().size());
    }

    @Test
    void isBadPeriod() {
        LogPeriod period = new LogPeriod(Settings.of(70.0, 100.0));
        period.addEntry(LogEntry.builder().ip("0").time(LocalDateTime.now()).responseTime(110.0).statusCode(200).build());
        period.addEntry(LogEntry.builder().ip("1").time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());

        assertTrue(period.isBadPeriod());

        period.addEntry(LogEntry.builder().ip("3").time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());
        period.addEntry(LogEntry.builder().ip("4").time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());
        period.addEntry(LogEntry.builder().ip("5").time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());

        assertFalse(period.isBadPeriod());
    }

    @Test
    void getAvailabilityLevel() {
        LogPeriod period = new LogPeriod(Settings.of(70.0, 100.0));
        period.addEntry(LogEntry.builder().ip("0").time(LocalDateTime.now()).responseTime(110.0).statusCode(200).build());
        period.addEntry(LogEntry.builder().ip("1").time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());
        period.addEntry(LogEntry.builder().ip("2").time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());

        assertEquals(67, Math.round(period.getAvailabilityLevel()));
    }
}
