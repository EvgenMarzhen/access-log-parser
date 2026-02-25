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
    private long totalOs;
    private long totalTraffic;
    private LocalDateTime minTime = LocalDateTime.MAX;
    private LocalDateTime maxTime = LocalDateTime.MIN;
    private HashSet<String> paths = new HashSet<>();
    private HashMap<OS, Integer> statOSCount = new HashMap<>();
    private HashMap<OS, Double> statOSFraction = new HashMap<>();

    public Statistics() {
    }

    public void addEntry(LogEntry logEntry) {
        UserAgent userAgent = new UserAgent(logEntry.getUserAgent());

        if (logEntry.getSizeResponse() != null) {
            totalTraffic += logEntry.getSizeResponse();
        }

        if (logEntry.getDateTime().isAfter(maxTime)) {
            maxTime = logEntry.getDateTime();
        }

        if (logEntry.getDateTime().isBefore(minTime)) {
            minTime = logEntry.getDateTime();
        }

        if (logEntry.getHttpStatus() == 200) {
            if (!logEntry.getPathMethod().isEmpty()) {
                this.paths.add(logEntry.getPathMethod());
            }
        }

        if (userAgent.getOsType() != null) {
            monitoringOS(userAgent);
        }
    }


    private void monitoringOS(UserAgent userAgent) {
        totalOs++;

        if (statOSCount.containsKey(userAgent.getOsType())) {
            statOSCount.put(userAgent.getOsType(), statOSCount.get(userAgent.getOsType()) + 1);
        } else {
            statOSCount.put(userAgent.getOsType(), 1);
        }

        if (statOSFraction.containsKey(userAgent.getOsType())) {
            statOSFraction.put(userAgent.getOsType(), ((double) statOSCount.get(userAgent.getOsType()) / totalOs));
        } else {
            statOSFraction.put(userAgent.getOsType(), 1.0 / totalOs);
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
