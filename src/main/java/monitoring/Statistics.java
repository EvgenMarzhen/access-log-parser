package monitoring;

import dictionary.Browsers;
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
    private long botReqCount;
    private long userReqCount;
    private long reqErrorCount;
    private long osTotal;
    private long browserTotal;
    private long totalTraffic;
    private final HashSet<String> successfulPaths = new HashSet<>();
    private final HashSet<String> notFoundPaths = new HashSet<>();
    private final HashSet<String> ipList = new HashSet<>();
    private final HashMap<OS, Integer> osCounts = new HashMap<>();
    private final HashMap<OS, Double> osFractions = new HashMap<>();
    private final HashMap<Browsers, Integer> browserCounts = new HashMap<>();
    private final HashMap<Browsers, Double> browserFractions = new HashMap<>();
    private LocalDateTime minTime = LocalDateTime.MAX;
    private LocalDateTime maxTime = LocalDateTime.MIN;

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

        if (!userAgent.isBot()) {
            userReqCount++;
            ipList.add(logEntry.getIp());
        } else {
            botReqCount++;
        }

        if (logEntry.getHttpStatus() >= 400 && logEntry.getHttpStatus() <= 500) {
            reqErrorCount++;
        }

        if (userAgent.getOsType() != null) {
            updateOsStatistics(userAgent);
        }

        if (userAgent.getBrowser() != null) {
            updateBrowserStatistics(userAgent);
        }

        recordPathByStatus(logEntry);

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

    private void updateBrowserStatistics(UserAgent userAgent) {
        browserTotal++;

        if (browserCounts.containsKey(userAgent.getBrowser())) {
            browserCounts.put(userAgent.getBrowser(), browserCounts.get(userAgent.getBrowser()) + 1);
        } else {
            browserCounts.put(userAgent.getBrowser(), 1);
        }

        if (browserFractions.containsKey(userAgent.getBrowser())) {
            browserFractions.put(userAgent.getBrowser(), ((double) browserCounts.get(userAgent.getBrowser()) / browserTotal));
        } else {
            browserFractions.put(userAgent.getBrowser(), 1.0 / browserTotal);
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

    public double getAvgTrafficPerHourOfUser() {
        if (minTime.equals(maxTime)) {
            return 0.0;
        }
        long diffSeconds = Duration.between(minTime, maxTime).getSeconds();
        double diffHours = diffSeconds / 3600.0;
        return userReqCount / diffHours;
    }

    public double getAvgErrorReqPerHour() {
        if (minTime.equals(maxTime)) {
            return 0.0;
        }
        long diffSeconds = Duration.between(minTime, maxTime).getSeconds();
        double diffHours = diffSeconds / 3600.0;
        return reqErrorCount / diffHours;
    }

    public double getAvgPerIp() {
        return (double) userReqCount / ipList.size();
    }

}
