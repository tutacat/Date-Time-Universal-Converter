package com.company;

import com.company.Operations.TimeInterface;
import com.company.Utilities.Events.Delegate;
import com.company.Utilities.Events.EventExecutor;
import com.company.Utilities.Events.EventListener;
import com.company.Utilities.Temporals;

import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;

import static java.time.LocalTime.from;
import static java.time.LocalTime.ofSecondOfDay;

public class Time implements TimeInterface, EventListener {

    EventExecutor onOperationProgressUpdate = new EventExecutor (0, new Delegate(void.class, float.class));

    public Time(){
        onOperationProgressUpdate.RegisterListener(this);
    }

    public LocalTime differenceBetweenHours(){ return null; };

    public TemporalQuery<LocalTime> xthSecondOfDay(int seconds){ return xthSecondOfDayQuery(seconds); }

    private final TemporalQuery<LocalTime> xthSecondOfDayQuery(int seconds)
    {
        while(seconds < 0) seconds += 86400;
        while(seconds >= 86400) seconds -= 86400;
        int correctSeconds = seconds;
        return temporal -> {
            LocalTime adjustedTime = ofSecondOfDay(correctSeconds);
            return adjustedTime;
        };
    }

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