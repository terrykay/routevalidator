package com.bjt.routevalidator;

import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.TrackPoint;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Ben.Taylor on 04/04/2016.
 */
public class StartElevationStatistic extends StandardStatistic {
    public StartElevationStatistic(GeoFile geoFile) {
        super("Start elevation", getStartElevation(geoFile));
    }

    private static String getStartElevation(GeoFile geoFile) {
        final List<? extends  TrackPoint> trackPoints = GeoHelper.getAllPoints(geoFile);
        final TrackPoint firstTrackPoint = trackPoints.get(0);
        final String elevationString = formatElevation(firstTrackPoint.getElevation());
        return elevationString;
    }

    public static String formatElevation(final Double elevation) {
        final String elevationString = String.format("%,.0f m", elevation);
        return elevationString;
    }
}
