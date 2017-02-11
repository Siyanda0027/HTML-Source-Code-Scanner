import org.jsoup.Jsoup;

import java.io.IOException;
import java.time.Instant;

public class htmlWebsiteRecord {
    int version;
    String url;
    String sourceCode;
    String timestampAccessed;
    boolean hasBeenCompared = false;

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

    public void setUrl(String url){
        this.url = url;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void loadSourceCode() throws Exception {
        if(url.equals(null))
        {
            throw new Exception("A websiteRecord's source code was requested before a URL was defined.");
        }
        try {
            sourceCode = Jsoup.connect(url).get().html();
            this.timestampAccessed = utils.dataTranslations.convertUnixTimeToStandardTime(Instant.now().getEpochSecond(), settings.getUserTimeZone());
        } catch (IOException e) {
            System.out.println("Error: Unable to gather the source code from the URL.");
            e.printStackTrace();
        }
    }

    public String getSourceCode(){
        return sourceCode;
    }
}

