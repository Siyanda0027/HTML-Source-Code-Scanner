package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class dataConversions
{
    public static String convertUnixTimeToStandardTime(long unixTime, TimeZone timezone)
    {
        Date date = new Date(unixTime * 1000); //Converts unix millis to seconds
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy, HH:mm:ss z"); //Example output: 02-11-2017, 12:27:14
        dateFormat.setTimeZone(timezone);
        String standardTimestamp = dateFormat.format(date);
        return standardTimestamp;
    }
}