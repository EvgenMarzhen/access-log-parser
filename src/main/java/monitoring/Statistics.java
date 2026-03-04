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
import java.util.function.Consumer;

@Getter
public class Statistics {
    private final Set<String> successfulPaths = new HashSet<>();
    private final Set<String> notFoundPaths = new HashSet<>();
    private final Set<String> ipList = new HashSet<>();
    private final Set<String> siteList = new HashSet<>();
    private final Set<String> domainList = new HashSet<>();
    private final Map<OS, Integer> osCounts = new HashMap<>();
    private final Map<String, Integer> countReqByIp = new HashMap<>();
    private final Map<OS, Double> osFractions = new HashMap<>();
    private final Map<Browsers, Integer> browserCounts = new HashMap<>();
    private final Map<Browsers, Double> browserFractions = new HashMap<>();
    private final Map<Long, Integer> reqPerSeconds = new HashMap<>();
    private long botReqCount;
    private long userReqCount;
    private long reqErrorCount;
    private long osTotal;
    private long browserTotal;
    private long totalTraffic;
    private LocalDateTime minTime = LocalDateTime.MAX;
    private LocalDateTime maxTime = LocalDateTime.MIN;


    public void addEntry(LogEntry logEntry) {
        UserAgent userAgent = new UserAgent(logEntry.getUserAgent());

        if (logEntry.getSizeResponse() != null) {
            totalTraffic += logEntry.getSizeResponse();
        }

        updateTimeRange(logEntry.getDateTime());

        if (!userAgent.isBot()) {
            userReqCount++;
            ipList.add(logEntry.getIp());
            updatePeakRate(logEntry);
            updateUserStatistics(logEntry);
        } else {
            botReqCount++;
        }

        if (isError(logEntry.getHttpStatus())) {
            reqErrorCount++;
        }

        if (userAgent.getOsType() != null) {
            updateOsStatistics(userAgent);
        }

        if (userAgent.getBrowser() != null) {
            updateBrowserStatistics(userAgent);
        }

        recordPathByStatus(logEntry);

        if (!(logEntry.getRefer().equals("-") || logEntry.getRefer().isEmpty())) {
            updateReferStatistics(logEntry);
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

    private void updateUserStatistics(LogEntry logEntry) {
        UserAgent userAgent = new UserAgent(logEntry.getUserAgent());
        String ip = logEntry.getIp();

        if(!userAgent.isBot()) {
            if(countReqByIp.containsKey(ip)) {
                countReqByIp.put(ip, countReqByIp.get(ip) + 1);
            } else {
                countReqByIp.put(ip, 1);
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
    }

    private void updateBrowserStatistics(UserAgent userAgent) {
        browserTotal++;
        Browsers browser = userAgent.getBrowser();

        if (browserCounts.containsKey(browser)) {
            browserCounts.put(browser, browserCounts.get(browser) + 1);
        } else {
            browserCounts.put(browser, 1);
        }
    }

    public void getFractionByOS() {

        double total = osCounts.get(OS.WINDOWS) + osCounts.get(OS.LINUX) + osCounts.get(OS.MACINTOSH);

        osFractions.put(OS.LINUX, osCounts.getOrDefault(OS.LINUX, 0).doubleValue() / total);
        osFractions.put(OS.WINDOWS, osCounts.getOrDefault(OS.WINDOWS, 0).doubleValue() / total);
        osFractions.put(OS.MACINTOSH, osCounts.getOrDefault(OS.MACINTOSH, 0).doubleValue() / total);

        System.out.println(osFractions);
    }

    public void updatePeakRate(LogEntry logEntry) {
        Long currentSecond = logEntry.getDateTime().toEpochSecond(ZoneOffset.UTC);

        if (reqPerSeconds.containsKey(currentSecond)) {
            reqPerSeconds.put(currentSecond, reqPerSeconds.get(currentSecond) + 1);
        } else {
            reqPerSeconds.put(currentSecond, 1);
        }
    }


    private boolean isError(int status) {
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

    private void updateReferStatistics(LogEntry logEntry) {
        siteList.add(logEntry.getRefer());

        String domain = extractDomain(logEntry.getRefer());
        if (!domain.equals("-")) {
            domainList.add(domain);
        }
    }

    public String extractDomain(String site) {
        if (site.equals("-")) return "-";

        String afterProtocol = site.replace("https://", "").replace("http://", "");

        int slashIndex = afterProtocol.indexOf('/');
        if (slashIndex != -1) {
            return afterProtocol.substring(0, slashIndex);
        }

        return afterProtocol;
    }

    public int getMaxReq() {
        return countReqByIp.values().stream()
                .max(Comparator.naturalOrder())
                .orElse(0);
    }
}
