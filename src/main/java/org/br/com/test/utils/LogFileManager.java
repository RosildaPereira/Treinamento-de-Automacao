package org.br.com.test.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LogFileManager {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static String getExecutionLogFileName() {
        String today = LocalDate.now().format(DATE_FORMATTER);
        return "target/log/execution-" + today + ".log";
    }
    
    public static String getAutomationLogFileName() {
        String today = LocalDate.now().format(DATE_FORMATTER);
        return "target/log/automation-" + today + ".log";
    }
    
    public static String getLogFilePattern() {
        return "target/log/%d{yyyy-MM-dd}-%i.log.gz";
    }
} 