import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class main
{
    static Scanner scanner = new Scanner(System.in);
    static boolean isValidWebsite;
    static boolean sourceCodeDifferenceFound;
    static ArrayList<htmlWebsiteRecord> recordList = new ArrayList<>();
    static htmlWebsiteRecord preSourceCodeChangeRecord;
    static htmlWebsiteRecord postSourceCodeChangeRecord;
    static String difference = "";

    public static void main(String[] args) throws Exception {

    	System.out.println("\nHTML Source Code Scanner Version 1.3 Successfully Launched\n");
        settings.getAutomaticSettings();
        do {
            getWebsiteFromUser();
            verifyValidWebsite();
        }while (isValidWebsite == false);
        getRefreshRateFromUser();
        getMaxNumberOfRefreshesFromUser();
        htmlCompareProcess();
        displayChangeReport();
        scanner.close();
    }

    private static void getWebsiteFromUser()
    {
        System.out.println("Please enter the full URL for the website whose HTML source code you want to track:");
        settings.setWebsite(scanner.nextLine());
    }

    private static void verifyValidWebsite() {
        String sourceCode = "";

        //Verify that the URL is reachable
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(settings.getWebsite()).openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) //200 is the success response code for websites
            {
                System.out.println("Error: Unable to connect to URL. Please verify that you entered the URL correctly and try again.");
                isValidWebsite = false;
                return;
            }
        } catch (ProtocolException e) {
            System.out.println("Error: Unable to connect to URL. Please verify that you entered the URL correctly and try again.\nNOTE: This error may have been caused by an incorrect URL protocol.");
            isValidWebsite =  false;
            return;
        } catch (MalformedURLException e) {
            System.out.println("Error: Unable to connect to URL. Please verify that you entered the URL correctly and try again.\nNote: This error may have been caused by a Malformed URL.");
            //TODO: Research this exception and provide the user with details that will help them fix the URL.
            isValidWebsite =  false;
            return;

        } catch (IOException e) {
            System.out.println("Error: Unable to connect to URL. Please verify that you entered the URL correctly and try again.\nNote: This error may have been caused by an IOException.");
            //TODO: Research this exception and provide the user with details that will help them fix the URL.
            isValidWebsite = false;
            return;
        } catch (Exception e) {
            System.out.println("Error: An error occurred while testing the URL. Please try again.");
            isValidWebsite = false;
            return;
        }

        //See if the source code is retrievable.
        try {
            sourceCode = Jsoup.connect(settings.getWebsite()).get().html();
        } catch (IOException e)
        {
            System.out.println("Error: Unable to gather website's source code. Please try again.\nNote: This error may be caused by an IOException.");
            isValidWebsite = false;
            return;
        }

        if (sourceCode.equals(null)) {
            System.out.println("Error: Unable to gather the source code from the URL. Please try again.");
            isValidWebsite = false;
            return;
        }

        System.out.println("\n"+ settings.getWebsite() +" has been confirmed as a valid, trackable URL.");
        isValidWebsite = true;
    }

    private static void getRefreshRateFromUser()
    {
        int refreshType = 0;
        int refreshFrequency;
        long refreshRate = 0;

        do {
            System.out.println("\nPlease enter how often you would like to evaluate the source code of " + settings.getWebsite() + " for changes.\n");
            System.out.println("Please enter \"1\" if you would like to track "+ settings.getWebsite() + "'s changes every X second(s).");
            System.out.println("Please enter \"2\" if you would like to track "+ settings.getWebsite() + "'s changes every X minute(s).");
            System.out.println("Please enter \"3\" if you would like to track "+ settings.getWebsite() + "'s changes every X hour(s).");
            System.out.println("Please enter \"4\" if you would like to track "+ settings.getWebsite() + "'s changes every X day(s).");
            System.out.println("Please enter \"5\" if you would like to track "+ settings.getWebsite() + "'s changes every X week(s).");
            if (scanner.hasNextLine()) {
                String userInput = scanner.nextLine(); //You have to save the value of scanner.nextLine because it changes each time you access it via scanner.next...
                try{
                    if((Integer.valueOf(userInput) >= 1) && (Integer.valueOf(userInput) <= 5))
                    {
                        refreshType = Integer.valueOf(userInput);
                    }
                    else{
                        System.out.println("Error: You entered an invalid option. Please enter 1, 2, 3, 4, or 5.");
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Error: You entered an invalid option. Please enter 1, 2, 3, 4, or 5.");
                }

            }

        } while (refreshType == 0);

        switch(refreshType)
        {
            case 1:
                	System.out.println("Enter the number of seconds between each evaluation.");
                	refreshFrequency = scanner.nextInt();
                	System.out.println(settings.getWebsite()+" will be checked for source code changes every " + refreshFrequency + " second(s).");
                	refreshRate = TimeUnit.SECONDS.toMillis(refreshFrequency);
                	break;

            case 2:
                    System.out.println("Enter the number of minutes between each evaluation.");
                    refreshFrequency = scanner.nextInt();
                    System.out.println(settings.getWebsite()+" will be checked for source code changes every " + refreshFrequency + " minute(s).");
                    refreshRate = TimeUnit.MINUTES.toMillis(refreshFrequency);
                    break;

            case 3:
                    System.out.println("Enter the number of hours between each evaluation.");
                    refreshFrequency = scanner.nextInt();
                    System.out.println(settings.getWebsite()+" will be checked for source code changes every " + refreshFrequency + " hour(s).");
                    refreshRate = TimeUnit.HOURS.toMillis(refreshFrequency);
                    break;

            case 4:
                    System.out.println("Enter the number of days between each evaluation.");
                    refreshFrequency = scanner.nextInt();
                    System.out.println(settings.getWebsite()+" will be checked for source code changes every " + refreshFrequency + " day(s).");
                    refreshRate = TimeUnit.DAYS.toMillis(refreshFrequency);
                    break;

            case 5:
                    System.out.println("Enter the number of weeks between each evaluation.");
                    refreshFrequency = scanner.nextInt();
                    System.out.println(settings.getWebsite()+" will be checked for source code changes every " + refreshFrequency + " week(s).");
                    refreshRate = (TimeUnit.DAYS.toMillis(refreshFrequency) * 7); // Times 7 because the TimeUnit library only goes up to Days to millis conversion.
                    break;
        }

        settings.setRefreshRate(refreshRate);
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
            settings.setInfiniteComparison(true);
        }
        else
        {
            settings.setInfiniteComparison(false);
            System.out.println("What is the maximum number of times that you would like to check for changes to " + settings.getWebsite() + "'s source code?");
            settings.setMaxNumberOfCompares(scanner.nextInt());
        }
    }

    private static htmlWebsiteRecord generateHtmlWebpageRecord(int recordNumber) throws Exception {
        htmlWebsiteRecord record = new htmlWebsiteRecord(recordNumber, settings.getWebsite());
        System.out.println("New website record created. (Record #: "+(record.getVersion()+1)+", Timestamp: " +record.getTimestampAccessed()+ ")");
        return record;
    }

    private static boolean doHtmlRecordsMatch(htmlWebsiteRecord firstRecord, htmlWebsiteRecord secondRecord) throws Exception
    {
        firstRecord.setHasBeenCompared(true);
        secondRecord.setHasBeenCompared(true);

        if(firstRecord.getSourceCode().equals(secondRecord.getSourceCode()))
        {
           return true;
        }
        else
        {
           difference = StringUtils.difference(firstRecord.getSourceCode(),secondRecord.getSourceCode());
           return false;
        }
    }

    private static void htmlCompareProcess() throws Exception {

        System.out.println("\nActivity Report:");
        System.out.println("--------------------------------------------------");
        if(settings.isInfiniteComparison())
        {
            System.out.println("*** NOTE: You have selected an indefinite comparison.\n*** TO STOP THE COMPARISON, PRESS \"CNTRL+C\". ");
            System.out.println("--------------------------------------------------");

            for (int i = 0; i <= 0; i--) //Note: This loop is infinite, hence the indefinite comparison.
            {
                if(sourceCodeDifferenceFound == false)
                {
                    htmlWebsiteRecord recentlyCreatedRecord;
                    recentlyCreatedRecord = generateHtmlWebpageRecord(i*-1);
                    recordList.add(recentlyCreatedRecord.getVersion(), recentlyCreatedRecord);
                    compareRecordsAndUpdateActivityReport();
                    Thread.sleep(settings.getRefreshRate());
                }
                else{
                    return;
                }
            }
        } else {
            for (int i = 0; i <= settings.getMaxNumberOfCompares(); i++)
            {
                if(sourceCodeDifferenceFound == false)
                {
                    htmlWebsiteRecord recentlyCreatedRecord;
                    recentlyCreatedRecord = generateHtmlWebpageRecord(i);
                    recordList.add(recentlyCreatedRecord.getVersion(), recentlyCreatedRecord);
                    compareRecordsAndUpdateActivityReport();
                    Thread.sleep(settings.getRefreshRate());
                }
                else{
                    return;
                }
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
                            sourceCodeDifferenceFound = !doHtmlRecordsMatch(recordList.get(i), recordList.get(i+1));
                            if(sourceCodeDifferenceFound != true)
                            {
                                System.out.println("No source code change found between website record "+(recordList.get(i).getVersion()+1)+" & "+(recordList.get(i+1).getVersion()+1));
                                //TODO: Remove and destroy record at recordList[i] to free system resources. This may require an arrayList shift.
                            }
                        }
                    }
                }
                else //A source code change has been detected
                {
                    preSourceCodeChangeRecord = recordList.get(i-1);
                    postSourceCodeChangeRecord = recordList.get(i);
                    System.out.println("\nNotice: The source code on " + settings.getWebsite()+ " has changed!");
                    return;
                }
            }
        }
    }

    public static void displayChangeReport() throws IOException
    {
        System.out.println("\nChange Report:");
        System.out.println("--------------------------------------------------");
        if(sourceCodeDifferenceFound)
        {
            String changeDetectedMessage1 = "The change was detected when comparing the source code of Record #: "+(recordList.indexOf(preSourceCodeChangeRecord)+1)+ " and Record #: "+(recordList.indexOf(postSourceCodeChangeRecord)+1)+".";
            String changeDetectedMessage2 = "Therefore, the change occurred sometime between "+preSourceCodeChangeRecord.getTimestampAccessed()+ " and "+postSourceCodeChangeRecord.getTimestampAccessed()+ ".";
            String changeReportFileName = "HSCSReport"+ Instant.now().getEpochSecond()+".txt"; //Uses the current unix timestamp to generate unique file names. HSCS = HTML Source Code Scanner.

        	System.out.println(changeDetectedMessage1);
            System.out.println(changeDetectedMessage2);

            File changeReport = new File(settings.getUserHomeDirectory(), changeReportFileName);
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(changeReport));
            try {
                fileWriter.write("HTML-Source-Code-Scanner Source Code Change Report:");
                fileWriter.newLine();
                fileWriter.newLine();
                fileWriter.write("Website Tracked: "+ settings.getWebsite());
                fileWriter.newLine();
                fileWriter.write(changeDetectedMessage1);
                fileWriter.newLine();
                fileWriter.write(changeDetectedMessage2);
                fileWriter.newLine();
                fileWriter.newLine();
                fileWriter.write("Source Code Change:");
                fileWriter.newLine();
                fileWriter.write("--------------------------------------------------");
                fileWriter.newLine();
                fileWriter.write(difference);
            } finally {
                System.out.println("\nNOTE: A text file containing more details has been created and saved to your desktop. See: \""+changeReportFileName+"\"");
                fileWriter.close();
            }
        }
        else
        {
        	System.out.println("No source code change occurred!");
        }
    }
}

