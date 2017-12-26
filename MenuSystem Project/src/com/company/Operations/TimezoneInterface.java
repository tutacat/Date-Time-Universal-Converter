package com.company.Operations;

import java.time.ZonedDateTime;

interface TimezoneInterface{
	ZonedDateTime currentHourInTimezone();
	ZonedDateTime convertTimezones(); //duas timezones dadas? p. ex, viagem dos EUA para Itália, quanto demora?
	Instant differenceBetweenZones(temporal idOne, temporal idTwo); //dados dois ZoneIds, e a hora no primeiro indica a hora no segundo
	//TBA?
}