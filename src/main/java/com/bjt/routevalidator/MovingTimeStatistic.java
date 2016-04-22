package com.bjt.routevalidator;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.Seconds;

/**
 * Created by Ben.Taylor on 21/04/2016.
 */
public class MovingTimeStatistic extends StandardStatistic {
    public MovingTimeStatistic(final TrackSummary trackSummary) {
        super("Moving Time", getStatistic(trackSummary));
    }

    private static String getStatistic(final TrackSummary trackSummary) {
        final int seconds = (int) trackSummary.getTotalTimeMoving();
        final Period period = Period.seconds(seconds);
        final String stat = DurationStatistic.getDurationString(period);
        return stat;
    }

}
