package com.bjt.routevalidator;

import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.TrackPoint;

import java.util.List;
import java.util.OptionalDouble;

/**
 * Created by Ben.Taylor on 04/04/2016.
 */
public class MaxElevationStatistic extends StandardStatistic {
    public MaxElevationStatistic(GeoFile geoFile) {
        super("Maximum elevation", getMaxElevation(geoFile));
    }

    private static String getMaxElevation(GeoFile geoFile) {
        final OptionalDouble Max = GeoHelper.getTrackpointsAsStream(geoFile)
                .mapToDouble(TrackPoint::getElevation)
                .max();
        if(!Max.isPresent()) {
            return "? m";
        } else {
            return StartElevationStatistic.formatElevation(Max.getAsDouble());
        }

    }
}