package com.bjt.routevalidator;

import org.joda.time.Duration;
import org.joda.time.Period;

/**
 * Created by Ben.Taylor on 21/04/2016.
 */
public class TimeAtRestStatistic extends StandardStatistic {
    public TimeAtRestStatistic(final TrackSummary trackSummary) {
        super("Time at Rest", getStatistic(trackSummary));
    }

    public static String getStatistic(TrackSummary trackSummary) {
        final int seconds = (int) trackSummary.getTotalTimeAtRest();
        final Period period = Duration.standardSeconds(seconds).toPeriod();
        final String stat = DurationStatistic.getDurationString(period);
        return stat;
    }
}
