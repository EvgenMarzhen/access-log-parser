package dictionary;

import lombok.Getter;

@Getter
public enum Browsers {
    CHROME("Chrome"),
    SAFARI("Safari"),
    OPERA("Opera"),
    FIREFOX("Firefox"),
    EDGE("Edge"),
    BOT("Bot"),
    INTERNET_EXPLORER("IE Explorer"),
    UNDEFINED("undefined");

    final String browserName;

    Browsers(String browserName) {
        this.browserName = browserName;
    }

    @Override
    public String toString() {
        return browserName;
    }
}
