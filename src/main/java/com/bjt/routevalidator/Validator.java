package com.bjt.routevalidator;

import com.bjt.gpxparser.*;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Ben.Taylor on 25/10/2015.
 */
public class Validator {
    private final GeoHelper geoHelper;

    public Validator() {
        geoHelper = new GeoHelper();
    }


    public Result validate(GpxFile intendedGpx, GpxFile actualGpx, int tolerance, List<? extends TrackUsePreference> trackUsePreferences) throws FactoryException, TransformException {

        final Result result = new Result(intendedGpx, actualGpx, tolerance, trackUsePreferences);

        //intended:
        final List<Coordinate> controls = getAllPoints(intendedGpx.getGpx());
        final List<List<Coordinate>> pathsRidden = getAllLines(actualGpx.getGpx(), trackUsePreferences);

        final List<List<Coordinate>> referralAreas = new ArrayList<>();
        List<Coordinate> currentReferralArea = null;
        int counter = 0;
        for(final Coordinate control : controls) {
            //if((int)(counter / 10) %5 == 0) {
            final Double dist = getMinDistance(control, pathsRidden);
            if(dist == null || dist > tolerance) {
                if(currentReferralArea == null) {
                    currentReferralArea = new ArrayList<>();
                    referralAreas.add(currentReferralArea);
                }
                currentReferralArea.add((Coordinate) control.clone());
            } else {
                currentReferralArea = null;
            }
        }

        if(referralAreas.isEmpty()) {
            result.setStatus(Result.STATUS_ACCEPT);
        } else {
            result.setStatus(Result.STATUS_REFER);
        }

        final List<String> renderedReferralAreas = renderReferralAreas(referralAreas);
        result.setReferralAreas(renderedReferralAreas);

        for(final Track track : actualGpx.getGpx().getTracks()) {
            Logger.getAnonymousLogger().info("Track " + track.getName() + " is of length: " + geoHelper.getDistance(track));
        }

        return result;
    }

    private List<String> renderReferralAreas(final List<List<Coordinate>> referralAreas) {
        final List<String> renderedStrings = new ArrayList<>();
        for(final List<Coordinate> referralArea : referralAreas) {
            final Geometry envelope = geoHelper.getEnvelope(referralArea);
            final List<String> latLongs = new ArrayList<>();
            for(final Coordinate coordinate : envelope.getCoordinates()) {
                latLongs.add(String.format("[%.4f,%.4f]", coordinate.y, coordinate.x));
            }
            final String referralAreaString = "[" + String.join(",", latLongs) + "]";
            renderedStrings.add(referralAreaString);
        }
        return renderedStrings;
    }

    private static List<Coordinate> getAllPoints(final GeoFile gpx) {
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

    private static List<List<Coordinate>> getAllLines(final GeoFile gpx, List<? extends TrackUsePreference> trackUsePreferences) {
        final List<List<Coordinate>> lines =  new ArrayList<>();
        for(final Track track: gpx.getTracks()) {
            if(trackUsed(track, trackUsePreferences)) {
                for (final TrackSegment trackSegment : track.getTrackSegments()) {
                    final List<Coordinate> line = new ArrayList<>();
                    for (final TrackPoint trackPoint : trackSegment.getTrackPoints()) {
                        final Coordinate coordinate = new Coordinate(trackPoint.getLon(), trackPoint.getLat());
                        line.add(coordinate);
                    }
                    lines.add(line);
                }
            } else {
                Logger.getAnonymousLogger().info(String.format("Track %s not used!", track.getName()));
            }
        }
        return lines;
    }

    private static boolean trackUsed(final Track track, List<? extends TrackUsePreference> trackUsePreferences ) {
        boolean val = false;
        for(final TrackUsePreference trackUsePreference : trackUsePreferences) {
            boolean equals = trackUsePreference.getTrackName().equals(track.getName());
            Logger.getAnonymousLogger().info(String.format("getTrackName = %s, track.getName = %s, used = %b", trackUsePreference.getTrackName(), track.getName(), equals));
            if(equals) {
                val |= trackUsePreference.isRender();
            }
        }
        return val;
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
