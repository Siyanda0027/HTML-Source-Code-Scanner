import javax.swing.filechooser.FileSystemView;
import java.util.Calendar;
import java.util.TimeZone;

public class settings
{
    private static long refreshRate;
    private static int maxNumberOfCompares;
    private static String website;
    private static String userHomeDirectory;
    private static boolean infiniteComparison = false;
    private static TimeZone userTimeZone;

    public static long getRefreshRate() {
        return refreshRate;
    }

    public static void setRefreshRate(long refreshRate) {
        settings.refreshRate = refreshRate;
    }

    public static int getMaxNumberOfCompares() {
        return maxNumberOfCompares;
    }

    public static void setMaxNumberOfCompares(int maxNumberOfCompares) {
        settings.maxNumberOfCompares = maxNumberOfCompares;
    }

    public static String getWebsite() {
        return website;
    }

    public static void setWebsite(String website) {
        settings.website = website;
    }

    public static boolean isInfiniteComparison() {
        return infiniteComparison;
    }

    public static void setInfiniteComparison(boolean infiniteComparison) {
        settings.infiniteComparison = infiniteComparison;
    }

    public static void setUserTimeZone(TimeZone userTimeZone){
        settings.userTimeZone = userTimeZone;
    }

    public static TimeZone getUserTimeZone(){
        return userTimeZone;
    }

    public static void setUserHomeDirectory(String directory)
    {
        userHomeDirectory = directory;
    }

    public static String getUserHomeDirectory(){
        return userHomeDirectory;
    }

    public static void getAutomaticSettings()
    {
        setUserTimeZone(Calendar.getInstance().getTimeZone());
        setUserHomeDirectory(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath());
    }
}
