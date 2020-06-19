package com.company;

import com.company.Operations.TimezoneInterface;
import com.company.Utilities.Events.*;

import java.time.*;
import java.time.temporal.*;

public class TimeZone implements TimezoneInterface, EventListener {

    EventExecutor onOperationProgressUpdate = new EventExecutor (0, new Delegate(void.class, float.class));

    public TimeZone(){
        onOperationProgressUpdate.RegisterListener(this);
    }

    private final TemporalQuery <ZonedDateTime> currentHourInTimezoneQuery =
            (temporal) -> {
                ZoneId idQuery = ZoneId.from(temporal);
                ZoneId systemZoneId = ZoneId.systemDefault();
                LocalDateTime ldt = LocalDateTime.now();
                ZonedDateTime zdtHere = ZonedDateTime.of(ldt, systemZoneId);
                return zdtHere.withZoneSameInstant(idQuery);
            };

    @Override
    public TemporalQuery <ZonedDateTime> currentHourInTimezone() {return currentHourInTimezoneQuery;}

    private final TemporalQuery <ZonedDateTime> differenceBetweenZonesQuery (Temporal idOne, Temporal idTwo) {
        return (temporal) -> {
            LocalDateTime timeOne = LocalDateTime.from(temporal);
            ZoneId zoneOne = ZoneId.from(idOne);
            ZoneId zoneTwo = ZoneId.from(idTwo);
            ZonedDateTime zdtOne = ZonedDateTime.of(timeOne, zoneOne);
            return zdtOne.withZoneSameInstant(zoneTwo);
        };
    }

    @Override
    public TemporalQuery <ZonedDateTime> differenceBetweenZones(Temporal idOne, Temporal idTwo) {return differenceBetweenZonesQuery(idOne, idTwo);}
}
