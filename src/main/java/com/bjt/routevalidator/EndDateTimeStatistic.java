package com.bjt.routevalidator;

import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.TrackPoint;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Ben.Taylor on 04/04/2016.
 */
public class EndDateTimeStatistic extends StandardStatistic {
    public EndDateTimeStatistic(GeoFile geoFile) {
        super("End Date/Time", getEndDateTime(geoFile));
    }

    private static String getEndDateTime(GeoFile geoFile) {
        final List<? extends TrackPoint> trackPoints = GeoHelper.getAllPoints(geoFile);
        final TrackPoint lastTrackPoint = trackPoints.get(trackPoints.size() - 1);
        final String friendlyDateTime = StartDateTimeStatistic.reformatTime(lastTrackPoint.getTime());
        return friendlyDateTime;
    }
}
