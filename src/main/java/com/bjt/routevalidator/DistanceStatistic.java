package com.bjt.routevalidator;

import com.vividsolutions.jts.geom.Coordinate;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import java.util.List;

/**
 * Created by Ben.Taylor on 27/03/2016.
 */
public class DistanceStatistic extends StandardStatistic {
    public DistanceStatistic(final List<List<Coordinate>> intendedPaths, final GeoHelper geoHelper) throws FactoryException, TransformException {
        super("Distance", getDistance(intendedPaths, geoHelper));
    }

    private static String getDistance(List<List<Coordinate>> intendedPaths, final GeoHelper geoHelper) throws FactoryException, TransformException {
        double distance = 0;
        for(final List<Coordinate> path : intendedPaths) {
            distance += geoHelper.getDistance(path);
        }
        final String distString = String.format("%.2f km", distance);
        return distString;
    }
}
