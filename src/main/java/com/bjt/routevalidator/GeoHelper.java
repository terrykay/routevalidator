package com.bjt.routevalidator;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.util.List;

/**
 * Created by Ben.Taylor on 26/10/2015.
 */
public class GeoHelper {
    private static final int WGS84_SRID = 4326;
    private final GeometryFactory geometryFactory;

    public GeoHelper() {
        geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FIXED), WGS84_SRID);
    }

    public double lineToPoint(final List<? extends  Coordinate> line, final Coordinate point) throws FactoryException, TransformException {
        final LineString geometryLine = geometryFactory.createLineString(line.toArray(new Coordinate[]{}));
        final Coordinate centroidOfLine =getCentroidOfLine(line);
        final String crsString = String.format("AUTO:42001,%f,%f", centroidOfLine.x, centroidOfLine.y);
        final CoordinateReferenceSystem crs = CRS.decode(crsString);
        final MathTransform mathTransform = CRS.findMathTransform(DefaultGeographicCRS.WGS84, crs); //gets it to metres
        final Point geometryPoint = geometryFactory.createPoint(point);
        final Geometry lineTransformed = JTS.transform(geometryLine, mathTransform);
        final Geometry pointTransformed = JTS.transform(geometryPoint, mathTransform);
        return lineTransformed.distance(pointTransformed);
    }

    private static final Coordinate getCentroidOfLine(final List<? extends Coordinate> line) {
        double totalX = 0, totalY = 0;
        for(final Coordinate coord : line) {
            totalX += coord.x;
            totalY += coord.y;
        }
        final double aveX = totalX / line.size();
        final double aveY = totalY / line.size();
        return new Coordinate(aveX, aveY);
    }
}
