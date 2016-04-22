package com.bjt.routevalidator;

import org.joda.time.Period;

/**
 * Created by Ben.Taylor on 21/04/2016.
 */
public class OverallTimeStatistic extends StandardStatistic {
    public OverallTimeStatistic(final TrackSummary trackSummary) {
        super("Time at Rest", getStatistic(trackSummary));
    }

    private static String getStatistic(TrackSummary trackSummary) {
        final int seconds = (int) trackSummary.getTotalTimeAtRest();
        final Period period = Period.seconds(seconds);
        final String stat = DurationStatistic.getDurationString(period);
        return stat;
    }
}
