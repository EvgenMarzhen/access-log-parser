import lombok.Getter;

@Getter
public class UserAgent {
    final String osType;
    final String browser;

    public UserAgent(String userAgent) {
        String[] componentUserAgent = userAgent.split(" ");

        if(componentUserAgent.length > 1) {
            this.osType = componentUserAgent[1].replaceAll("[;()]", "");
            this.browser = componentUserAgent[0].replace("\"", "");
        } else {
            this.osType = null;
            this.browser = null;
        }
    }

//    public void printProperty(String userAgent) {
//        String[] componentUserAgent = userAgent.split(" ");
//        for (String component: componentUserAgent) {
//            System.out.println(component);
//        }
//    }


}
