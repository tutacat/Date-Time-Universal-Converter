package com.company.Operations;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalQuery;

public interface TimezoneInterface{
	TemporalQuery <LocalDateTime> currentHourInTimezone();
	ZonedDateTime convertTimezones(); //duas timezones dadas? p. ex, viagem dos EUA para Itália, quanto demora?
	//TBA?
}