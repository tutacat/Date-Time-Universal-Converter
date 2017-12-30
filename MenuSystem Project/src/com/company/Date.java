package com.company;

import com.company.Operations.DateInterface;
import com.company.Utilities.Colorfull_Console.ColorfulConsole;
import com.company.Utilities.Events.Delegate;
import com.company.Utilities.Events.Event;
import com.company.Utilities.Events.EventExecutor;
import com.company.Utilities.Events.EventListener;
import com.company.Utilities.Temporals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.*;

import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.*;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.Bold;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.Regular;
import static java.time.LocalDate.from;
import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;

public class Date implements DateInterface, EventListener {

    EventExecutor onOperationProgressUpdate = new EventExecutor (0, new Delegate (void.class, float.class));

    private float progress = 0;

    public Date(){
        onOperationProgressUpdate.RegisterListener (this);
    }

    @Event(eventExecutorCode = 0)
    public void setExecutor(float progress){
        ColorfulConsole.Write (Blue (Bold), "<[");
        for (int i = 0; i < 100; i += 5) {
            if(i < progress) {
                if(i > progress - 5)
                    ColorfulConsole.Write (Green (Bold), "-");
                else ColorfulConsole.Write (Green (Bold), "=");
            }else ColorfulConsole.Write (White (Regular), " ");
        }
        ColorfulConsole.Write (Blue (Bold), "]> " + progress + "%\n");
    }

    private int CheckAllDays(LocalDate start, LocalDate end, TemporalAdjuster adjuster){
        if(start == null || end == null)
            throw new NullPointerException();

        int resultDays = 0;
        LocalDate date = start.minusDays(1);
        int yearFrom = date.getYear ();
        int to = end.getYear ();
        int currentYear = 0;

        onOperationProgressUpdate.Invoke (progress);

        while (date.isBefore(end)){

            date = date.with(adjuster);
            if(date.isAfter (end))
                break;

            //Update Progress
            if(currentYear != date.getYear ()) {
                currentYear = date.getYear ();
                int div = (to - yearFrom);
                if(div == 0) {
                    div = 1;
                }
                progress += 100 / div;
                onOperationProgressUpdate.Invoke (progress);
            }

            if(date.isBefore(end))
                resultDays++;
        }
        onOperationProgressUpdate.Invoke (progress);
        progress = 0;
        ColorfulConsole.WriteLine (Green (Regular), "Done!");
        return resultDays;
    }

    private final TemporalQuery <DayOfWeek> xthDayOfXthWeekQuery = (temporal) -> {
        LocalDate date = tryGetValidDate(temporal);
        if (date == null)
            return DayOfWeek.from(now());
        return DayOfWeek.from(date);
    };

    public TemporalQuery<DayOfWeek> xthDayOfXthWeek() {
        return xthDayOfXthWeekQuery;
    }

    private final TemporalQuery<DayOfWeek> firstWeekDayOfXthYearQuery =
            (temporal) -> {
                LocalDate date = tryGetValidDate(temporal);
                if(date == null)
                    return null;
                LocalDate adjusted = (LocalDate) TemporalAdjusters.firstDayOfYear().adjustInto(date);
                return DayOfWeek.from(adjusted);
            };

    @Override
    public TemporalQuery <DayOfWeek> firstWeekDayOfXthYear() {
        return firstWeekDayOfXthYearQuery;
    }

    private final TemporalQuery<Integer> xthDayOfXthYearQuery =
            (temporal) -> {
                LocalDate date = tryGetValidDate(temporal);
                if(date == null)
                    return -1;
                return date.getDayOfYear();
            };

    @Override
    public TemporalQuery <Integer> xthDayOfXthYear() {
        return xthDayOfXthYearQuery;
    }

    private final TemporalQuery<Integer> daysLeftUntilXthDayQuery =
            (temporal) ->{
                LocalDate date = tryGetValidDate(temporal);
                if(date == null)
                    return Integer.MIN_VALUE;
                if(now().isBefore(date))
                    return Math.toIntExact(DAYS.between(now(), date));
                return 0;
            };

    @Override
    public TemporalQuery<Integer> daysLeftUntilXthDay() {
        return daysLeftUntilXthDayQuery;
    }



    private TemporalQuery<Integer> workDaysUntil(Temporal startingDate, boolean includeHolidays, String country){
        return (temporal) -> {
            LocalDate start = tryGetValidDate (startingDate);
            LocalDate end = tryGetValidDate(temporal);
            if(end == null)
                return Integer.MIN_VALUE;
            if(includeHolidays)
                return CheckAllDays(start, end, Temporals.nextWorkingDayWithHolidays (true, country));
            return CheckAllDays(start, end, Temporals.nextWorkingDay());
        };
    }

    @Override
    public TemporalQuery<Integer> workDaysUntilDate(Temporal startingDate, boolean includeHolidays, String country) {
        if(startingDate == null || startingDate.equals (now ()))
            startingDate = now ();
        return workDaysUntil(startingDate, includeHolidays, country);
    }

    private TemporalQuery<Integer> holidaysUntil(Temporal startingDate, boolean includeHolidays, String country) {
        return (temporal) -> {
            LocalDate start = tryGetValidDate (startingDate);
            LocalDate end = tryGetValidDate (temporal);
            if (end == null)
                return Integer.MIN_VALUE;
            return CheckAllDays (start, end, Temporals.nextHoliday (includeHolidays, country));
        };
    }

    @Override
    public TemporalQuery<Integer> holidaysUntilDate(Temporal startingDate, boolean includeWeekends, String Country) {
        if(startingDate == null || startingDate.equals (now ()))
            startingDate = now ();
        return holidaysUntil (startingDate, includeWeekends, Country);
    }

    @Override
    public TemporalAdjuster checkHoliday(String country) {
        return Temporals.nextWorkingDayWithHolidays (true, country);
    }

    @Override
    public TemporalQuery <TemporalAccessor> addDateToCurrentDate(Temporal dateToAdd)
    {
        return temporal -> {
            LocalDate date = from(temporal);
            return LocalDate.from(Temporals.addTemporalToTemporal(date, dateToAdd));
        };
    }

    @Override
    public TemporalQuery <TemporalAccessor> subtractDateFromCurrentDate(Temporal dateToSubtract) {
        return temporal -> {
            LocalDate date  = from(temporal);
            return LocalDate.from(Temporals.subtractTemporalToTemporal(date, dateToSubtract));
        };
    }

    @Override
    public LocalDate differenceBetweenDates() {
        return null;
    }
}
