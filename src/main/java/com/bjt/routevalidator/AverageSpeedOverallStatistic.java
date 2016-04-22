package com.bjt.routevalidator;

/**
 * Created by Ben.Taylor on 21/04/2016.
 */
public class AverageSpeedOverallStatistic extends  StandardStatistic {
    public AverageSpeedOverallStatistic(final TrackSummary trackSummary) {
        super("Average Speed - Overall", getStatistic(trackSummary));
    }

    private static String getStatistic(final TrackSummary trackSummary) {
        final double overallAverageSpeed = trackSummary.OverallAverageSpeed();
        final String stat = String.format("%.1f", overallAverageSpeed) + " km/h";
        return stat;
    }
}
