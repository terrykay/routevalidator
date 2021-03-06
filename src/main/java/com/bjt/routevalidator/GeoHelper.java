package com.bjt.routevalidator;

import com.bjt.gpxparser.*;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.joda.time.DateTime;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        final MathTransform mathTransform = getMathTransform(line);
        final Point geometryPoint = geometryFactory.createPoint(point);
        final Geometry lineTransformed = JTS.transform(geometryLine, mathTransform);
        final Geometry pointTransformed = JTS.transform(geometryPoint, mathTransform);
        return lineTransformed.distance(pointTransformed);
    }

    private MathTransform getMathTransform(List<? extends Coordinate> line) throws FactoryException {
        final Coordinate centroidOfLine =getCentroidOfLine(line);
        final String crsString = String.format("AUTO:42001,%f,%f", centroidOfLine.x, centroidOfLine.y);
        final CoordinateReferenceSystem crs = CRS.decode(crsString);
        return CRS.findMathTransform(DefaultGeographicCRS.WGS84, crs);
    }

    public Geometry getEnvelope(final List<? extends Coordinate> coordinates) {
        final Coordinate[] coordinateArray = coordinates.toArray(new Coordinate[]{});
        final MultiPoint multiPoint = geometryFactory.createMultiPoint(coordinateArray);
        return multiPoint.getEnvelope();
    }

    public double getDistance(final Track track) throws FactoryException, TransformException {
        final List<Coordinate> coords = getCoords(track);
        return getDistance(coords);
    }

    public double getDistanceTrackpoints(List<? extends TrackPoint> trackPoints) throws FactoryException, TransformException {
        final List<Coordinate> coords = trackPoints.stream().map(o -> new Coordinate(o.getLon(), o.getLat())).collect(Collectors.toList());
        final double distance = getDistance(coords);
        return distance;
    }

    public double getDistance(List<? extends Coordinate> coords) throws FactoryException, TransformException {
        final MathTransform mathTransform = getMathTransform(coords);
        final Geometry lineString = JTS.transform(geometryFactory.createLineString(coords.toArray(new Coordinate[]{})), mathTransform);
        double length = lineString.getLength();
        return length;
    }

    public static List<Coordinate> getCoords(final Track track) {
        final List<Coordinate> coordinates = track.getTrackSegments().stream()
                .flatMap(ts -> ts.getTrackPoints().stream())
                .map(o -> new Coordinate(o.getLon(), o.getLat()))
                .collect(Collectors.toList());
        return coordinates;
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

    public static Stream<? extends TrackPoint> getTrackpointsAsStream(GeoFile geoFile) {
        return geoFile.getTracks().stream()
                .map(o -> o.getTrackSegments()).flatMap(List::stream)
                .map(o -> o.getTrackPoints()).flatMap(List::stream);
    }

    public static List<? extends Coordinate> getAllPointsAsCoordinates(final GeoFile geoFile) {
        final List<? extends Coordinate> coords = getTrackpointsAsStream(geoFile)
                .map(o -> new Coordinate(o.getLon(), o.getLat()))
                .collect(Collectors.toList());
        return coords;
    }

    public static List<? extends TrackPoint> getAllPoints(final GeoFile geoFile) {
        final Stream<? extends TrackPoint> trackpointsAsStream = getTrackpointsAsStream(geoFile);

        List<? extends TrackPoint> trackPoints;
        if(getTrackpointsAsStream(geoFile).allMatch(t -> t.getTime() != null && !t.getTime().isEmpty())) {
            trackPoints = getTrackpointsAsStream(geoFile)
                    .sorted((o1, o2) -> DateTime.parse(o1.getTime()).compareTo(DateTime.parse(o2.getTime())))
                    .collect(Collectors.toList());
        } else {
            trackPoints = getTrackpointsAsStream(geoFile)
                    .collect(Collectors.toList());
        }
        return trackPoints;
    }
}
