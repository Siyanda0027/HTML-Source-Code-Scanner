import org.jsoup.Jsoup;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main
{
    static Scanner scanner = new Scanner(System. in);
    static long refreshRate;
    static int maxNumberOfRefreshes;
    static String webpage;
    static boolean isValidWebpage;
    static boolean sourceCodeDifferenceFound;
    static ArrayList<htmlWebpageRecord> recordList;

    public static void main(String[] args) throws Exception {
        do {
            webpage = getWebpageFromUser();
            isValidWebpage = isUsersWebpageValid();
        }while (isValidWebpage == false);

        refreshRate = getRefreshRateFromUser();
        maxNumberOfRefreshes = getMaxNumberOfRefreshesFromUser();
        System.out.println("\nActivity Report:");
        System.out.println("--------------------------------------------------");

        recordList = new ArrayList<>();

        for(int i = 0; i <= maxNumberOfRefreshes; i++)
        {
            htmlWebpageRecord recentlyCreatedRecord;
            recentlyCreatedRecord = generateHtmlWebpageRecord(i);
            Thread.sleep(refreshRate);

            recordList.add(recentlyCreatedRecord.getVersion(), recentlyCreatedRecord);
            if(recordList.size() >= 2) //The recordList needs at least 2 records to do a compare
            {
                for(int j = 0; j < recordList.size(); j++)
                {
                    if(sourceCodeDifferenceFound != true)
                    {
                        if(j+1 >= recordList.size()) // Protection for index out of bounds exception.
                        {
                            break;
                        }
                        else
                        {
                            if(recordList.get(j).getHasBeenCompared() == false || recordList.get(j+1).getHasBeenCompared() == false)
                            {
                                sourceCodeDifferenceFound = doHtmlRecordsMatch(recordList.get(j), recordList.get(j+1));
                                if(sourceCodeDifferenceFound != true)
                                {
                                    System.out.println("No source code change found between webpage record "+recordList.get(j).getVersion()+" & " +recordList.get(j+1).getVersion());
                                }
                            }
                        }
                    }
                    else
                    {
                        System.out.println("Notice: The source code on " + webpage+ "has changed");
                    }
                }
            }
        }

    }

    private static String getWebpageFromUser()
    {
        System.out.println("Please enter the full URL for the webpage that you want to track:");
        return scanner.nextLine().toString();
    }

    private static boolean isUsersWebpageValid() {
        String sourceCode = "";

        //Verify that the URL is reachable
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(webpage).openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) //200 is the success response code for webpages
            {
                System.out.println("Error: Unable to connect to URL. Please verify that you entered the URL fully and correctly and try again.");
                return false;
            }
        } catch (ProtocolException e) {
            System.out.println("Error: Unable to connect to URL. Please verify that you entered the URL fully and correctly and try again.\nNOTE: This error may be caused by an incorrect URL protocol.");
            //e.printStackTrace();
            return false;
        } catch (MalformedURLException e) {
            System.out.println("Error: Unable to connect to URL. Please verify that you entered the URL fully and correctly and try again.\nNote: This error may be caused by a Malformed URL.");
            //TODO: Research this exception and provide the user with details that will help them fix the URL.
            //e.printStackTrace();
            return false;

        } catch (IOException e) {
            System.out.println("Error: Unable to connect to URL. Please verify that you entered the URL fully and correctly and try again.\nNote: This error may be caused by an IOException.");
            //TODO: Research this exception and provide the user with details that will help them fix the URL.
            //e.printStackTrace();
            return false;
        }

        //See if the source code is retrievable.
        try {
            sourceCode = Jsoup.connect(webpage).get().html();
        } catch (IOException e)
        {
            System.out.println("Error: Unable to gather webpage's source code. Please try again.\nNote: This error may be caused by an IOException.");
            //e.printStackTrace();
        }

        if (sourceCode.equals(null)) {
            System.out.println("Error: Unable to gather the source code from the URL. Please try again.");
            return false;
        }
        System.out.println("\nThank you. "+webpage+" has been confirmed as a valid, trackable URL.");
        return true;
    }

    private static long getRefreshRateFromUser()
    {
        int refreshType;
        int refreshFrequency;
        long refreshRate = 0;

        System.out.println("Now, please tell me how often you would like me to evaluate the source code of " +webpage+ " for changes.");
        System.out.println("Please enter \"0\" if you would like me to track "+webpage+ "'s changes every X second(s).");
        System.out.println("Please enter \"1\" if you would like me to track "+webpage+ "'s changes every X minute(s).");
        System.out.println("Please enter \"2\" if you would like me to track "+webpage+ "'s changes every X hour(s).");
        System.out.println("Please enter \"3\" if you would like me to track "+webpage+ "'s changes every X day(s).");
        System.out.println("Please enter \"4\" if you would like me to track "+webpage+ "'s changes every X week(s).");

        refreshType = scanner.nextInt();

        switch(refreshType)
        {
            case 0:
                System.out.println("Okay. How many seconds shall I wait between each evaluation?");
                refreshFrequency = scanner.nextInt();
                System.out.println("Got it! I will check "+webpage+" for source code changes every " + refreshFrequency + " seconds");
                refreshRate = TimeUnit.SECONDS.toMillis(refreshFrequency);
                break;

            case 1:
                    System.out.println("Okay. How many minutes shall I wait between each evaluation?");
                    refreshFrequency = scanner.nextInt();
                    System.out.println("Got it! I will check "+webpage+" for source code changes every " + refreshFrequency + " minutes");
                    refreshRate = TimeUnit.MINUTES.toMillis(refreshFrequency);
                    break;

            case 2:
                    System.out.println("Okay. How many hours shall I wait between each evaluation?");
                    refreshFrequency = scanner.nextInt();
                    System.out.println("Got it! I will check "+webpage+" for source code changes every " + refreshFrequency + " hours");
                    refreshRate = TimeUnit.HOURS.toMillis(refreshFrequency);
                    break;

            case 3:
                    System.out.println("Okay. How many days shall I wait between each evaluation?");
                    refreshFrequency = scanner.nextInt();
                    System.out.println("Got it! I will check "+webpage+" for source code changes every " + refreshFrequency + " days");
                    refreshRate = TimeUnit.DAYS.toMillis(refreshFrequency);
                    break;

            case 4:
                    System.out.println("Okay. How many weeks shall I wait between each evaluation?");
                    refreshFrequency = scanner.nextInt();
                    System.out.println("Got it! I will check "+webpage+" for source code changes every " + refreshFrequency + " weeks");
                    refreshRate = (TimeUnit.DAYS.toMillis(refreshFrequency) * 7); // Times 7 because the TimeUnit library only goes up to Days to millis conversion.
                    break;
        }

        return refreshRate;
    }

    private static int getMaxNumberOfRefreshesFromUser()
    {
        System.out.println("What is the maximum number of times that you would like me to check for changes to "+webpage+"'s source code?");
        return scanner.nextInt();
    }

    private static htmlWebpageRecord generateHtmlWebpageRecord(int recordNumber)
    {
        htmlWebpageRecord record = new htmlWebpageRecord();
        record.setVersion(recordNumber);
        record.setUrl(webpage);
        System.out.println("New webpage record created. (Record #: "+record.getVersion() + ")");
        return record;
    }

    private static boolean doHtmlRecordsMatch(htmlWebpageRecord firstRecord, htmlWebpageRecord secondRecord) throws Exception
    {
        firstRecord.setHasBeenCompared(true);
        secondRecord.setHasBeenCompared(true);

        if(firstRecord.getSourceCode().equals(secondRecord.getSourceCode()))
        {
           return true;
        }
        else
        {
           return false;
        }
    }
}

