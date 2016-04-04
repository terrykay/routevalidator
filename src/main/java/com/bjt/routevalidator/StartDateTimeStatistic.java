package com.bjt.routevalidator;

import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.Track;
import com.bjt.gpxparser.TrackPoint;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Ben.Taylor on 04/04/2016.
 */
public class StartDateTimeStatistic extends StandardStatistic {
    public StartDateTimeStatistic(GeoFile geoFile) {
        super("Start Date/Time", getStartDateTime(geoFile));
    }

    private static String getStartDateTime(GeoFile geoFile) {
        final List<? extends  TrackPoint> trackPoints = GeoHelper.getAllPoints(geoFile);
        final TrackPoint firstTrackPoint = trackPoints.get(0);
        final String friendlyDateTime = reformatTime(firstTrackPoint.getTime());
        return friendlyDateTime;
    }

    public static String reformatTime(final String trackpointTime) {
        final DateTime dateTime =DateTime.parse(trackpointTime);
        final String dateTimeString = dateTime.toString("ddd dd/MM/yy HH:mm");
        return dateTimeString;
    }
}
