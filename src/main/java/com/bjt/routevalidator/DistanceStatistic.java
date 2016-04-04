package com.bjt.routevalidator;

        import com.bjt.gpxparser.GeoFile;
        import com.vividsolutions.jts.geom.Coordinate;
        import org.opengis.referencing.FactoryException;
        import org.opengis.referencing.operation.TransformException;

        import java.util.List;

/**
 * Created by Ben.Taylor on 27/03/2016.
 */
public class DistanceStatistic extends StandardStatistic {
    public DistanceStatistic(final GeoFile geoFile, final GeoHelper geoHelper) throws FactoryException, TransformException {
        super("Distance", getDistance(geoFile, geoHelper));
    }

    private static String getDistance(final GeoFile geoFile, final GeoHelper geoHelper) throws FactoryException, TransformException {
        final List<? extends Coordinate> allPoints = GeoHelper.getAllPointsAsCoordinates(geoFile);
        double distanceInMetres = geoHelper.getDistance(allPoints);
        final String distString = String.format("%,.2f km", distanceInMetres / 1000);
        return distString;
    }
}
