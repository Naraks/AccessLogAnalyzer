package ru.denko.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsoleWriter implements Writer {

    protected static final Logger log = LogManager.getLogger(ConsoleWriter.class);

    public void write(String message) {
        log.info(message);
    }

}
