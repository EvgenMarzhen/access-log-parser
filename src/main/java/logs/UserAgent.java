package logs;

import dictionary.Browsers;
import dictionary.OS;
import lombok.Getter;

@Getter
public class UserAgent {
    final OS osType;
    final Browsers browser;

    public UserAgent(String userAgent) {
        String[] componentUserAgent = userAgent.split(" ");

        if (componentUserAgent.length > 1) {
            this.osType = findOS(userAgent);
            this.browser = findBrowser(userAgent);
        } else {
            this.osType = null;
            this.browser = null;
        }
    }

    public Browsers findBrowser(String userAgent) {
        String[] componentUserAgent = userAgent.split(" ");

        if (userAgent.contains("Firefox/")) {
            return Browsers.FIREFOX;
        }

        if (componentUserAgent.length > 2) {
            if (userAgent.contains("(KHTML, like Gecko)") && userAgent.contains("Chrome/") && userAgent.contains("Safari/")) {
                return Browsers.CHROME;
            }
        }

        if (userAgent.contains("OPR/") || (userAgent.contains("Opera") && userAgent.contains("Presto/"))) {
            return Browsers.OPERA;
        }

        if (userAgent.contains("Edg/")) {
            return Browsers.EDGE;
        }

        if (userAgent.contains("IEMobile/")) {
            return Browsers.INTERNET_EXPLORER;
        }

        if (componentUserAgent.length > 2) {
            if (userAgent.contains("Safari/") && userAgent.contains("Mobile/")) {
                return Browsers.SAFARI;
            }
        }

        if (userAgent.contains("compatible")) {
            return Browsers.BOT;
        }

        return Browsers.UNDEFINED;

    }

    public OS findOS(String userAgent) {

        if(userAgent.contains("Windows")) {
            return OS.WINDOWS;
        }

        if(userAgent.contains("Linux")) {
            return OS.LINUX;
        }

        if(userAgent.contains("Macintosh")) {
            return OS.MACINTOSH;
        }

        if(userAgent.contains("compatible")) {
            return OS.COMPATIBLE;
        }

        return OS.UNDEFINED;
    }

    public boolean isBot() {
        return browser == Browsers.BOT;
    }
}
