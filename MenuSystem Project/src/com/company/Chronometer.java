package com.company;

import com.company.Operations.IChronometer;
import com.company.Utilities.Colorfull_Console.ColorfulConsole;
import com.company.Utilities.Events.Delegate;
import com.company.Utilities.Events.EventExecutor;
import com.company.Utilities.Events.EventListener;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.PriorityQueue;
import java.util.Queue;

import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Blue;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Green;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.Bold;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.Regular;

public class Chronometer implements IChronometer, EventListener {

    private boolean stopped = true;

    public void setState(){
        stopped = !stopped;
        onChronometerStateChanged.Invoke (stopped);
    }

    public boolean isStopped() {
        return stopped;
    }

    public Instant start;
    public Duration lap;
    public Instant stop;

    public Queue<Duration> laps;

    private final int LapCapacity = 10;

    public Chronometer(){
        laps = new PriorityQueue <> (LapCapacity);
        onChronometerStateChanged.RegisterListener (this);
    }

    @Override
    public void start() {
        if(!isStopped ())
            return;
        start = Instant.now ();
        setState ();
    }

    @Override
    public long stop() {
        if(isStopped ())
            return 0;
        stop = Instant.now ();
        setState ();
        return start.until (stop, ChronoUnit.SECONDS);
    }

    @Override
    public void lap() {
        if(isStopped ()) {
            return;
        }

        lap = Duration.between (start, Instant.now ());
        if(laps.size () >= LapCapacity){
            laps.remove ();
        }
        laps.offer (lap);
    }

    @Override
    public long getMillis() {
        Duration between = Duration.between (start, Instant.now ());
        return between.toMillis ();
    }

    @Override
    public long getSeconds() {
        Duration between = Duration.between (start, Instant.now ());
        return between.getSeconds ();
    }

    @Override
    public long getMinutes() {
        Duration between = Duration.between (start, Instant.now ());
        return between.toMinutes ();
    }

    @Override
    public long getHours() {
        Duration between = Duration.between (start, Instant.now ());
        return between.toHours ();
    }

    @Override
    public void Print() {
        final int[] i = {0};
        laps.forEach ((duration) -> {
            i[0]++;
            String formatted = String.format ("{0}Lap %d -> {1}%s{0}H {1}%s{0}Min {1}%s{0}S {1}%s{0}Millis",
                    i[0], duration.toHours (),
                    duration.toMinutes (), duration.getSeconds (), duration.toMillis ());
            ColorfulConsole.WriteLineFormatted (formatted, Blue (Regular), Green (Bold));
        });
    }

    public EventExecutor onChronometerStateChanged
            = new EventExecutor (0, new Delegate (void.class, boolean.class));
}
