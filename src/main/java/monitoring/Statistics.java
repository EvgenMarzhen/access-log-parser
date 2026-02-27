package monitoring;

import dictionary.Browsers;
import dictionary.OS;
import logs.LogEntry;
import logs.UserAgent;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Getter
public class Statistics {
    private final HashSet<String> successfulPaths = new HashSet<>();
    private final HashSet<String> notFoundPaths = new HashSet<>();
    private final HashSet<String> ipList = new HashSet<>();
    private final HashMap<OS, Integer> osCounts = new HashMap<>();
    private final HashMap<OS, Double> osFractions = new HashMap<>();
    private final HashMap<Browsers, Integer> browserCounts = new HashMap<>();
    private final HashMap<Browsers, Double> browserFractions = new HashMap<>();
    private final HashMap<Long, Integer> reqPerSeconds = new HashMap<>();
    private long botReqCount;
    private long userReqCount;
    private long reqErrorCount;
    private long osTotal;
    private long browserTotal;
    private long totalTraffic;
    private LocalDateTime minTime = LocalDateTime.MAX;
    private LocalDateTime maxTime = LocalDateTime.MIN;

    public Statistics() {
    }

    public void addEntry(LogEntry logEntry) {
        UserAgent userAgent = new UserAgent(logEntry.getUserAgent());

        if (logEntry.getSizeResponse() != null) {
            totalTraffic += logEntry.getSizeResponse();
        }

        updateTimeRange(logEntry.getDateTime());

        if (!userAgent.isBot()) {
            userReqCount++;
            ipList.add(logEntry.getIp());
        } else {
            botReqCount++;
        }

        if (isErrorStatus(logEntry.getHttpStatus())) {
            reqErrorCount++;
        }

        if (userAgent.getOsType() != null) {
            updateOsStatistics(userAgent);
        }

        if (userAgent.getBrowser() != null) {
            updateBrowserStatistics(userAgent);
        }

        recordPathByStatus(logEntry);

        if (!userAgent.isBot()) {
            updatePeakRate(logEntry);
        }

    }

    private void updateTimeRange(LocalDateTime dateTime) {
        if (dateTime.isAfter(maxTime)) {
            maxTime = dateTime;
        }
        if (dateTime.isBefore(minTime)) {
            minTime = dateTime;
        }
    }

    private boolean isErrorStatus(int status) {
        return status >= 400 && status <= 599;
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
        OS os = userAgent.getOsType();

        if (osCounts.containsKey(os)) {
            osCounts.put(os, osCounts.get(os) + 1);
        } else {
            osCounts.put(os, 1);
        }

        if (osFractions.containsKey(os)) {
            osFractions.put(os, ((double) osCounts.get(os) / osTotal));
        } else {
            osFractions.put(os, 1.0 / osTotal);
        }
    }

    private void updateBrowserStatistics(UserAgent userAgent) {
        browserTotal++;
        Browsers browser = userAgent.getBrowser();

        if (browserCounts.containsKey(browser)) {
            browserCounts.put(browser, browserCounts.get(browser) + 1);
        } else {
            browserCounts.put(browser, 1);
        }

        if (browserFractions.containsKey(browser)) {
            browserFractions.put(browser, ((double) browserCounts.get(browser) / browserTotal));
        } else {
            browserFractions.put(browser, 1.0 / browserTotal);
        }
    }

    public void updatePeakRate(LogEntry logEntry) {
        Long currentSecond = logEntry.getDateTime().toEpochSecond(ZoneOffset.UTC);

        if (reqPerSeconds.containsKey(currentSecond)) {
            reqPerSeconds.put(currentSecond, reqPerSeconds.get(currentSecond) + 1);
        } else {
           reqPerSeconds.put(currentSecond, 1);
        }
    }

    public Integer getPeakRatePerSecond() {
       return reqPerSeconds.values().stream().max((a, b) -> a.compareTo(b)).orElse(0);
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
