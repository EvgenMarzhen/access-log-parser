import lombok.Getter;
import static java.lang.Integer.parseInt;

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
    final StringBuilder userAgent;


    public LogEntry(String line) {
        String[] logComponents = line.split(" ");

        this.ip = logComponents[0];
        this.someProperty1 = logComponents[1];
        this.someProperty2 = logComponents[2];
        this.dateTime = logComponents[3];
        this.method = HttpMethods.valueOf(logComponents[5].replace("\"", ""));
        this.pathMethod = logComponents[6];
        this.httpVersion = logComponents[7].replace("\"", "");
        this.httpStatus = parseInt(logComponents[8]);
        this.sizeResponse =  parseInt(logComponents[9]);
        this.refer = logComponents[10];

        if(logComponents.length > 11) {
            this.userAgent = userAgentBuild(logComponents);
        } else this.userAgent = new StringBuilder("-");

    }

    public StringBuilder userAgentBuild(String[] userAgentComponents) {
        StringBuilder userAgentFinal = new StringBuilder();
        for (int i = 11; i < userAgentComponents.length; i++) {
                userAgentFinal.append(userAgentComponents[i]).append(" ");
        }
        return userAgentFinal;
    }

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
                ", userAgent=" + userAgent +
                '}';
    }
}
