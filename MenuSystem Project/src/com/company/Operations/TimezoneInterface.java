package com.company.Operations;

import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalQuery;

public interface TimezoneInterface{
	TemporalQuery <ZonedDateTime> currentHourInTimezone();
	TemporalQuery <ZonedDateTime> differenceBetweenZones(Temporal idOne, Temporal idTwo); //dados dois ZoneIds, e a hora no primeiro indica a hora no segundo
}