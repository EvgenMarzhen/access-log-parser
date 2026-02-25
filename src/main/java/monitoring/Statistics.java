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
    private long osTotal;
    private long totalTraffic;
    private LocalDateTime minTime = LocalDateTime.MAX;
    private LocalDateTime maxTime = LocalDateTime.MIN;
    private final HashSet<String> successfulPaths = new HashSet<>();
    private final HashSet<String> notFoundPaths = new HashSet<>();
    private final HashMap<OS, Integer> osCounts = new HashMap<>();
    private final HashMap<OS, Double> osFractions = new HashMap<>();

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

        recordPathByStatus(logEntry);

        if (userAgent.getOsType() != null) {
            updateOsStatistics(userAgent);
        }
    }

    private void recordPathByStatus(LogEntry logEntry) {
        if (logEntry.getHttpStatus() == 200) {
            if (!logEntry.getPathMethod().isEmpty()) {
                this.successfulPaths.add(logEntry.getPathMethod());
            }
        }

        if (logEntry.getHttpStatus() == 404) {
            if (!logEntry.getPathMethod().isEmpty()) {
                this.notFoundPaths.add(logEntry.getPathMethod());
            }
        }
    }


    private void updateOsStatistics(UserAgent userAgent) {
        osTotal++;

        if (osCounts.containsKey(userAgent.getOsType())) {
            osCounts.put(userAgent.getOsType(), osCounts.get(userAgent.getOsType()) + 1);
        } else {
            osCounts.put(userAgent.getOsType(), 1);
        }

        if (osFractions.containsKey(userAgent.getOsType())) {
            osFractions.put(userAgent.getOsType(), ((double) osCounts.get(userAgent.getOsType()) / osTotal));
        } else {
            osFractions.put(userAgent.getOsType(), 1.0 / osTotal);
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
