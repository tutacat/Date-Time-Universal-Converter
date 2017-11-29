package com.company.Operations;

import java.time.LocalTime;

interface TimeInterface{
	LocalTime timeLeftUntilXthHour();
	LocalTime addHourToCurrentTime(); //que horas são daqui a x horas?
	LocalTime subtractHourFromCurrentTime(); //que horas eram há x horas atrás?
	//TBA? há que desenvolver mais nesta, brainstorming's not helping
}