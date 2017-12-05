package com.company;

import com.company.Operations.DateInterface;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDate.now;

public class Date implements DateInterface {

    private LocalDate fedDate;

    @Override
    public void SetCurrentLocalDate(LocalDate date) {
    if(date == null) {
            return;
        }
        this.fedDate = date;
    }

    @Override
    public LocalDate getCurrentLocalDate() {
        if(fedDate == null)
            throw new NullPointerException();
        return fedDate;
    }

    @Override
    public DayOfWeek firstWeekDayOfXthYear() {
        if(fedDate == null)
            return now().getDayOfWeek();
        int dayOfYear = fedDate.getDayOfYear();
        LocalDate fstDayOfYearDate = fedDate.minusDays(dayOfYear - 1);
        return fstDayOfYearDate.getDayOfWeek();
    }

    @Override
    public int xthDayOfXthYear() {
        if(fedDate == null)
            return now().getDayOfYear();
        return fedDate.getDayOfYear();
    }

    @Override
    public int xthDayOfXthWeek() {
        if(fedDate ==  null)
            return now().getDayOfWeek().getValue();
        return fedDate.getDayOfWeek().getValue();
    }

    @Override
    public int daysLeftUntilXthDay() {
        if(fedDate == null)
            return 0;
        if(now().isBefore(fedDate)) {
            return Math.toIntExact(ChronoUnit.DAYS.between(now(), fedDate));
        }
        return 0;
    }

    @Override
    public int workDaysUntilDate(int workDaysPerWeek) {
        int totalWorkingDays = 0;
        LocalDate plusDays = now();
        if(fedDate.isBefore(now()))
            return 0;
        while (true){
            plusDays = plusDays.plusDays(1);
            if(plusDays.isEqual(fedDate))
                return totalWorkingDays;
            if(plusDays.getDayOfWeek().getValue() <= workDaysPerWeek)
                totalWorkingDays++;
        }
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
