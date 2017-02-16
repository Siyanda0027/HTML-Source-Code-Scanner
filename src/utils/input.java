package utils;

import java.util.ArrayList;
import java.util.Scanner;

public class input
{
    private static Scanner scanner = new Scanner(System.in);
    private static String invalidInputMessagePrefix = "ERROR: Invalid input. You entered ";


    //Uses a whitelist type procedure to determine whether the user's input is valid.
    //TODO: Modify this so that it can be used with integer ranges.
    public static boolean inputIsValid(String ... args)
    {
        String input = args[0];
        ArrayList<String> validInputs = new ArrayList<>();

        for(int i = 1; i < args.length; i++) //Starts at 1 because we don't want the first arg (the user's input)
        {
            validInputs.add(args[i]);
        }

        for(String validInput : validInputs)
        {
            if(input.equalsIgnoreCase(validInput))
            {
                return true;
            }
        }
        System.out.println(invalidInputMessagePrefix +"\""+ input +"\". The following options are valid: ");
        for(int i = 0; i < validInputs.size(); i++ )
        {
            if(i != validInputs.size()){//For all valid inputs except for the last one...
                System.out.print("\""+validInputs.get(i)+"\", ");
            }
            else //For the last valid input ...
            {
                System.out.print("and \""+validInputs.get(i)+"\".");
            }
        }
        return false;
    }

    public static String getStringFromConsole() {

        String input = "";

        input = scanner.nextLine(); //You have to save the value of scanner.nextLine because it changes each time you access it via scanner.next...

        return input;
    }

    public static int getIntFromConsole(){
        String input = "";
        int value;

        while(true) {
            try {
                input = scanner.nextLine();
                value = Integer.valueOf(input);
                break;
            } catch (Exception e) {
                System.out.println(invalidInputMessagePrefix + "\"" + input + "\". Please enter a whole number.");
            }
        }
        return value;
    }

    public static boolean getBooleanFromConsole(){
        String input;
        boolean value;
        while(true)
        {
            input = scanner.nextLine();
            if(input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no"))
            {
                if(input.equalsIgnoreCase("yes"))
                {
                    value = true;
                    break;
                }
                else
                {
                    value = false;
                    break;
                }
            }
            else
            {
                System.out.println(invalidInputMessagePrefix + "\"" + input + "\". Please enter \"Yes\" or \"No\".");
            }
        }
        return value;
    }
}
