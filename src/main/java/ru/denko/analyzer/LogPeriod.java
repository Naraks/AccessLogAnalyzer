package ru.denko.analyzer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.denko.parser.LogEntry;
import ru.denko.settings.Settings;

@Data
@RequiredArgsConstructor
public class LogPeriod {

    private final Settings settings;

    private List<LogEntry> entries = new ArrayList<>();

    private int totalEntriesCount;
    private int badEntriesCount;

    public LocalDateTime getStartPeriod() {
        return Optional.ofNullable(entries.get(0))
                       .map(LogEntry::getTime)
                       .orElse(null);
    }

    public LocalDateTime getEndPeriod() {
        return Optional.ofNullable(entries.get(entries.size() - 1))
                       .map(LogEntry::getTime)
                       .orElse(null);
    }

    public void trim() {
        Optional<LogEntry> lastFailureEntry = entries.stream()
                                                     .filter(this::isFailure)
                                                     .reduce((accumulator, entry) -> entry);
        lastFailureEntry.ifPresent(logEntry -> {
            entries = entries.subList(0, entries.indexOf(logEntry) + 1);
            totalEntriesCount = entries.size();
        });
    }

    public void removeLastEntry() {
        LogEntry logEntry = entries.remove(entries.size() - 1);
        totalEntriesCount--;
        if (isFailure(logEntry)) {
            badEntriesCount--;
        }
    }

    public boolean addEntry(LogEntry entry) {
        boolean failure = isFailure(entry);
        if (!entries.isEmpty() || failure) {
            entries.add(entry);
            totalEntriesCount++;
            if (failure) {
                badEntriesCount++;
            }
        }
        return isBadPeriod();
    }

    public boolean isBadPeriod() {
        if (entries.isEmpty()) {
            return false;
        }
        return getAvailabilityLevel() < settings.getAvailabilityLevel();
    }

    public double getAvailabilityLevel() {
        if (badEntriesCount == 0) {
            return 100.0;
        }
        return 100.0 * ((double) totalEntriesCount - badEntriesCount) / totalEntriesCount;
    }

    private boolean isFailure(LogEntry entry) {
        return isServerError(entry) || isLongRequest(entry);
    }

    private boolean isLongRequest(LogEntry entry) {
        return entry.getResponseTime() > settings.getResponseTime();
    }

    private boolean isServerError(LogEntry entry) {
        return entry.getStatusCode() >= 500 && entry.getStatusCode() < 600;
    }

}
