package ru.denko;

import java.text.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.denko.analyzer.DefaultLogPeriodFormatter;
import ru.denko.analyzer.LogPeriodFormatter;
import ru.denko.analyzer.LogPeriodService;
import ru.denko.io.ConsoleReader;
import ru.denko.io.ConsoleWriter;
import ru.denko.io.Writer;
import ru.denko.parser.AccessLogParser;
import ru.denko.parser.LogEntry;
import ru.denko.parser.LogParser;
import ru.denko.settings.Settings;
import ru.denko.settings.SettingsParser;

public class Analyzer {

    protected static final Logger log = LogManager.getLogger(Analyzer.class);

    private final LogParser logParser;
    private final ConsoleReader reader;
    private final LogPeriodService logPeriodService;
    private final Writer writer;
    private final LogPeriodFormatter logPeriodFormatter;

    public Analyzer(String[] args) throws ParseException {
        SettingsParser settingsParser = new SettingsParser();
        Settings settings = settingsParser.parseSettings(args);

        writer = new ConsoleWriter();
        logParser = new AccessLogParser();
        reader = new ConsoleReader();
        logPeriodService = new LogPeriodService(settings);
        logPeriodFormatter = new DefaultLogPeriodFormatter();
    }

    public void run() {
        reader.read(line -> {
            try {
                if (!line.isEmpty()) {
                    LogEntry logEntry = logParser.parseEntry(line);
                    logPeriodService.analyzeEntry(logEntry);
                } else {
                    finish();
                    System.exit(0);
                }
            } catch (ParseException e) {
                log.warn(e.getMessage());
            }
        });
        finish();
    }

    private void finish() {
        logPeriodService.flush();
        logPeriodService.writeInfo(writer, logPeriodFormatter);
    }

    public static void main(String[] args) {
        try {
            Analyzer analyzer = new Analyzer(args);
            analyzer.run();
        } catch (ParseException exception) {
            log.warn(exception.getMessage());
            System.exit(1);
        }
    }

}
