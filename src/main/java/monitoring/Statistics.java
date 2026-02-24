package monitoring;

import dictionary.OS;
import logs.LogEntry;
import logs.UserAgent;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;

@Getter
public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime = LocalDateTime.MAX;
    private LocalDateTime maxTime = LocalDateTime.MIN;
    private HashSet<String> paths = new HashSet<>();
    private HashMap<OS, Integer> statisticOS  = new HashMap<>();

    public Statistics() {
    }

    public void addEntry(LogEntry logEntry) {
        UserAgent userAgent = new UserAgent(logEntry.getUserAgent());

        if (logEntry.getSizeResponse() != null) {
            totalTraffic += logEntry.getSizeResponse();
        }

        if (logEntry.getDateTime().isAfter(getMaxTime())) {
            maxTime = logEntry.getDateTime();
        }

        if (logEntry.getDateTime().isBefore(getMinTime())) {
            minTime = logEntry.getDateTime();
        }

        if(logEntry.getHttpStatus() == 200) {
            if(!logEntry.getPathMethod().isEmpty()) {
                this.paths.add(logEntry.getPathMethod());
            }
        }

        if (userAgent.getOsType() != null) {
            if(statisticOS.containsKey(userAgent.getOsType())) {
                statisticOS.put(userAgent.getOsType(), statisticOS.get(userAgent.getOsType()) + 1);
            } else {
                statisticOS.put(userAgent.getOsType(), 1);
            }
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
