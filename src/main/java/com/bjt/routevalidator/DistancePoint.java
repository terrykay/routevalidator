package com.bjt.routevalidator;

/**
 * Created by Ben.Taylor on 23/04/2016.
 */
public class DistancePoint {
    private String dist;
    private double lat;
    private double lng;

    public DistancePoint(final TrackpointWrapper trackpointWrapper) {
        dist = String.format( "%.1f", trackpointWrapper.getDistanceCumulative()) + " km";
        lat = trackpointWrapper.getTrackpoint().getLat();
        lng = trackpointWrapper.getTrackpoint().getLon();
    }
}
