package com.company;

import com.company.Operations.TimezoneInterface;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalQuery;

public class TimeZone implements TimezoneInterface {

    private TemporalQuery <LocalDateTime> currentHourInTimezoneQuery;

    @Override
    public TemporalQuery <LocalDateTime> currentHourInTimezone() {
        return currentHourInTimezoneQuery;
    }

    @Override
    public ZonedDateTime convertTimezones() {
        return null;
    }
}