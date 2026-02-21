import lombok.Getter;

@Getter
public class UserAgent {
    final String osType;
    final String browser;

    public UserAgent(String userAgent) {
        this.osType = userAgent;
        this.browser = userAgent;
    }
}
