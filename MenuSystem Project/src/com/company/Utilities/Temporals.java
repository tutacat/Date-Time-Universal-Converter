package com.company.Utilities;

import com.company.App;
import com.company.Utilities.Net.Holiday;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.*;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;

public class Temporals {

    public static TemporalAdjuster timeAtTimeZone(ZoneId id){
        return temporal -> {
            LocalDateTime from = LocalDateTime.from(temporal);
            return id.getRules().getOffset(from).adjustInto(from);
        };
    }

    public static Temporal addTemporalToTemporal(Temporal temporal, Temporal toAdd){
        if(temporal.isSupported (NANOS) && temporal.isSupported (NANO_OF_SECOND))
            temporal = temporal.plus (toAdd.get (NANO_OF_SECOND), NANOS);
        if(temporal.isSupported (SECONDS)&& temporal.isSupported (SECOND_OF_MINUTE)){
            temporal = temporal.plus (toAdd.get (SECOND_OF_MINUTE), SECONDS);
        }if(temporal.isSupported (MINUTES)&& temporal.isSupported (MINUTE_OF_HOUR)){
            temporal =temporal.plus (toAdd.get (MINUTE_OF_HOUR), MINUTES);
        }if(temporal.isSupported (HOURS)&& temporal.isSupported (HOUR_OF_DAY)){
            temporal = temporal.plus (toAdd.get (HOUR_OF_DAY), HOURS);
        }if(temporal.isSupported (DAYS)&& temporal.isSupported (DAY_OF_MONTH)){
            temporal =temporal.plus (toAdd.get (DAY_OF_MONTH), DAYS);
        }if(temporal.isSupported (MONTHS)&& temporal.isSupported (MONTH_OF_YEAR)){
            temporal =temporal.plus (toAdd.get (MONTH_OF_YEAR), MONTHS);
        }if(temporal.isSupported (YEARS)&& temporal.isSupported (YEAR_OF_ERA)){
            temporal =temporal.plus (toAdd.get (YEAR_OF_ERA), YEARS);
        }
        return temporal;
    }

    public static Temporal subtractTemporalToTemporal(Temporal temporal, Temporal toSubtract){
        if(temporal.isSupported (NANOS) && temporal.isSupported (NANO_OF_SECOND))
            temporal =temporal.minus (toSubtract.get (NANO_OF_SECOND), NANOS);
        if(temporal.isSupported (SECONDS)&& temporal.isSupported (SECOND_OF_MINUTE)){
            temporal =temporal.minus(toSubtract.get (SECOND_OF_MINUTE), SECONDS);
        }if(temporal.isSupported (MINUTES)&& temporal.isSupported (MINUTE_OF_HOUR)){
            temporal = temporal.minus(toSubtract.get (MINUTE_OF_HOUR), MINUTES);
        }if(temporal.isSupported (HOURS)&& temporal.isSupported (HOUR_OF_DAY)){
            temporal = temporal.minus(toSubtract.get (HOUR_OF_DAY), HOURS);
        }if(temporal.isSupported (DAYS)&& temporal.isSupported (DAY_OF_MONTH)){
            temporal =temporal.minus(toSubtract.get (DAY_OF_MONTH), DAYS);
        }if(temporal.isSupported (MONTHS)&& temporal.isSupported (MONTH_OF_YEAR)){
            temporal = temporal.minus(toSubtract.get (MONTH_OF_YEAR), MONTHS);
        }if(temporal.isSupported (YEARS)&& temporal.isSupported (YEAR_OF_ERA)){
            temporal = temporal.minus(toSubtract.get (YEAR_OF_ERA), YEARS);
        }
        return temporal;
    }

    /*TODO: Add support for a more generic working days
        Like holidays and stuff
    */

    /**
     * Returns an adjuster that returns the next working day, ignoring Saturday and Sunday.
     */
    public static TemporalAdjuster nextWorkingDay() {
        return Adjuster.NEXT_WORKING;
    }

    /**
     * Returns an adjuster that returns the previous working day, ignoring Saturday and Sunday.
     */
    public static TemporalAdjuster previousWorkingDay() {
        return Adjuster.PREVIOUS_WORKING;
    }

    public static TemporalAdjuster nextWorkingDayWithHolidays(boolean includeWeekends,
                                                              String country) {
        return nextWorkingWithHolidays(includeWeekends, country);
    }

    public static TemporalAdjuster nextHoliday(boolean includeWeekEnds, String country){
        return adjustToNextHoliday (includeWeekEnds, country);
    }

    public static TemporalAdjuster nextWeekendDay(){
        return WeekendAdjuster.NEXT_WEEKEND;
    }



    private enum WeekendAdjuster implements TemporalAdjuster {
        /**
         * Adjust into the next weekend
         * Start of Saturday
         */
        NEXT_WEEKEND {
            @Override
            public Temporal adjustInto(Temporal temporal) {
                int dow = temporal.get(DAY_OF_WEEK);
                if(dow == 6)
                    return temporal.plus(dow + 1, DAYS);
                if(dow == 7)
                    return temporal.plus (dow - 1, DAYS);
                return temporal.plus((7 - 1) - dow, DAYS);
            }
        },
    }

    //-----------------------------------------------------------------------
    /**
     * Enum implementing the adjusters.
     */
    private enum Adjuster implements TemporalAdjuster {
        /** Next working day adjuster. */
        NEXT_WORKING {
            @Override
            public Temporal adjustInto(Temporal temporal) {
                int dow = temporal.get(DAY_OF_WEEK);
                switch (dow) {
                    case 6:  // Saturday
                        return temporal.plus(2, DAYS);
                    case 5:  // Friday
                        return temporal.plus(3, DAYS);
                    default:
                        return temporal.plus(1, DAYS);
                }
            }
        },
        /** Previous working day adjuster. */
        PREVIOUS_WORKING {
            @Override
            public Temporal adjustInto(Temporal temporal) {
                int dow = temporal.get(DAY_OF_WEEK);
                switch (dow) {
                    case 1:  // Monday
                        return temporal.minus(3, DAYS);
                    case 7:  // Sunday
                        return temporal.minus(2, DAYS);
                    default:
                        return temporal.minus(1, DAYS);
                }
            }
        }
    }



    private static int prevYear = 0;
    private static List<Holiday> holidays;
    /**
     * Adjusts a date to the next working day taking in count Holidays
     *  and weekends if includeWeekends is equals to TRUE
     *
     * @param includeWeekends Include weekends as non working days
     * @param country check working days in a certain country
     *                Every country has their own Holidays
     * @return Adjuster to to adjust a temporal
     */
    private static TemporalAdjuster nextWorkingWithHolidays(boolean includeWeekends, String country){
        return temporal -> {
            LocalDate localDate = LocalDate.from (temporal);
            if(prevYear != localDate.getYear ()) {
                prevYear = localDate.getYear ();
                if(holidays != null)
                    holidays.clear ();
                holidays = new ArrayList <> (App.holidaysManager.getHolidays (country, localDate.getYear ()));
            }
            for (Holiday holiday : holidays)
            {
                LocalDate holidayDate = holiday.getHolidayDate ();
                if(holidayDate.isAfter (localDate))
                {
                    LocalDate tomorrowDate = localDate.plusDays (1);
                    if(tomorrowDate.isEqual (holidayDate))
                    {
                        //TODO: REMOVE OR NOT?--------------------------------
                        //ColorfulConsole.WriteLine (Green (Underline), holiday.getHolidayName ());
                        //----------------------------------------------------
                        return localDate.plus (1, DAYS);
                    }
                    if(includeWeekends)
                        return LocalDate.from (nextWorkingDay ().adjustInto (localDate));
                    else return localDate.plus (1, DAYS);
                }
            }
            if(includeWeekends)
                return LocalDate.from (nextWorkingDay ().adjustInto (localDate));
            else return localDate.plus (1, DAYS);
        };
    }


    public static boolean weekend = false;
    private static TemporalAdjuster adjustToNextHoliday(boolean includeWeekEnds, String country) {
        return temporal -> {
            LocalDate localDate = LocalDate.from (temporal);
            if(prevYear != localDate.getYear ()) {
                prevYear = localDate.getYear ();
                if(holidays != null)
                    holidays.clear ();
                holidays = new ArrayList <> (App.holidaysManager.getHolidays (country, localDate.getYear ()));
            }
            if(includeWeekEnds && weekend) {
                //Advance to Monday from Saturday
                weekend = false;
                return localDate.plus (2, DAYS);
            }

            for (Holiday holiday : holidays) {
                LocalDate holidayDate = holiday.getHolidayDate ();
                LocalDate adjustedToWeekEnd = LocalDate.from (nextWeekendDay ().adjustInto (localDate));
                if (holidayDate.isAfter (localDate))
                {
                    int value = holidayDate.getDayOfWeek ().getValue ();
                    if (value >= 6) {
                        //Holidays is on a weekend
                        if (includeWeekEnds) {
                            weekend = true;
                            return adjustedToWeekEnd;
                        }
                    }
                    if (holidayDate.isBefore (adjustedToWeekEnd)) {
                        return holidayDate;
                    } else {
                        weekend = true;
                        return adjustedToWeekEnd;
                    }
                }
            }
            return nextWorkingDay ().adjustInto (localDate);
        };
    }

    static ValueRange SecondsRange =  ValueRange.of(0,59);
    static ValueRange MinutesRange = ValueRange.of(0,59);
    static ValueRange HoursRange = ValueRange.of(0, 23);

    static  ValueRange DaysRange = ValueRange.of(1,31);
    static ValueRange MonthsRange = ValueRange.of(1,12);
    static ValueRange YearsRange = ValueRange.of(0, Integer.MAX_VALUE);

    public static boolean valueIsValid(TemporalUnit unit, int value){
        if(unit.isTimeBased()) {
            if (unit == ChronoUnit.SECONDS)
                return SecondsRange.isValidValue(value);
            if (unit == ChronoUnit.MINUTES)
                return MinutesRange.isValidValue(value);
            if(unit == ChronoUnit.HOURS)
                return HoursRange.isValidValue(value);
        }
        if(unit.isDateBased()){
            if(unit == ChronoUnit.DAYS)
                return DaysRange.isValidValue(value);
            if(unit == ChronoUnit.MONTHS)
                return MonthsRange.isValidValue(value);
            if(unit == ChronoUnit.YEARS)
                return YearsRange.isValidValue(value);
        }
        return false;
    }
}
