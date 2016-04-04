package com.bjt.routevalidator;

import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.TrackPoint;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * Created by Ben.Taylor on 04/04/2016.
 */
public class MinElevationStatistic extends StandardStatistic {
    public MinElevationStatistic(GeoFile geoFile) {
        super("Minimum elevation", getMinElevation(geoFile));
    }

    private static String getMinElevation(GeoFile geoFile) {
        final OptionalDouble min = GeoHelper.getTrackpointsAsStream(geoFile)
                .mapToDouble(TrackPoint::getElevation)
                .min();
        if(!min.isPresent()) {
            return "? m";
        } else {
            return StartElevationStatistic.formatElevation(min.getAsDouble());
        }

    }
}
