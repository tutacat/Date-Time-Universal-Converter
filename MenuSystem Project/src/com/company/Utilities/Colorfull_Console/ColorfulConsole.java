package com.company.Utilities.Colorfull_Console;

import java.util.InputMismatchException;
import java.util.Scanner;

import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.RESET;
import static java.lang.System.in;
import static java.lang.System.out;

public class ColorfulConsole {

    public static void WriteLine(AnsiColor color, String rawText){
        out.println(color.Color + rawText);
        Reset();
    }

    public static void WriteLine(AnsiColor color, AnsiColor backgroundColor, String rawText){
        out.println(backgroundColor.Color + color.Color + rawText);
        Reset();
    }

    public static void Write(AnsiColor text, String rawText){
        out.print(text.Color + rawText);
        Reset();
    }

    /**
     * Formats a text using the selected colors.
     * Colors are applied with the order used in the last parameter
     *
     * Format uses eg: {0}  where 0 is the first color used in the parameters
     *
     * eg: "{0}Hello {1}World" Hello will have a color associated and World other color
     *
     * @param FormattedText Text including Braces
     * @param colors Colors in order of using
     */
    public static void WriteLineFormatted(String FormattedText, AnsiColor... colors){
        char[] textArray = FormattedText.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < textArray.length; i++) {
            if(textArray[i] == '}'){
                continue;
            }
            if(textArray[i] == '{'){
                int index = Integer.parseInt(String.valueOf(textArray[i+1]));
                stringBuilder.append(colors[index].Color);
                i++;
                continue;
            }
            stringBuilder.append(textArray[i]);
        }
        out.println(stringBuilder.toString());
        Reset();
    }

    private static Scanner scanner = new Scanner(in);
    private static Scanner intScanner = new Scanner(in);

    public static int ReadNextInt() {
        int result;
        try {
            result = intScanner.nextInt();
        }
        catch (InputMismatchException e){
            /*
            * Ok so consecutive next Int
            * would not work! i needed to call the next
            * to get the stream going
            *
            * i can still have some errors
            * wtv
            * */
            intScanner.next();
            return -1;
        }
        intScanner.nextLine();
        return result;
    }

    public static String ReadNext() {
        if(scanner.hasNext())
            return scanner.next();
        else return "";
    }

    private static void Reset(){
        out.print(RESET);
        out.flush();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
