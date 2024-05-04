package ru.denko.analyzer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import ru.denko.parser.LogEntry;
import ru.denko.settings.Settings;

class LogPeriodServiceTest {

    @Test
    void analyzeEntry() {
        LogPeriodService service = new LogPeriodService(Settings.of(70.0, 100.0));

        LocalDateTime start = LocalDateTime.now();
        service.analyzeEntry(LogEntry.builder().time(start).responseTime(110.0).statusCode(200).build());
        service.analyzeEntry(LogEntry.builder().time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());
        LocalDateTime start1 = LocalDateTime.now();
        service.analyzeEntry(LogEntry.builder().time(start1).responseTime(110.0).statusCode(200).build());
        service.analyzeEntry(LogEntry.builder().time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());
        service.analyzeEntry(LogEntry.builder().time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());
        service.analyzeEntry(LogEntry.builder().time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());
        service.analyzeEntry(LogEntry.builder().time(LocalDateTime.now()).responseTime(70.0).statusCode(200).build());
        LocalDateTime start2 = LocalDateTime.now();
        service.analyzeEntry(LogEntry.builder().time(start2).responseTime(70.0).statusCode(500).build());

        service.flush();

        assertEquals(1, service.getBadPeriods().size());
        assertEquals(start, service.getBadPeriods().get(0).getStartPeriod());
        assertEquals(start1, service.getBadPeriods().get(0).getEndPeriod());
    }
}
