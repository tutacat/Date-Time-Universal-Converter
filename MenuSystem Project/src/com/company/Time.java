package com.company;

import com.company.Operations.TimeInterface;
import com.company.Utilities.Temporals;

import java.time.LocalTime;
import java.time.temporal.*;

import static java.time.LocalTime.from;
import static java.time.LocalTime.ofSecondOfDay;

public class Time implements TimeInterface {

    public TemporalQuery<LocalTime> timeLeftUntilXthHour(){ return timeLeftUntilXthHourQuery; }

    private final TemporalQuery<LocalTime> xthSecondOfDayQuery(int seconds)
    {
        return temporal -> {
            LocalTime adjustedTime = LocalTime.ofSecondOfDay(seconds);
            return adjustedTime;
        };
    }

    public TemporalQuery<LocalTime> xthSecondOfDay(int seconds){ return xthSecondOfDayQuery(seconds); }

    public LocalTime differenceBetweenHours(){ return null; };


    private final TemporalQuery<TemporalAccessor> addHourToCurrentTimeQuery(Temporal hourToAdd)
    {
        return temporal -> {
            LocalTime time = from(temporal);
            return LocalTime.from(Temporals.addTemporalToTemporal(time, hourToAdd));
        };
    }

    public TemporalQuery<TemporalAccessor> addHourToCurrentTime(Temporal hourToAdd){ return addHourToCurrentTimeQuery(hourToAdd); }


    private final TemporalQuery <TemporalAccessor> subtractHourFromCurrentTimeQuery(Temporal hourToSubtract)
    {
        return temporal -> {
            LocalTime time = from(temporal);
            return LocalTime.from(Temporals.subtractTemporalToTemporal(time, hourToSubtract));
        };
    }

    public TemporalQuery<TemporalAccessor> subtractHourFromCurrentTime(Temporal hourToSubtract){ return subtractHourFromCurrentTimeQuery(hourToSubtract); }
}