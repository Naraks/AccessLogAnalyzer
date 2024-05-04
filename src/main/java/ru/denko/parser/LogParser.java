package ru.denko.parser;

import java.text.ParseException;

public interface LogParser {

    String PRIORITY_PREFIX = "prio:";

    LogEntry parseEntry(String logEntry) throws ParseException;

}
