package com.company.Utilities;

import com.company.Utilities.Colorfull_Console.ColorfulConsole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Green;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.Underline;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Red;
import static java.time.temporal.ChronoUnit.*;

public class ChronoMenusUtilities {

    /**
     * Create a LocalDate from user input
     * */
    public static LocalDate CreateLocalDate(){
        int day = validateInput(DAYS);
        int month = validateInput(MONTHS);
        int year = validateInput(YEARS);
        return LocalDate.of(year, month, day);
    }

    /**
     * Create a LocalTime from user input
     * */
    public static LocalTime CreateLocalTime(){
        int hour = validateInput(HOURS);
        int minute = validateInput(MINUTES);
        int second = validateInput(SECONDS);
        return LocalTime.of(hour, minute, second);
    }

    /**
     * Create a LocalDateTime from user input
     * */
    public static LocalDateTime CreateLocalDateTime(){
        LocalDate date = CreateLocalDate();
        LocalTime time = CreateLocalTime();
        return LocalDateTime.of(date, time);
    }

    static int validateInput(ChronoUnit unit){
        int v;
        while (true){
            ColorfulConsole.Write(Green(Underline), String.format("%s:",
                    unit.toString().replace('s', ' ')));
            v = ColorfulConsole.ReadNextInt();
            if(v == -1) {
                ColorfulConsole.Write(Red(Underline), "Value not valid\n");
                continue;
            }
            boolean b = Temporals.valueIsValid(unit, v);
            if(b)
                break;
            ColorfulConsole.Write(Red(Underline), unit.toString() + " Value not valid\n");
        }
        return v;
    }
}
