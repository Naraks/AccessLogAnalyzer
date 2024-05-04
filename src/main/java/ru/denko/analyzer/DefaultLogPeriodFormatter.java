package ru.denko.analyzer;

public class DefaultLogPeriodFormatter implements LogPeriodFormatter {

    @Override
    public String formatEntry(LogPeriod period) {
        return String.format(
            "%s %s %.1f",
            period.getStartPeriod().toString(),
            period.getEndPeriod().toString(),
            period.getAvailabilityLevel()
        );
    }
}
