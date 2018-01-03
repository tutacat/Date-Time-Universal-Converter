package com.company.Utilities.UserInterface;

import com.company.App;
import com.company.Chronometer;
import com.company.Date;
import com.company.DateConverter;
import com.company.Operations.*;
import com.company.Utilities.ChronoMenusUtilities;
import com.company.Utilities.Colorfull_Console.ColorfulConsole;
import com.company.Utilities.Events.Event;
import com.company.Utilities.Events.EventListener;
import com.company.Utilities.Net.Holiday;
import com.company.Utilities.Temporals;
import com.company.Utilities.Tuple;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Locale;

import static com.company.App.SaveToFile;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.*;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.*;
import static java.time.Month.DECEMBER;
import static java.time.temporal.ChronoUnit.*;

public class Menus implements EventListener {

    private DateInterface dates;
    private IDateConverter dateConverter;
    private IChronometer chronometer;


    MenuInterface mainMenu;
    MenuInterface dateMenu;
    MenuInterface exitMenu;
    MenuInterface convertMenu;
    MenuInterface worldLocations;
    MenuInterface locationOptions;
    MenuInterface timeMenu;
    MenuInterface chronometerMenu;

    public void CreateMenus(Application app)
    {
        dateConverter = new DateConverter();

        mainMenu = MenuFactory.getMenu(app, "Main Menu");
        dateMenu = MenuFactory.getMenu(app, "Date Menu");
        exitMenu = MenuFactory.getMenu(app, "Exit Menu");

        convertMenu = MenuFactory.getMenu(app, "Converter Menu");

        worldLocations = MenuFactory.getMenu(app, "World Continents");
        worldLocations.SetRows (3);

        locationOptions = MenuFactory.getMenu (app, "Location Options");

        timeMenu = MenuFactory.getMenu(app, "Time Menu");

        chronometerMenu = MenuFactory.getMenu (app, "Chronometer");

        //================================= MAIN MENU =========================================================
        mainMenu.SetRows (2);
        mainMenu.SetHeader("This is the main menu");
        mainMenu.AddOption("Time Operations", () -> mainMenu);
        mainMenu.AddOption("Date Operations", () -> {
            if(dates == null)
                dates = new Date ();
            return dateMenu;
        });
        mainMenu.AddOption("Time Zone Operations", () -> mainMenu);
        mainMenu.AddOption ("Holidays", () -> {
            if(dates == null)
                dates = new Date ();
            return worldLocations;
        });
        mainMenu.AddOption ("Chronometer", () -> {
            chronometer = new Chronometer ();
            ((Chronometer)chronometer).onChronometerStateChanged.RegisterListener (this);
            chronometerMenu.SetHeader ("Chronometer State: " + ((Chronometer) chronometer).isStopped ());
            return chronometerMenu;
        });
        mainMenu.AddOption ("Toggle Save to file", () -> {
            ColorfulConsole.WriteLine(Green(Regular), "Should the application save All the holidays in a certain year\n" +
                    " every time the user requests holidays");
            SaveToFile = !SaveToFile;
            ColorfulConsole.WriteLineFormatted("{0}Save to File set to: {1}" + SaveToFile,
                    Green(Regular), Red(Regular));
            return mainMenu;
        });
        mainMenu.AddExitOption(exitMenu);
        //====================================================================================================

        //================================= DATES MENU =======================================================
        dateMenu.SetRows(2);

        dateMenu.SetMenuName("Date Menu");
        dateMenu.SetHeader("Menu that operate dates");
        dateMenu.AddOption("First Week Day", () -> {
            LocalDate customDate = ChronoMenusUtilities.CreateLocalDate();
            DayOfWeek dayOfWeek = customDate.query(dates.firstWeekDayOfXthYear());
            String formatted = String.format("{0}The First day of the week in the year of {1}%d {0}is: {1}%s",
                    customDate.getYear(), dayOfWeek.getDisplayName(TextStyle.FULL, Locale.UK));
            ColorfulConsole.WriteLineFormatted(formatted, Green(Regular), Red(Regular));
            return dateMenu;
        });

        dateMenu.AddOption(() -> {
            LocalDate customDate = ChronoMenusUtilities.CreateLocalDate();
            int res = customDate.query(dates.daysLeftUntilXthDay());
            String s = String.format("{0}There are {1}%d {0}days until {1}%s", res, customDate
                    .format(DateTimeFormatter.ISO_DATE));
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            /**
             * This is a example when we want to send some info to the result menu
             * */
            return new Tuple<>(convertMenu, new Object[]{  ChronoUnit.DAYS, res } );
        }, "Days Until");

        dateMenu.AddOption("Day of the Year", () -> {
            LocalDate customDate = ChronoMenusUtilities.CreateLocalDate();
            Integer query = customDate.query(dates.xthDayOfXthYear());
            String s = String.format("{0}The day of the Year of {1}%s {0}is {1}%d",
                    customDate.format(DateTimeFormatter.ISO_DATE), query);
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            return dateMenu;
        });

        dateMenu.AddOption("Day of the Week", () -> {
            LocalDate customDate = ChronoMenusUtilities.CreateLocalDate();
            DayOfWeek query = customDate.query(dates.xthDayOfXthWeek ());
            String s = String.format("{0}The day of the Week of {1}%s {0}is {1}%s",
                    customDate.format(DateTimeFormatter.ISO_DATE), query.getDisplayName (TextStyle.FULL, Locale.UK));
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            return dateMenu;
        });


        dateMenu.AddOption ("Subtract a Date to a Date", () -> {
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
            return dateMenu;
        });


        dateMenu.AddOption ("Sum a Date to a Date", () -> {
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
            return dateMenu;
        });

        dateMenu.AddExitOption(mainMenu);
        //====================================================================================================

        //================================= Converter Menu ========================================================
        convertMenu.AddOption("Convert to Milliseconds", () ->
        {
            Object[] arg = (Object[]) convertMenu.getArg();
            TemporalUnit temporalUnit = (TemporalUnit) arg[0];
            int amount = (int) arg[1];
            long i = dateConverter.toMilliseconds((TemporalUnit) arg[0], amount);
            String s = String.format("{1}%d {0}%s are equivalent to: {1}%d {0}%s", amount,
                    temporalUnit.toString(), i, MILLIS.toString());
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            return convertMenu;
        });

        convertMenu.AddOption("Convert to Seconds", () ->
        {
            Object[] arg = (Object[]) convertMenu.getArg();
            TemporalUnit converterTemporal = (TemporalUnit) arg[0];
            int amount = (int) arg[1];
            long i = dateConverter.toSeconds(converterTemporal, amount);
            String s = String.format("{1}%d {0}%s are equivalent to: {1}%d {0}%s", amount,
                    converterTemporal.toString(), i, SECONDS.toString());
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            return convertMenu;
        });

        convertMenu.AddOption("Convert to Minutes", () ->
        {
            Object[] arg = (Object[]) convertMenu.getArg();
            TemporalUnit converterTemporal = (TemporalUnit) arg[0];
            int amount = (int) arg[1];
            long i = dateConverter.toMinutes(converterTemporal, amount);
            String s = String.format("{1}%d {0}%s are equivalent to: {1}%d {0}%s", amount,
                    converterTemporal.toString(), i, MINUTES.toString());
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            return convertMenu;
        });

        convertMenu.AddOption("Convert to Hours", () ->
        {
            Object[] arg = (Object[]) convertMenu.getArg();
            TemporalUnit converterTemporal = (TemporalUnit) arg[0];
            int amount = (int) arg[1];
            long i = dateConverter.toHours(converterTemporal, amount);
            String s = String.format("{1}%d {0}%s are equivalent to: {1}%d {0}%s", amount,
                    converterTemporal.toString(), i, HOURS.toString());
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            return convertMenu;
        });

        convertMenu.AddExitOption(mainMenu);
        //====================================================================================================

        //=====================================HOLIDAYS=======================================================
        App.holidaysManager.loadZones();
        //final int[] i = {0};
        for (Tuple t : App.holidaysManager.Countries){
            //MenuInterface location = locations[i[0]];
            worldLocations.AddOption(() -> {
                locationOptions.SetHeader ((String) t.b);
                return new Tuple <> (locationOptions, new Object[]{t});
            }, (String) t.b);
            //for (Object o : ((ArrayList) t.b)) {
            //    String oS = (String) o;
            //    location.AddOption (() -> new Tuple<>(locationOptions, new Object[]{ oS } ), oS);
            //}
            //location.AddExitOption (worldLocations);
            //i[0]++;
        }

        worldLocations.AddExitOption (mainMenu);
        //====================================================================================================

        //================================= LOCATION OPTIONS =================================================
        locationOptions.AddOption ("Show All holidays given a Year", () -> {
            int nextInt = -10;
            while(!Temporals.valueIsValid (ChronoUnit.YEARS, nextInt)){
                ColorfulConsole.Write (Green (Regular), "Year > ");
                nextInt = ColorfulConsole.ReadNextInt ();
            }

            Tuple<String, String> t = (Tuple) ((Object[]) locationOptions.getArg ())[0];
            String s = t.a;
            List<Holiday> holidays = App.holidaysManager.getHolidays (s, nextInt);
            String formatted = String.format ("{0}Found {1}%d{0} Holidays in {1}%s", holidays.size (), t.b);
            ColorfulConsole.WriteLineFormatted (formatted, Green (Regular), Red (Underline));
            final int[] counter = {0,0};
            //JUst dont use january because if January wont be displayed
            final Month[] cMonth = {DECEMBER};
            holidays.forEach((Holiday r) -> {
                Month month = r.getHolidayDate ().getMonth ();
                if(month.getValue () != cMonth[0].getValue ()){
                    ColorfulConsole.WriteLine (White (Regular), "");
                    cMonth[0] = month;
                    String s2 = "----------------%14s%-10s----------------";
                    String format = String.format (s2, cMonth[0], "");
                    ColorfulConsole.WriteLine (Green (Bold), format);
                }
                DayOfWeek holidayDayOfWeek = r.getHolidayDayOfWeek ();
                if(holidayDayOfWeek.getValue () >= 6){
                    String s1 = String.format ("{0}%s {1}%10s", r.toString (), r.getHolidayName ());
                    ColorfulConsole.WriteLineFormatted (s1, Red (Regular), Cyan (Underline));
                    counter[0]++;
                }
                else {
                        ColorfulConsole.WriteLineFormatted ("{0}" + r.toString () +
                            " {1}" + r.getHolidayName (), Blue (Regular), Cyan (Underline));
                        counter[1]++;
                    }
            });
            String res = String.format ("{1}2018{0} -> In %s were found {1}%d {2}Weekend Holiday(s) {0}and {1}%d {2}Week Day Holiday(s)",
                    s, counter[0], counter[1]);
            ColorfulConsole.WriteLineFormatted (res, Green (Regular), Red (Regular), Blue (Bold));
            return mainMenu;
        });

        locationOptions.AddOption(() -> {
            Tuple<String, String> t = (Tuple) ((Object[]) locationOptions.getArg ())[0];
            String country = t.a;

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
                    t.a ,res, customDate.format(DateTimeFormatter.ofPattern("d MMM uuuu")));
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            return new Tuple<>(convertMenu, new Object[]{  ChronoUnit.DAYS, res } );
        }, "Working Days Until");

        locationOptions.AddOption(() -> {
            ColorfulConsole.WriteLine (Green (Underline) ,"Create a Finishing Date");
            LocalDate customDate = ChronoMenusUtilities.CreateLocalDate();
            ColorfulConsole.WriteLine (Green (Underline) ,"Create a Starting Date");
            LocalDate startDate = ChronoMenusUtilities.CreateLocalDate ();

            Tuple<String, String> t = (Tuple) ((Object[]) locationOptions.getArg ())[0];
            String country = t.a;
            boolean choice = choice("Include WeekEnds");
            int res = customDate.query(dates.holidaysUntilDate (startDate, choice, country));
            String s = String.format("{0}In {1}%s: {0}There are {1}%d {0}Weekend/Holiday Days until {1}%s",
                    t.b, res, customDate.format(DateTimeFormatter.ofPattern("d MMM uuuu")));
            ColorfulConsole.WriteLineFormatted(s, Green(Regular), Red(Regular));
            return new Tuple<>(convertMenu, new Object[]{  ChronoUnit.DAYS, res } );
        }, "Holiday Days Until");

        locationOptions.AddExitOption (mainMenu);
        //====================================================================================================

        //================================= TIME MENU ========================================================

        /*
        Falta adicionar as opções para os métodos do time menu
        Tem de receber uma operação (soma/subtração) para adicionar à hora atual
        Terá também uma opção para receber um inteiro de 0 a 86399 e invocar o método que determina a que hora corresponde o segundo dado
        ETA amanhã
         */

        //====================================================================================================

        //================================= CHRONOMETER MENU =================================================
        chronometerMenu.AddOption ("Start", () -> {
            chronometer.start ();
            return chronometerMenu;
        });
        chronometerMenu.AddOption ("Lap", () -> {
            chronometer.lap ();
            chronometer.Print ();
            return chronometerMenu;
        });
        chronometerMenu.AddOption ("Stop",() -> {
            chronometer.stop ();
            return chronometerMenu;
        });

        chronometerMenu.AddExitOption (mainMenu);
        //====================================================================================================


        //================================= EXIT MENU ========================================================
        exitMenu.AddOption("Press Enter", () -> {
            app.SetState(Application.State.closed);
            return exitMenu;
        });
        //====================================================================================================
    }

    @Event(eventExecutorCode = 0)
    public void setOnChronometerStateChangedEvent(boolean state) {
        if(state)
            chronometerMenu.SetHeader ("Chronometer State: Stopped");
        else chronometerMenu.SetHeader ("Chronometer State: Running");
    }

    private boolean choice(String additionalInfo){
        ColorfulConsole.WriteLine (Green (Underline), "Choose your Option 1 = YES other number = NO");
        ColorfulConsole.WriteLine (Green (Underline), additionalInfo);
        int i1 = ColorfulConsole.ReadNextInt ();
        boolean choice;
        choice = i1 == 1;
        return choice;
    }
}
