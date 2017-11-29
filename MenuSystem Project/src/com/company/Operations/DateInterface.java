package com.company.Operations;

import java.time.LocalDate;

interface DateInterface{
	LocalDate firstWeekDayOfXthYear(); //1º dia da semana de um dado ano
	LocalDate xthDayOfXthYear();
	LocalDate xthDayOfXthWeek();
	LocalDate daysLeftUntilXthDay(); //de hoje até ao dia x. pode ser interpretado como "dias até ao prazo"
	LocalDate workDaysUntilDate();
	LocalDate weekendDaysUntilDate();
	LocalDate checkHoliday(); // arrayList com feriados nacionais?
	LocalDate addDateToCurrentDate();
	LocalDate subtractDateFromCurrentDate();
	LocalDate differenceBetweenDates();
	//TBA? podemos fazer o ano que tem menos dias úteis, por exemplo
}