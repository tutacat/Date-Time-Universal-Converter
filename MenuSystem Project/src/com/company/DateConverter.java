package com.company;

import com.company.Operations.IDateConverter;

import java.time.temporal.TemporalUnit;

public class DateConverter implements IDateConverter {

    @Override
    public long toMilliseconds(TemporalUnit unit, int value) {
        return unit.getDuration().toMillis() * value;
    }

    @Override
    public long toSeconds(TemporalUnit unit, int value) {
        return (unit.getDuration().toMinutes() * 60) * value;
    }

    @Override
    public long toMinutes(TemporalUnit unit, int value) {
        return unit.getDuration().toMinutes() * value;
    }

    @Override
    public long toHours(TemporalUnit unit, int value) {
        return unit.getDuration().toHours() * value;
    }
}
