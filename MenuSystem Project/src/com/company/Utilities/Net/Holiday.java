package com.company.Utilities.Net;

import java.time.LocalDate;

public class Holiday  {
    private LocalDate holidayDate;
    private String holidayName;

    public LocalDate getHolidayDate() {
        return holidayDate;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public Holiday(LocalDate holidayDate, String holidayName){
        this.holidayDate = holidayDate;
        this.holidayName = holidayName;
    }
}
