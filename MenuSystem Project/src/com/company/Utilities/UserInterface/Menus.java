package com.company.Utilities.UserInterface;

import com.company.App;
import com.company.Date;
import com.company.DateConverter;
import com.company.Operations.Application;
import com.company.Operations.DateInterface;
import com.company.Operations.IDateConverter;
import com.company.Operations.MenuInterface;
import com.company.Utilities.ChronoMenusUtilities;
import com.company.Utilities.Colorfull_Console.ColorfulConsole;
import com.company.Utilities.Events.EventListener;
import com.company.Utilities.Net.Holiday;
import com.company.Utilities.Temporals;
import com.company.Utilities.Tuple;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.company.App.SaveToFile;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.*;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.Regular;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.Underline;
import static java.time.temporal.ChronoUnit.*;

public class Menus implements EventListener {

    private static DateInterface dates;
    private static IDateConverter dateConverter;

    public static void CreateMenus(Application app)
    {
        dateConverter = new DateConverter();

        MenuInterface main_menu = MenuFactory.getMenu(app, "Main Menu");
        MenuInterface date_menu = MenuFactory.getMenu(app, "Date Menu");
        MenuInterface exit_menu = MenuFactory.getMenu(app, "Exit Menu");

        MenuInterface converter_Menu = MenuFactory.getMenu(app, "Converter Menu");

        MenuInterface worldLocations = MenuFactory.getMenu(app, "World Continents");
        worldLocations.SetRows (3);
        MenuInterface[] locations = {
                MenuFactory.getMenu (app, "North America"),
                MenuFactory.getMenu (app, "Atlantic"),
                MenuFactory.getMenu (app, "South America"),
                MenuFactory.getMenu (app, "Australia Pacific"),
                MenuFactory.getMenu (app, "UNWorld"),
                MenuFactory.getMenu (app, "Asia"),
                MenuFactory.getMenu (app, "Indian Ocean"),
                MenuFactory.getMenu (app, "Europe"),
                MenuFactory.getMenu (app, "Africa")};
        for (MenuInterface location : locations) {
            location.SetRows (5);
        }

        MenuInterface locationOptions = MenuFactory.getMenu (app, "Location Options");

        //================================= MAIN MENU =========================================================
        main_menu.SetRows (2);
        main_menu.SetHeader("This is the main menu");
        main_menu.AddOption("Time Operations", () -> main_menu);
        main_menu.AddOption("Date Operations", () -> {
            if(dates == null)
                dates = new Date ();
            return date_menu;
        });
        main_menu.AddOption("Time Zone Operations", () -> main_menu);
        main_menu.AddOption ("Holidays", () -> {
            if(dates == null)
                dates = new Date ();
            return worldLocations;
        });
        main_menu.AddOption ("Toggle Save to file", () -> {
            ColorfulConsole.WriteLine(Green(Regular), "Should the application save All the holidays in a certain year\n" +
                    " every time the user requests holidays");
            SaveToFile = !SaveToFile;
            ColorfulConsole.WriteLineFormatted("{0}Save to File set to: {1}" + SaveToFile,
                    Green(Regular), Red(Regular));
            return main_menu;
        });
        main_menu.AddExitOption(exit_menu);
        //====================================================================================================

        //================================= DATES MENU =======================================================
        date_menu.SetRows(2);

        date_menu.SetMenuName("Date Menu");
        date_menu.SetHeader("Menu that operate dates");
        date_menu.AddOption("First Week Day", () -> {
            LocalDate customDate = ChronoMenusUtilities.CreateLocalDate();
            DayOfWeek dayOfWeek = customDate.query(dates.firstWeekDayOfXthYear());
            String formatted = String.format("{0}The First day of the week in the year of {1}%d {0}is: {1}%s",
                    customDate.getYear(), dayOfWeek.getDisplayName(TextStyle.FULL, Locale.UK));
            ColorfulConsole.WriteLineFormatted(formatted, Green(Regular), Red(Regular));
            return date_menu;
        });

        date_menu.AddOption(() -> {
            LocalDate customDate = ChronoMenusUtilities.CreateLocalDate();
            int res = customDate.query(dates.daysLeftUntilXthDay());
            String s = String.format("{0}There are {1}%d {0}days until {1}%s", res, customDate
                    .format(DateTimeFormatter.ISO_DATE));
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            /**
             * This is a example when we want to send some info to the result menu
             * */
            return new Tuple<>(converter_Menu, new Object[]{  ChronoUnit.DAYS, res } );
        }, "Days Until");

        date_menu.AddOption("Day of the Year", () -> {
            LocalDate customDate = ChronoMenusUtilities.CreateLocalDate();
            Integer query = customDate.query(dates.xthDayOfXthYear());
            String s = String.format("{0}The day of the Year of {1}%s {0}is {1}%d",
                    customDate.format(DateTimeFormatter.ISO_DATE), query);
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            return date_menu;
        });

        date_menu.AddOption("Day of the Week", () -> {
            LocalDate customDate = ChronoMenusUtilities.CreateLocalDate();
            DayOfWeek query = customDate.query(dates.xthDayOfXthWeek ());
            String s = String.format("{0}The day of the Week of {1}%s {0}is {1}%s",
                    customDate.format(DateTimeFormatter.ISO_DATE), query.getDisplayName (TextStyle.FULL, Locale.UK));
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            return date_menu;
        });


        date_menu.AddOption ("Subtract a Date to a Date", () -> {
            ColorfulConsole.WriteLineFormatted("{0}Create a Date", Green(Regular));
            LocalDate customDate = ChronoMenusUtilities.CreateLocalDate ();
            ColorfulConsole.WriteLineFormatted("{0}Create a Date to subtract to the First date", Green(Regular));
            LocalDate customDate1 = ChronoMenusUtilities.CreateLocalDate ();

            TemporalAccessor query = customDate.query (dates.subtractDateFromCurrentDate (customDate1));
            LocalDate date = LocalDate.from (query);
            String s = String.format("{0}The result of Subtracting {1}%s {0}to {1}%s {0}is: {1}%s",
                    customDate.format(DateTimeFormatter.ISO_DATE),
                    customDate1.format(DateTimeFormatter.ISO_DATE),
                    date.format (DateTimeFormatter.ISO_DATE));
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            return date_menu;
        });


        date_menu.AddOption ("Sum a Date to a Date", () -> {
            ColorfulConsole.WriteLineFormatted("{0}Create a Date", Green(Regular));
            LocalDate customDate = ChronoMenusUtilities.CreateLocalDate ();
            ColorfulConsole.WriteLineFormatted("{0}Create a Date to add to the First date", Green(Regular));
            LocalDate customDate1 = ChronoMenusUtilities.CreateLocalDate ();

            TemporalAccessor query = customDate.query (dates.addDateToCurrentDate (customDate1));
            LocalDate date = LocalDate.from (query);
            String s = String.format("{0}The result of adding {1}%s {0}to {1}%s {0}is: {1}%s",
                    customDate.format(DateTimeFormatter.ISO_DATE),
                    customDate1.format(DateTimeFormatter.ISO_DATE),
                    date.format (DateTimeFormatter.ISO_DATE));
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            return date_menu;
        });

        date_menu.AddExitOption(main_menu);
        //====================================================================================================

        //================================= Converter Menu ========================================================
        converter_Menu.AddOption("Convert to Milliseconds", () ->
        {
            Object[] arg = (Object[]) converter_Menu.getArg();
            TemporalUnit temporalUnit = (TemporalUnit) arg[0];
            int amount = (int) arg[1];
            long i = dateConverter.toMilliseconds((TemporalUnit) arg[0], amount);
            String s = String.format("{1}%d {0}%s are equivalent to: {1}%d {0}%s", amount,
                    temporalUnit.toString(), i, MILLIS.toString());
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            return converter_Menu;
        });

        converter_Menu.AddOption("Convert to Seconds", () ->
        {
            Object[] arg = (Object[]) converter_Menu.getArg();
            TemporalUnit converterTemporal = (TemporalUnit) arg[0];
            int amount = (int) arg[1];
            long i = dateConverter.toSeconds(converterTemporal, amount);
            String s = String.format("{1}%d {0}%s are equivalent to: {1}%d {0}%s", amount,
                    converterTemporal.toString(), i, SECONDS.toString());
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            return converter_Menu;
        });

        converter_Menu.AddOption("Convert to Minutes", () ->
        {
            Object[] arg = (Object[]) converter_Menu.getArg();
            TemporalUnit converterTemporal = (TemporalUnit) arg[0];
            int amount = (int) arg[1];
            long i = dateConverter.toMinutes(converterTemporal, amount);
            String s = String.format("{1}%d {0}%s are equivalent to: {1}%d {0}%s", amount,
                    converterTemporal.toString(), i, MINUTES.toString());
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            return converter_Menu;
        });

        converter_Menu.AddOption("Convert to Hours", () ->
        {
            Object[] arg = (Object[]) converter_Menu.getArg();
            TemporalUnit converterTemporal = (TemporalUnit) arg[0];
            int amount = (int) arg[1];
            long i = dateConverter.toHours(converterTemporal, amount);
            String s = String.format("{1}%d {0}%s are equivalent to: {1}%d {0}%s", amount,
                    converterTemporal.toString(), i, HOURS.toString());
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            return converter_Menu;
        });

        converter_Menu.AddExitOption(main_menu);
        //====================================================================================================

        //=====================================HOLIDAYS=======================================================
        App.holidaysManager.loadZones();
        final int[] i = {0};
        for (Tuple t : App.holidaysManager.Columns){
            MenuInterface location = locations[i[0]];
            worldLocations.AddOption((String) t.a, () -> location);
            for (Object o : ((ArrayList) t.b)) {
                String oS = (String) o;
                location.AddOption (() -> new Tuple<>(locationOptions, new Object[]{ oS } ), oS);
            }
            location.AddExitOption (worldLocations);
            i[0]++;
        }

        worldLocations.AddExitOption (main_menu);
        //====================================================================================================

        //================================= LOCATION OPTIONS =================================================
        locationOptions.AddOption ("Show All holidays given a Year", () -> {
            int nextInt = -10;
            while(!Temporals.valueIsValid (ChronoUnit.YEARS, nextInt)){
                ColorfulConsole.Write (Green (Regular), "Year > ");
                nextInt = ColorfulConsole.ReadNextInt ();
            }

            String s = (String) ((Object[]) locationOptions.getArg ())[0];
            List<Holiday> holidays = App.holidaysManager.getHolidays (s, nextInt);
            String formatted = String.format ("{0}Found {1}%d{0} Holidays in {1}%s", holidays.size (), s);
            ColorfulConsole.WriteLineFormatted (formatted, Green (Regular), Red (Underline));
            holidays.forEach((r) -> ColorfulConsole.WriteLineFormatted ("{0}" + r.getHolidayDate () +
                    " {1}" + r.getHolidayName (), Blue (Regular), Cyan (Underline)));
            return main_menu;
        });

        locationOptions.AddOption(() -> {
            String country = (String) ((Object[]) locationOptions.getArg ())[0];

            //Create Custom Dates
            ColorfulConsole.WriteLine (Green (Underline) ,"Create a Finishing Date");
            LocalDate customDate = ChronoMenusUtilities.CreateLocalDate();
            ColorfulConsole.WriteLine (Green (Underline) ,"Create a Starting Date");
            LocalDate startDate = ChronoMenusUtilities.CreateLocalDate ();

            //Should the algorithm include holidays
            boolean choice = choice("?Include Holidays?");

            int res = customDate.query(dates.workDaysUntilDate(startDate, choice, country));

            String s = String.format("{0}In {1}%s: {0}Working from {1}Monday {0}to {1}Friday\n" +
                            "{0}There are {1}%d {0}Working Days until {1}%s",
                    country ,res, customDate.format(DateTimeFormatter.ofPattern("d MMM uuuu")));
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            return new Tuple<>(converter_Menu, new Object[]{  ChronoUnit.DAYS, res } );
        }, "Working Days Until");

        locationOptions.AddOption(() -> {
            ColorfulConsole.WriteLine (Green (Underline) ,"Create a Finishing Date");
            LocalDate customDate = ChronoMenusUtilities.CreateLocalDate();
            ColorfulConsole.WriteLine (Green (Underline) ,"Create a Starting Date");
            LocalDate startDate = ChronoMenusUtilities.CreateLocalDate ();
            String country = (String) ((Object[]) locationOptions.getArg ())[0];
            boolean choice = choice("Include WeekEnds");
            int res = customDate.query(dates.holidaysUntilDate (startDate, choice, country));
            String s = String.format("{0}In {1}%s: {0}There are {1}%d {0}Weekend/Holiday Days until {1}%s",
                    country, res, customDate.format(DateTimeFormatter.ofPattern("d MMM uuuu")));
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            return new Tuple<>(converter_Menu, new Object[]{  ChronoUnit.DAYS, res } );
        }, "Holiday Days Until");

        locationOptions.AddExitOption (main_menu);
        //====================================================================================================

        //================================= EXIT MENU ========================================================
        exit_menu.AddOption("Press Enter", () -> {
            app.SetState(Application.State.closed);
            return exit_menu;
        });
        //====================================================================================================
    }

    private static boolean choice(String additionalInfo){
        ColorfulConsole.WriteLine (Green (Underline), "Choose your Option 1 = YES other number = NO");
        ColorfulConsole.WriteLine (Green (Underline), additionalInfo);
        int i1 = ColorfulConsole.ReadNextInt ();
        boolean choice;
        choice = i1 == 1;
        return choice;
    }
}
