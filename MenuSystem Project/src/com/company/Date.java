package com.company;

import com.company.Operations.DateInterface;
import com.company.Utilities.Temporals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalQuery;

import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;

public class Date implements DateInterface {

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

        int businessDays = 0;
        LocalDate date = start.minusDays(1);
        while (date.isBefore(end)){
            //Adjust into the next working day
            date = date.with(Temporals.nextWorkingDay());
            if(date.isBefore(end))
                businessDays++;
        }
        return businessDays;
    };

    @Override
    public TemporalQuery<Integer> workDaysUntilDate() {
        return workDaysUntilDateQuery;
    }

    @Override
    public LocalDate weekendDaysUntilDate() {
        return null;
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
