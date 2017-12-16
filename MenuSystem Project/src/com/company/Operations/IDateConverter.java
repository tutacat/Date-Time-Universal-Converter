package com.company.Operations;

import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalUnit;

public interface IDateConverter {
    long toMilliseconds(TemporalUnit unit, int value);
    long toSeconds(TemporalUnit unit, int value);
    long toMinutes(TemporalUnit unit, int value);
    long toHours(TemporalUnit unit, int value);

    TemporalAccessor toTimeZone(TemporalAccessor time, ZoneId currentZoneId, ZoneId newZone);
}
