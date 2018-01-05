package com.company;

import com.company.Operations.TimezoneInterface;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalQuery;
/* IntellIJ is auto-removing these because they're not used
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalAdjusters;
import static java.lang.System.out;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.averagingDouble;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.summingLong;
import static java.util.stream.Collectors.summarizingDouble;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.counting;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.naturalOrder;
import static java.util.Arrays.stream;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.Month.JANUARY;
import static java.time.Month.FEBRUARY;
import static java.time.Month.MARCH;
import static java.time.Month.APRIL;
import static java.time.Month.MAY;
import static java.time.Month.JUNE;
import static java.time.Month.JULY;
import static java.time.Month.AUGUST;
import static java.time.Month.SEPTEMBER;
import static java.time.Month.OCTOBER;
import static java.time.Month.NOVEMBER;
import static java.time.Month.DECEMBER;
import static java.time.temporal.ChronoUnit.MILLENNIA;
import static java.time.temporal.ChronoUnit.CENTURIES;
import static java.time.temporal.ChronoUnit.DECADES;
import static java.time.temporal.ChronoUnit.YEARS;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HALF_DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.MICROS;
import static java.time.temporal.ChronoUnit.NANOS;
import static java.time.temporal.IsoFields.QUARTER_OF_YEAR;
import static java.time.temporal.TemporalQueries.chronology;
import static java.time.temporal.TemporalQueries.localDate;
import static java.time.temporal.TemporalQueries.localTime;
import static java.time.temporal.TemporalQueries.offset;
import static java.time.temporal.TemporalQueries.precision;
import static java.time.temporal.TemporalQueries.zone;
import static java.time.temporal.TemporalQueries.zoneId;
 */


public class TimeZone implements TimezoneInterface {

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