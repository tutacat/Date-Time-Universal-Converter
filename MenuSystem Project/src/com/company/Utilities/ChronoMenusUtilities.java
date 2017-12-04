package com.company.Utilities;

import java.time.LocalDate;

import static com.company.Utilities.ConsoleColors.AnsiColor.Green;
import static com.company.Utilities.ConsoleColors.AnsiColor.Modifier.Underline;

public class ChronoMenusUtilities {

    public static LocalDate CreateLocalDate(){
        ColorfulConsole.WriteLine(Green(Underline), "Create a Date");
        ColorfulConsole.Write(Green(Underline), "Day:");
        int day = ColorfulConsole.ReadNextInt();

        ColorfulConsole.Write(Green(Underline), "Month:");
        int month = ColorfulConsole.ReadNextInt();

        ColorfulConsole.Write(Green(Underline), "Year:");
        int year = ColorfulConsole.ReadNextInt();

        return LocalDate.of(year, month, day);
    }
}
