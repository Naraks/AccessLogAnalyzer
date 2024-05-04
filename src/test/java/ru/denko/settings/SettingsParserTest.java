package ru.denko.settings;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import org.junit.jupiter.api.Test;

class SettingsParserTest {

    private final SettingsParser settingsParser = new SettingsParser();

    @Test
    void parseValidSettings() {
        String[] params = {"-u", "99.9", "-t", "45"};

        Settings settings;
        try {
            settings = settingsParser.parseSettings(params);
        } catch (ParseException e) {
            settings = null;
        }

        assertNotNull(settings);
        assertEquals(99.9, settings.getAvailabilityLevel());
        assertEquals(45, settings.getResponseTime());
    }

    @Test
    void parseSettingsWithoutValue() {
        String[] params = {"-u", "99.9", "-t"};

        ParseException thrown = assertThrows(ParseException.class, () -> settingsParser.parseSettings(params));

        assertEquals("Setting without value: -t: -u 99.9 -t", thrown.getMessage());
    }

    @Test
    void parseMissingSettings() {
        String[] params = {"-u", "99.9"};

        ParseException thrown = assertThrows(ParseException.class, () -> settingsParser.parseSettings(params));

        assertEquals("setting of response time cannot be null: -u 99.9", thrown.getMessage());
    }

    @Test
    void parseSettingsWithUnknownSetting() {
        String[] params = {"-u", "99.9", "-t", "45", "-a", "44"};

        ParseException thrown = assertThrows(ParseException.class, () -> settingsParser.parseSettings(params));

        assertEquals("Unknown setting: -a: -u 99.9 -t 45 -a 44", thrown.getMessage());
    }

    @Test
    void parseSettingsWithInvalidValue() {
        String[] params = {"-u", "99.9", "-t", "err"};

        ParseException thrown = assertThrows(ParseException.class, () -> settingsParser.parseSettings(params));

        assertEquals("Unable to parse settings: -u 99.9 -t err", thrown.getMessage());
    }

}
