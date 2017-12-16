package com.company.Operations;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;

import static java.time.Month.JANUARY;

public interface DateInterface{

    default LocalDate tryGetValidDate(TemporalAccessor accessor){
        LocalDate date;
        try {
            date = LocalDate.from(accessor);
        }catch (DateTimeException e) {
            return null;
        }
        return date;
    }

    static LocalDate beginningOfYear(int year){
        return LocalDate.of(year, JANUARY, 1);
    }

    TemporalQuery<DayOfWeek> firstWeekDayOfXthYear();//1º dia da semana de um dado ano
    TemporalQuery<Integer> xthDayOfXthYear();
    TemporalQuery<DayOfWeek> xthDayOfXthWeek();
    TemporalQuery<Integer> daysLeftUntilXthDay(); //de hoje até ao dia x. pode ser interpretado como "dias até ao prazo"
    TemporalQuery<Integer> workDaysUntilDate();
	TemporalQuery<Integer> weekendDaysUntilDate();
	LocalDate checkHoliday(); // arrayList com feriados nacionais?
    TemporalQuery<TemporalAccessor> addDateToCurrentDate(TemporalAccessor dateToAdd);
    TemporalQuery<TemporalAccessor> subtractDateFromCurrentDate(TemporalAccessor dateToAdd);
	LocalDate differenceBetweenDates();
	//TBA? podemos fazer o ano que tem menos dias úteis, por exemplo
}