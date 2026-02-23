package dictionary;

import lombok.Getter;

@Getter
public enum OS {
    WINDOWS("Windows"),
    LINUX("Linux"),
    MACINTOSH("Macintosh"),
    COMPATIBLE("Bot"),
    UNDEFINED("Undefined");

    private final String osName;

    OS(String osName) {
        this.osName = osName;
    }

    @Override
    public String toString() {
        return osName;
    }
}
