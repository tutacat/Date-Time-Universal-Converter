package com.company;

import com.company.Operations.DateInterface;
import com.company.Utilities.Logger.LogSystem;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class Date implements DateInterface {

    private LocalDate fedDate;

    @Override
    public void SetCurrentLocalDate(LocalDate date) {
        if(date == null) {
            LogSystem.LogError("Parameter date cannot be null");
            return;
        }
        this.fedDate = date;
    }

    @Override
    public LocalDate getCurrentLocalDate() {
        return fedDate;
    }

    @Override
    public DayOfWeek firstWeekDayOfXthYear() {
        if(fedDate == null)
            return LocalDate.now().getDayOfWeek();

        else {
            int dayOfYear = fedDate.getDayOfYear();
            LocalDate fstDayOfYearDate = fedDate.minusDays(dayOfYear - 1);
            return fstDayOfYearDate.getDayOfWeek();
        }
    }

    @Override
    public int xthDayOfXthYear() {
        if(fedDate == null)
            return LocalDate.now().getDayOfYear();
        else {
            return fedDate.getDayOfYear();
        }
    }

    @Override
    public LocalDate xthDayOfXthWeek() {
        return null;
    }

    @Override
    public LocalDate daysLeftUntilXthDay() {
        return null;
    }

    @Override
    public LocalDate workDaysUntilDate() {
        return null;
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
