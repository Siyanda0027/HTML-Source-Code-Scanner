import org.jsoup.Jsoup;
import utils.dataConversions;

import java.io.IOException;
import java.time.Instant;

public class htmlWebsiteRecord {
    int version;
    String url;
    String sourceCode;
    String timestampAccessed;
    boolean hasBeenCompared = false;

    public htmlWebsiteRecord(int version, String url) throws Exception {
        this.version = version;
        this.url = url;
        loadSourceCode();
    }

    public String getTimestampAccessed()
    {
        return timestampAccessed;
    }

    public void setHasBeenCompared(boolean hasBeenCompared){
        this.hasBeenCompared = hasBeenCompared;
    }

    public boolean getHasBeenCompared() {
        return hasBeenCompared;
    }

    public int getVersion() {
        return version;
    }

    public void loadSourceCode() throws Exception {
        try {
            sourceCode = Jsoup.connect(url).get().html().trim();
            this.timestampAccessed = dataConversions.convertUnixTimeToStandardTime(Instant.now().getEpochSecond(), settings.getUserTimeZone());
        } catch (IOException e) {
            System.out.println("Error: Unable to gather the source code from the URL.");
            e.printStackTrace();
        }
    }

    public String getSourceCode(){
        return sourceCode;
    }
}

