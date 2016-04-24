package com.bjt.routevalidator;

/**
 * Created by Ben.Taylor on 23/04/2016.
 */
public class DistancePoint {
    private String label;
    private double lat;
    private double lng;

    public DistancePoint(final TrackpointWrapper trackpointWrapper) {
        label = String.format( "%.1f km\n%s (%s)",
                trackpointWrapper.getDistanceCumulative(),
                trackpointWrapper.getDateTime().toString("h:mm a").toLowerCase(),
                DurationStatistic.getDurationString(trackpointWrapper.getTimeCumulative().toPeriod())
        );
        lat = trackpointWrapper.getTrackpoint().getLat();
        lng = trackpointWrapper.getTrackpoint().getLon();
    }
}
