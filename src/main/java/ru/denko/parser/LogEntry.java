package ru.denko.parser;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogEntry {

    private String ip;
    private LocalDateTime time;
    private HttpMethod httpMethod;
    private String url;
    private String protocol;
    private int statusCode;
    private double responseTime;
    private String tag;
    private int priority;

}
