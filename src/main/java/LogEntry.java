import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;

@Getter
public class LogEntry {
    final String ip;
    final String someProperty1;
    final String someProperty2;
    final String dateTime;
    final HttpMethods method;
    final String pathMethod;
    final String httpVersion;
    final Integer httpStatus;
    final Integer sizeResponse;
    final String refer;
//    final StringBuilder userAgent;


    public LogEntry(String line) {
        String[] logComponents = line.split(" ");

        this.ip = logComponents[0];
        this.someProperty1 = logComponents[1];
        this.someProperty2 = logComponents[2];
        this.dateTime = logComponents[3] + " " + logComponents[4];
        this.method = HttpMethods.valueOf(logComponents[5].replace("\"", ""));
        this.pathMethod = logComponents[6];
        this.httpVersion = logComponents[7].replace("\"", "");
        this.httpStatus = Integer.parseInt(logComponents[8]);
        this.sizeResponse =  Integer.parseInt(logComponents[9]);
        this.refer = logComponents[10];
//        this.userAgent = userAgentBuild(logComponents);

    }

//    public StringBuilder userAgentBuild(String[] userAgentComponent) {
//        StringBuilder userAgentFinal = null;
//        for (int i = 11; i < userAgentComponent.length; i++) {
//            userAgentFinal.append(userAgentComponent[i]).append(" ");
//        }
//        return userAgentFinal;
//    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "ip='" + ip + '\'' +
                ", someProperty1='" + someProperty1 + '\'' +
                ", someProperty2='" + someProperty2 + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", method=" + method +
                ", pathMethod='" + pathMethod + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                ", httpStatus=" + httpStatus +
                ", sizeResponse=" + sizeResponse +
                ", refer='" + refer + '\'' +
                '}';
    }
}
