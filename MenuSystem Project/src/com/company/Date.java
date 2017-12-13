package com.company;

import com.company.Operations.DateInterface;
import com.company.Utilities.Temporals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalQuery;
import java.time.temporal.TemporalUnit;

import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;

public class Date implements DateInterface {

    private TemporalUnit currentUnit = null;
    private long ammount = 0;

    private int CheckAllDays(LocalDate start, LocalDate end, TemporalAdjuster adjuster){
        if(start == null || end == null)
            throw new NullPointerException();

        int resultDays = 0;
        LocalDate date = start.minusDays(1);
        while (date.isBefore(end)){
            date = date.with(adjuster);
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

    private final TemporalQuery<Integer> workDaysUntilDateQuery = (temporal) -> {
        LocalDate start = now();
        LocalDate end = tryGetValidDate(temporal);
        if(end == null)
            return Integer.MIN_VALUE;
        return CheckAllDays(start, end, Temporals.nextWorkingDay());
    };

    @Override
    public TemporalQuery<Integer> workDaysUntilDate() {
        return workDaysUntilDateQuery;
    }

    private final TemporalQuery<Integer> weekendDaysUntilDateQuery = (temporal) -> {
        LocalDate start = now();
        LocalDate end = tryGetValidDate(temporal);
        if(end == null)
            return Integer.MIN_VALUE;
        return CheckAllDays(start, end, Temporals.nextWeekendDay());
    };

    @Override
    public TemporalQuery<Integer> weekendDaysUntilDate() {
        return weekendDaysUntilDateQuery;
    }

    @Override
    public LocalDate checkHoliday() {
        return null;
    }

    @Override
    public LocalDate addDateToCurrentDate() {
        return null;
    }

    @Override
    public LocalDate subtractDateFromCurrentDate() {
        return null;
    }

    @Override
    public LocalDate differenceBetweenDates() {
        return null;
    }
}
