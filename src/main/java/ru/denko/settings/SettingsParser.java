package ru.denko.settings;

import java.text.ParseException;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.denko.utils.StringUtils;

public class SettingsParser {

    protected static final Logger log = LogManager.getLogger(SettingsParser.class);

    public Settings parseSettings(String[] params) throws ParseException {
        Settings model = new Settings();

        try {
            for (int i = 0; i < params.length; i += 2) {
                if (i + 1 > params.length - 1) {
                    throw new IllegalArgumentException("Setting without value: " + params[i]);
                }
                parseSetting(params[i], params[i + 1], model);
            }
            model.validate();
        } catch (NumberFormatException exception) {
            logError(exception);
            throw new ParseException(
                "Unable to parse settings: " + String.join(StringUtils.EMPTY_STRING, params), 0);
        } catch (IllegalArgumentException exception) {
            logError(exception);
            throw new ParseException(
                exception.getMessage() + ": " + String.join(StringUtils.EMPTY_STRING, params), 0);
        }

        return model;
    }

    private void logError(Exception exception) {
        log.error(String.format("%s: %s", exception.getClass(), exception.getMessage()));
        log.error(Arrays.toString(exception.getStackTrace()));
    }

    private void parseSetting(String setting, String value, Settings model) {
        switch (setting) {
            case "-u":
                model.setAvailabilityLevel(Double.parseDouble(value));
                break;
            case "-t":
                model.setResponseTime(Double.parseDouble(value));
                break;
            default:
                throw new IllegalArgumentException("Unknown setting: " + setting);
        }
    }

}
