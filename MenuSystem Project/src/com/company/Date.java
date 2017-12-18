package com.company;

import com.company.Operations.DateInterface;
import com.company.Utilities.Temporals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.*;

import static java.time.LocalDate.from;
import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;

public class Date implements DateInterface {

    private int CheckAllDays(LocalDate start, LocalDate end, TemporalAdjuster adjuster){
        if(start == null || end == null)
            throw new NullPointerException();

        int resultDays = 0;
        LocalDate date = start.minusDays(1);
        while (date.isBefore(end)){
            date = date.with(adjuster);
            if(date.isAfter (end)) {
                resultDays -= Period.between (end, date).getDays ();
                break;
            }
            if(date.isBefore(end))
                resultDays++;
        }
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


    private TemporalQuery<Integer> workDaysUntil(boolean includeHolidays, String country){

        return (temporal) -> {
            LocalDate start = now();
            LocalDate end = tryGetValidDate(temporal);
            if(end == null)
                return Integer.MIN_VALUE;
            if(includeHolidays)
                return CheckAllDays(start, end, Temporals.nextWorkingDayWithHolidays (true, country));
            return CheckAllDays(start, end, Temporals.nextWorkingDay());
        };
    }

    @Override
    public TemporalQuery<Integer> workDaysUntilDate(boolean includeHolidays, String country) {
        return workDaysUntil(includeHolidays, country);
    }


    private TemporalQuery<Integer> holidaysUntil(boolean includeHolidays, String country) {
        return (temporal) -> {
            LocalDate start = now ();
            LocalDate end = tryGetValidDate (temporal);
            if (end == null)
                return Integer.MIN_VALUE;
            return CheckAllDays (start, end, Temporals.nextHoliday (includeHolidays, country));
        };
    }

    @Override
    public TemporalQuery<Integer> holidaysUntilDate(boolean includeWeekends, String Country) {
        return holidaysUntil (includeWeekends, Country);
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
