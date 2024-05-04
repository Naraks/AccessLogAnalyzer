package ru.denko.settings;

import java.util.Objects;
import lombok.Data;

@Data
public class Settings {

    private Double availabilityLevel;
    private Double responseTime;

    public static Settings of(Double availabilityLevel, Double responseTime) {
        Settings settings = new Settings();
        settings.setAvailabilityLevel(availabilityLevel);
        settings.setResponseTime(responseTime);
        settings.validate();
        return settings;
    }

    public void validate() {
        if (Objects.isNull(availabilityLevel)) {
            throw new IllegalArgumentException("setting of availability level cannot be null");
        }

        if (Objects.isNull(responseTime)) {
            throw new IllegalArgumentException("setting of response time cannot be null");
        }
    }

}
