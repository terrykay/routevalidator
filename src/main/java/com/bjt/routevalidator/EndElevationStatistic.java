package com.bjt.routevalidator;

import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.TrackPoint;

import java.util.List;

/**
 * Created by Ben.Taylor on 04/04/2016.
 */
public class EndElevationStatistic extends StandardStatistic {
    public EndElevationStatistic(GeoFile geoFile) {
        super("End elevation", getEndElevation(geoFile));
    }

    private static String getEndElevation(GeoFile geoFile) {
        final List<? extends  TrackPoint> trackPoints = GeoHelper.getAllPoints(geoFile);
        final TrackPoint firstTrackPoint = trackPoints.get(trackPoints.size() - 1);
        final String elevationString = StartElevationStatistic.formatElevation(firstTrackPoint.getElevation());
        return elevationString;
    }

}
