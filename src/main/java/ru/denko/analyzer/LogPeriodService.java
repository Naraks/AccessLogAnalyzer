package ru.denko.analyzer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Data;
import ru.denko.io.Writer;
import ru.denko.parser.LogEntry;
import ru.denko.settings.Settings;

@Data
public class LogPeriodService {

    public static final int ENTRIES_THRESHOLD = 100_000;

    private final Settings settings;
    private final List<LogPeriod> badPeriods;
    private LogPeriod currentPeriod;

    public LogPeriodService(Settings settings) {
        this.settings = settings;
        this.badPeriods = new ArrayList<>();
        this.currentPeriod = new LogPeriod(settings);
    }

    public void analyzeEntry(LogEntry entry) {
        boolean prevStatus = currentPeriod.isBadPeriod();
        boolean newStatus = currentPeriod.addEntry(entry);
        if (prevStatus && !newStatus) {
            currentPeriod.removeLastEntry();
            currentPeriod.trim();
            if (currentPeriod.getEntries().size() > 1) {
                badPeriods.add(currentPeriod);
            }
            currentPeriod = new LogPeriod(settings);
            currentPeriod.addEntry(entry);
        }

        if (currentPeriod.getEntries().size() > ENTRIES_THRESHOLD) {
            if (currentPeriod.isBadPeriod()) {
                badPeriods.add(currentPeriod);
            }
            currentPeriod = new LogPeriod(settings);
        }
    }

    public void flush() {
        if (currentPeriod.getEntries().size() > 1 && currentPeriod.isBadPeriod()) {
            currentPeriod.trim();
            badPeriods.add(currentPeriod);
        }
    }

    public void writeInfo(Writer writer, LogPeriodFormatter formatter) {
        badPeriods.stream()
                  .sorted(Comparator.comparing(LogPeriod::getStartPeriod))
                  .map(formatter::formatEntry)
                  .forEach(writer::write);
    }

}
