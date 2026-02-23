package monitoring;

import logs.LogEntry;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime = LocalDateTime.MAX;
    private LocalDateTime maxTime = LocalDateTime.MIN;

    public Statistics() {
    }

    public void addEntry(LogEntry logEntry) {
        if (logEntry.getSizeResponse() != null) {
            totalTraffic += logEntry.getSizeResponse();
        }

        if (logEntry.getDateTime().isAfter(getMaxTime())) {
            maxTime = logEntry.getDateTime();
        }

        if (logEntry.getDateTime().isBefore(getMinTime())) {
            minTime = logEntry.getDateTime();
        }
    }

    public double getTrafficRate() {
        if (minTime.equals(maxTime)) {
            return 0.0;
        }
        long diffSeconds = Duration.between(minTime, maxTime).getSeconds();
        double diffHours = diffSeconds / 3600.0;
        return totalTraffic / diffHours;
    }
}
