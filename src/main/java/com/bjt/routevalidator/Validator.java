package com.bjt.routevalidator;

import com.bjt.gpxparser.Gpx;
import com.bjt.gpxparser.Track;
import com.bjt.gpxparser.TrackPoint;
import com.bjt.gpxparser.TrackSegment;
import com.vividsolutions.jts.geom.Coordinate;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben.Taylor on 25/10/2015.
 */
public class Validator {
    private final GeoHelper geoHelper;

    public Validator() {
        geoHelper = new GeoHelper();
    }

    public Result validate(Gpx intendedGpx, Gpx actualGpx, int tolerance) throws FactoryException, TransformException {
        final Result result = new Result();
        result.setTolerance(tolerance);

        //intended:
        final List<Coordinate> controls = getAllPoints(intendedGpx);
        final List<List<Coordinate>> pathsRidden = getAllLines(actualGpx);

        final List<List<TrackPoint>> referralAreas = new ArrayList<>();
        List<TrackPoint> currentReferralArea = null;
        for(final Coordinate control : controls) {
            final Double dist = getMinDistance(control, pathsRidden);
            if(dist > tolerance) {
                if(currentReferralArea == null) {
                    currentReferralArea = new ArrayList<>();
                    referralAreas.add(currentReferralArea);
                }
                final TrackPoint referralPoint = new TrackPoint(control.y, control.x);
                currentReferralArea.add(referralPoint);
            } else {
                currentReferralArea = null;
            }
        }

        if(referralAreas.isEmpty()) {
            result.setStatus(Result.STATUS_ACCEPT);
        } else {
            result.setStatus(Result.STATUS_REFER);
        }
        return result;
    }

    private static List<Coordinate> getAllPoints(final Gpx gpx) {
        final List<Coordinate> points = new ArrayList<>();
        for(final Track track: gpx.getTracks()) {
            for(final TrackSegment trackSeg : track.getTrackSegments()) {
                for(final TrackPoint trackPoint : trackSeg.getTrackPoints()) {
                    final Coordinate coordinate = new Coordinate(trackPoint.getLon(), trackPoint.getLat());
                    points.add(coordinate);
                }
            }
        }
        return points;
    }

    private static List<List<Coordinate>> getAllLines(final Gpx gpx) {
        final List<List<Coordinate>> lines =  new ArrayList<>();
        for(final Track track: gpx.getTracks()) {
            for(final TrackSegment trackSegment : track.getTrackSegments()) {
                final List<Coordinate> line = new ArrayList<>();
                for(final TrackPoint trackPoint : trackSegment.getTrackPoints()) {
                    final Coordinate coordinate = new Coordinate(trackPoint.getLon(), trackPoint.getLat());
                    line.add(coordinate);
                }
                lines.add(line);
            }
        }
        return lines;
    }

    private Double getMinDistance(final Coordinate point,  final List<List<Coordinate>> lines) throws FactoryException, TransformException {
        Double minDistance = null;
        for(final List<Coordinate> line : lines) {
            final double dist = geoHelper.lineToPoint(line, point);
            if(minDistance == null || dist < minDistance) minDistance = dist;
        }
        return minDistance;
    }
}
