package com.bjt.routevalidator;

/**
 * Created by Ben.Taylor on 28/04/2016.
 */
public class TotalClimbStatistic extends StandardStatistic {
    public TotalClimbStatistic(TrackSummary trackSummary) {
        super("Total recorded climb", getTotalClimb(trackSummary));
    }

    public static String getTotalClimb(TrackSummary trackSummary) {
        final String stat = String.format("%,.0f", trackSummary.getTotalClimb()) + " m";
        return stat;
    }
}
