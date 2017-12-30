package com.company.Utilities.Net;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;


public class Holiday {
    public String Date;
    public String LocalName;
    public String Name;
    public String CountryCode;
    public Boolean Fixed;
    public Boolean CountyOfficialHoliday;
    public Boolean CountyAdministrationHoliday;
    public Boolean Global;

    public LocalDate getHolidayDate() {
        return LocalDate.parse (Date, DateTimeFormatter.ISO_DATE);
    }

    public String getHolidayName() {
        return Name;
    }

    public DayOfWeek getHolidayDayOfWeek(){
        return getHolidayDate ().getDayOfWeek ();
    }

    public String getHolidayDayOfWeekString() {
        return getHolidayDayOfWeek ().getDisplayName (TextStyle.FULL, Locale.UK);
    }

    @Override
    public String toString() {
        return toStringFormatted ();
    }

    public String getName() {
        return Name;
    }

    public String getLocalName() {
        return LocalName;
    }

    private String toStringFormatted(){
        return String.format ("%s %-5s", Date, getHolidayDayOfWeekString ().toUpperCase ());
    }
}
