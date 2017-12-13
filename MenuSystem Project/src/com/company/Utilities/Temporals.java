package com.company.Utilities;

import java.time.temporal.*;

import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoUnit.DAYS;

public class Temporals {

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

    public static TemporalAdjuster nextWeekendDay(){
        return WeekendAdjuster.NEXT_WEEKEND;
    }

    private static enum WeekendAdjuster implements TemporalAdjuster {
        /**
         * Adjust into the next weekend
         * Start of Saturday
         */
        NEXT_WEEKEND {
            @Override
            public Temporal adjustInto(Temporal temporal) {
                int dow = temporal.get(DAY_OF_WEEK);
                if(dow == 6 || dow == 7){
                    return temporal.plus(dow, DAYS);
                }
                return temporal.plus((7 - 1) - dow, DAYS);
            }
        },
    }

    //-----------------------------------------------------------------------
    /**
     * Enum implementing the adjusters.
     */
    private static enum Adjuster implements TemporalAdjuster {
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
        },
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
