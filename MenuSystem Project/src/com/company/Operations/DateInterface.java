package com.company.Operations;

import java.time.DayOfWeek;
import java.time.LocalDate;

public interface DateInterface{

    void SetCurrentLocalDate(LocalDate date);
    LocalDate getCurrentLocalDate();

	DayOfWeek firstWeekDayOfXthYear(); //1º dia da semana de um dado ano
	int xthDayOfXthYear();
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