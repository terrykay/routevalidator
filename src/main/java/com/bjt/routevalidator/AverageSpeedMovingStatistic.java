package com.bjt.routevalidator;

/**
 * Created by Ben.Taylor on 21/04/2016.
 */
public class AverageSpeedMovingStatistic extends StandardStatistic {
    public AverageSpeedMovingStatistic(final TrackSummary trackSummary) {
        super("Average Speed - Moving", getStatistic(trackSummary));

    }

    private static String getStatistic(TrackSummary trackSummary) {
        final double averageSpeedMoving = trackSummary.MovingAverageSpeed();
        final String stat = String.format("%.1f", averageSpeedMoving);
        return stat;
    }
}
