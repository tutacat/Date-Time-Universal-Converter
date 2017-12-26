package com.company.Operations;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;

public interface TimeInterface{

	default LocalTime tryGetValidTime(TemporalAccessor accessor){
		LocalTime time;
		try {
			time = LocalTime.from(accessor);
		}catch (DateTimeException e) {
			return null;
		}
		return time;
	}

    //TemporalQuery<LocalTime> timeLeftUntilXthHour(); faz literalmente o mesmo que subtractHourFromCurrentTime
    LocalTime differenceBetweenHours();
    TemporalQuery<LocalTime> xthSecondOfDay(); //que horas são no segundo especificado? entre 0 e 86399
	TemporalQuery<TemporalAccessor> addHourToCurrentTime(Temporal hourToAdd); //que horas são daqui a x horas?
	TemporalQuery<TemporalAccessor> subtractHourFromCurrentTime(Temporal hourToSubtract); //que horas eram há x horas atrás?
	//TBA? há que desenvolver mais nesta, brainstorming's not helping
}

