package com.bjt.routevalidator;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

/**
 * Created by Ben.Taylor on 23/04/2016.
 */
public class DistancePoint {
    private String label;
    private double lat;
    private double lng;

    public DistancePoint(final TrackpointWrapper trackpointWrapper) {
        final DateTime dateTime = trackpointWrapper.getDateTime();
        label = String.format( "%.1f km @ %s (%s)",
                trackpointWrapper.getDistanceCumulative(),
                formatDateTime(dateTime),
                DurationStatistic.getDurationString(trackpointWrapper.getTimeCumulative().toPeriod())
        );
        lat = trackpointWrapper.getTrackpoint().getLat();
        lng = trackpointWrapper.getTrackpoint().getLon();
    }

    public static String formatDateTime(final DateTime dateTime) {
        final DateTime localDateTime = dateTime.withZone(DateTimeZone.getDefault());
        return localDateTime.toString("h:mm a").toLowerCase();
    }
}
