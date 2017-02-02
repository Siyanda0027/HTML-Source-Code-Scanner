import org.jsoup.Jsoup;

import java.io.IOException;
import java.time.Instant;

public class htmlWebpageRecord {
    int version;
    String url;
    String sourceCode;
    long timestampAccessed; //Unix timestamp
    boolean hasBeenCompared = false;

    public long getTimestampAccessed() {
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

    public String getSourceCode() throws Exception {
        if(url.equals(null))
        {
            throw new Exception("A webpageRecord's source code was requested before a URL was defined.");
        }

        try {
            sourceCode = Jsoup.connect(url).get().html();
            this.timestampAccessed = Instant.now().getEpochSecond();
        } catch (IOException e) {
            System.out.println("Error: Unable to gather the source code from the URL.");
            e.printStackTrace();
        }
        return sourceCode;
    }
}

