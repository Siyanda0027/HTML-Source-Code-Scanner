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
    static Scanner scanner = new Scanner(System.in);
    static long refreshRate;
    static int maxNumberOfCompares;
    static String website;
    static boolean isValidWebpage;
    static boolean sourceCodeDifferenceFound;
    static ArrayList<htmlWebpageRecord> recordList;
    static boolean infiniteComparison = false;

    public static void main(String[] args) throws Exception {
    	System.out.println("\nHTML Source Code Scanner Version 1.1 Successfully Launched\n");
        do {
            website = getWebpageFromUser();
            isValidWebpage = isUsersWebpageValid();
        }while (isValidWebpage == false);
        refreshRate = getRefreshRateFromUser();
        getMaxNumberOfRefreshesFromUser();
        htmlCompareProcess();
    }

    private static String getWebpageFromUser()
    {
        System.out.println("Please enter the full URL for the website whose HTML source code you want to track:");
        return scanner.nextLine();
    }

    private static boolean isUsersWebpageValid() {
        String sourceCode = "";

        //Verify that the URL is reachable
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(website).openConnection();
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
            sourceCode = Jsoup.connect(website).get().html();
        } catch (IOException e)
        {
            System.out.println("Error: Unable to gather website's source code. Please try again.\nNote: This error may be caused by an IOException.");
            //e.printStackTrace();
        }

        if (sourceCode.equals(null)) {
            System.out.println("Error: Unable to gather the source code from the URL. Please try again.");
            return false;
        }
        System.out.println("\n"+ website +" has been confirmed as a valid, trackable URL.");
        return true;
    }

    private static long getRefreshRateFromUser()
    {
        int refreshType;
        int refreshFrequency;
        long refreshRate = 0;

        System.out.println("Please enter how often you would like to evaluate the source code of " + website + " for changes.");
        System.out.println("Please enter \"1\" if you would like to track "+ website + "'s changes every X second(s).");
        System.out.println("Please enter \"2\" if you would like to track "+ website + "'s changes every X minute(s).");
        System.out.println("Please enter \"3\" if you would like to track "+ website + "'s changes every X hour(s).");
        System.out.println("Please enter \"4\" if you would like to track "+ website + "'s changes every X day(s).");
        System.out.println("Please enter \"5\" if you would like to track "+ website + "'s changes every X week(s).");

        refreshType = scanner.nextInt();

        switch(refreshType)
        {
            case 1:
                	System.out.println("Enter the number of seconds between each evaluation.");
                	refreshFrequency = scanner.nextInt();
                	System.out.println(""+website+" will be checked for source code changes every " + refreshFrequency + " second(s).");
                	refreshRate = TimeUnit.SECONDS.toMillis(refreshFrequency);
                	break;

            case 2:
                    System.out.println("Enter the number of minutes between each evaluation.");
                    refreshFrequency = scanner.nextInt();
                    System.out.println(""+website+" will be checked for source code changes every " + refreshFrequency + " minute(s).");
                    refreshRate = TimeUnit.MINUTES.toMillis(refreshFrequency);
                    break;

            case 3:
                    System.out.println("Enter the number of hours between each evaluation.");
                    refreshFrequency = scanner.nextInt();
                    System.out.println(""+website+" will be checked for source code changes every " + refreshFrequency + " hour(s).");
                    refreshRate = TimeUnit.HOURS.toMillis(refreshFrequency);
                    break;

            case 4:
                    System.out.println("Enter the number of days between each evaluation.");
                    refreshFrequency = scanner.nextInt();
                    System.out.println(""+website+" will be checked for source code changes every " + refreshFrequency + " day(s).");
                    refreshRate = TimeUnit.DAYS.toMillis(refreshFrequency);
                    break;

            case 5:
                    System.out.println("Enter the weeks of seconds between each evaluation.");
                    refreshFrequency = scanner.nextInt();
                    System.out.println(""+website+" will be checked for source code changes every " + refreshFrequency + " week(s).");
                    refreshRate = (TimeUnit.DAYS.toMillis(refreshFrequency) * 7); // Times 7 because the TimeUnit library only goes up to Days to millis conversion.
                    break;
        }

        return refreshRate;
    }

    private static void getMaxNumberOfRefreshesFromUser()
    {
        String userResponceToIndefQuestion;

        System.out.println("Would you like to perform the checks indefinitely?");
        userResponceToIndefQuestion = scanner.next();
        if(userResponceToIndefQuestion.equalsIgnoreCase("yes") == false && userResponceToIndefQuestion.equalsIgnoreCase("no") == false)
        {
            System.out.println("ERROR: Please enter \"Yes\" or \"no\"");
            getMaxNumberOfRefreshesFromUser();
        }
        if(userResponceToIndefQuestion.equalsIgnoreCase("yes"))
        {
            infiniteComparison = true;
        }
        else
        {
            infiniteComparison = false;
            System.out.println("What is the maximum number of times that you would like to check for changes to " + website + "'s source code?");
            maxNumberOfCompares = scanner.nextInt();
        }
    }

    private static htmlWebpageRecord generateHtmlWebpageRecord(int recordNumber)
    {
        htmlWebpageRecord record = new htmlWebpageRecord();
        record.setVersion(recordNumber);
        record.setUrl(website);
        System.out.println("New website record created. (Record #: "+(record.getVersion()+1)+")");
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

    private static void htmlCompareProcess() throws Exception {

        System.out.println("\nActivity Report:");
        System.out.println("--------------------------------------------------");
        if(infiniteComparison)
        {
            System.out.println("*** NOTE: You have selected an indefinite comparison.\n*** TO STOP THE COMPARISON, PRESS \"CNTRL+C\". ");
            System.out.println("--------------------------------------------------");
        }

        recordList = new ArrayList<>();

        if(infiniteComparison == false) {
            for (int i = 0; i <= maxNumberOfCompares; i++) {
                htmlWebpageRecord recentlyCreatedRecord;
                recentlyCreatedRecord = generateHtmlWebpageRecord(i);
                recordList.add(recentlyCreatedRecord.getVersion(), recentlyCreatedRecord);
                compareRecordsAndUpdateActivityReport();
                Thread.sleep(refreshRate);
            }
        }
        else
        {
            for (int i = 0; i <= 0; i--) {
                htmlWebpageRecord recentlyCreatedRecord;
                recentlyCreatedRecord = generateHtmlWebpageRecord(i*-1);
                recordList.add(recentlyCreatedRecord.getVersion(), recentlyCreatedRecord);
                compareRecordsAndUpdateActivityReport();
                Thread.sleep(refreshRate);
            }
        }
    }

    private static void compareRecordsAndUpdateActivityReport() throws Exception {
        if (recordList.size() >= 2) //The recordList needs at least 2 records to do a compare
        {
            for(int i = 0; i < recordList.size(); i++)
            {
                if(sourceCodeDifferenceFound != true)
                {
                    if(i+1 >= recordList.size()) // Protection for index out of bounds exception.
                    {
                        break;
                    }
                    else
                    {
                        if(recordList.get(i).getHasBeenCompared() == false || recordList.get(i+1).getHasBeenCompared() == false)
                        {
                            sourceCodeDifferenceFound = doHtmlRecordsMatch(recordList.get(i), recordList.get(i+1));
                            if(sourceCodeDifferenceFound != true)
                            {
                                System.out.println("No source code change found between webpage record "+(recordList.get(i).getVersion()+1)+" & "+(recordList.get(i+1).getVersion()+1));
                                //TODO: Remove and destroy record at recordList[i] to free system resources. This may require an arrayList shift.
                            }
                        }
                    }
                }
                else
                {
                    System.out.println("\nNotice: The source code on " + website+ "has changed");
                }
            }
        }
    }
}

