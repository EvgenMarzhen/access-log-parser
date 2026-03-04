package logs;

import dictionary.HttpMethods;
import lombok.Getter;

import java.time.LocalDateTime;

import static java.lang.Integer.parseInt;

@Getter
public class LogEntry {
    private final String ip;
    private final String someProperty1;
    private final String someProperty2;
    private final LocalDateTime dateTime;
    private final HttpMethods method;
    private final String pathMethod;
    private final String httpVersion;
    private final Integer httpStatus;
    private final Integer sizeResponse;
    private final String refer;
    private final String userAgent;


    public LogEntry(String line) {
        String[] logComponents = line.split(" ");

        this.ip = logComponents[0];
        this.someProperty1 = logComponents[1];
        this.someProperty2 = logComponents[2];
        this.dateTime = dateTimeBuild(logComponents[3]);
        this.method = HttpMethods.valueOf(logComponents[5].replace("\"", ""));
        this.pathMethod = logComponents[6];
        this.httpVersion = logComponents[7].replace("\"", "");
        this.httpStatus = parseInt(logComponents[8]);
        this.sizeResponse =  parseInt(logComponents[9]);
        this.refer = logComponents[10];
        this.userAgent = userAgentBuild(logComponents);

    }

    public String userAgentBuild(String[] userAgentComponents) {
        String userAgentFinal = "";
        for (int i = 11; i < userAgentComponents.length; i++) {
                userAgentFinal += userAgentComponents[i] + " ";
        }
        return userAgentFinal;
    }

    public LocalDateTime dateTimeBuild(String strDT) {
        String cleanStrDT = strDT.replace("[", "");
        String[] componentsDT = cleanStrDT.split("[/:]");

        int day = parseInt(componentsDT[0]);
        int year = parseInt(componentsDT[2]);
        int hour = parseInt(componentsDT[3]);
        int minute = parseInt(componentsDT[4]);
        int second = parseInt(componentsDT[5]);

        int month = switch (componentsDT[1]) {
            case "Jan" -> 1;
            case "Feb" -> 2;
            case "Mar" -> 3;
            case "Apr" -> 4;
            case "May" -> 5;
            case "Jun" -> 6;
            case "Jul" -> 7;
            case "Aug" -> 8;
            case "Sep" -> 9;
            case "Oct" -> 10;
            case "Nov" -> 11;
            case "Dec" -> 12;
            default -> throw new IllegalArgumentException("Неизвестный месяц при формирование даты и времени: " + strDT );
        };

        return LocalDateTime.of(year, month, day, hour, minute, second);
    }

    @Override
    public String toString() {
        return "logs.LogEntry{" +
                "ip=" + ip +
                ", someProperty1=" + someProperty1 +
                ", someProperty2=" + someProperty2 +
                ", dateTime=" + dateTime +
                ", method=" + method +
                ", pathMethod=" + pathMethod +
                ", httpVersion=" + httpVersion +
                ", httpStatus=" + httpStatus +
                ", sizeResponse=" + sizeResponse +
                ", refer=" + refer +
                ", userAgent=" + userAgent +
                '}';
    }
}
