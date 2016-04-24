package com.bjt.routevalidator;

import com.bjt.gpxparser.*;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Ben.Taylor on 25/10/2015.
 */
public class Validator {
    private final GeoHelper geoHelper;
    private final ServletContext servletContext;

    public Validator(ServletContext servletContext) {
        this.servletContext = servletContext;
        geoHelper = new GeoHelper();
    }


    public Result validate(GpxFile intendedGpx, GpxFile actualGpx, int tolerance, List<? extends TrackUsePreference> trackUsePreferences) throws FactoryException, TransformException, IOException, FriendlyException {

        final StatusResult actualCheck = Actualness.check(actualGpx.getGpx());
        if (!actualCheck.isSuccess()) {
            throw new FriendlyException("The 'actual' file does not look like it was generated by a device.\r\n" + actualCheck.getError());
        }

        final List<String> trackUsePreferenceNames = Arrays.asList(trackUsePreferences.stream().filter(o -> o.isRender()).map(o -> o.getTrackName()).toArray(value -> new String[value]));
        actualGpx.getGpx().pruneTracks(trackUsePreferenceNames);

        //intended:
        final List<? extends Coordinate> controls = GeoHelper.getAllPointsAsCoordinates(intendedGpx.getGpx());
        //actual:
        final List<List<Coordinate>> pathsRidden = getAllLines(actualGpx.getGpx());

        final List<? extends Statistic> intendedStatistics = getIntendedStatistics(intendedGpx.getGpx());
        final TrackSummary trackSummary = TrackSummary.AnalyzeTrack(actualGpx.getGpx());
        final List<? extends Statistic> actualStatistics = getActualStatistics(actualGpx.getGpx(), trackSummary);

        final Result result = new Result(intendedGpx, actualGpx, tolerance, trackUsePreferences, intendedStatistics, actualStatistics, trackSummary);

        final List<List<Coordinate>> referralAreas = new ArrayList<>();
        List<Coordinate> currentReferralArea = null;
        int counter = 0;
        for (final Coordinate control : controls) {
            //if((int)(counter / 10) %5 == 0) {
            final Double dist = getMinDistance(control, pathsRidden);
            if (dist == null || dist > tolerance) {
                if (currentReferralArea == null) {
                    currentReferralArea = new ArrayList<>();
                    referralAreas.add(currentReferralArea);
                }
                currentReferralArea.add((Coordinate) control.clone());
            } else {
                currentReferralArea = null;
            }
        }

        if (referralAreas.isEmpty()) {
            result.setStatus(Result.STATUS_ACCEPT);
        } else {
            result.setStatus(Result.STATUS_REFER);
        }

        final List<String> renderedReferralAreas = renderReferralAreas(referralAreas);
        result.setReferralAreas(renderedReferralAreas);

        for (final Track track : actualGpx.getGpx().getTracks()) {
            Logger.getAnonymousLogger().info("Track " + track.getName() + " is of length: " + geoHelper.getDistance(track));
        }

        return result;
    }

    private List<? extends Statistic> getActualStatistics(final GeoFile geoFile, final TrackSummary trackSummary) throws FactoryException, TransformException {
        if (geoFile.getTracks().size() > 0)
            return Arrays.asList(
                    new DistanceStatistic(geoFile, geoHelper),
                    new DurationStatistic(geoFile),
                    new StartDateTimeStatistic(geoFile),
                    new EndDateTimeStatistic(geoFile),
                    new StartElevationStatistic(geoFile),
                    new EndElevationStatistic(geoFile),
                    new MinElevationStatistic(geoFile),
                    new MaxElevationStatistic(geoFile),
                    new TotalTrackpointsStatistic(geoFile),
                    new PercentageOfRideWithinSpeedLimitsStatistic(trackSummary),
                    new AverageSpeedOverallStatistic(trackSummary),
                    new AverageSpeedMovingStatistic(trackSummary),
                    new MovingTimeStatistic(trackSummary),
                    new TimeAtRestStatistic(trackSummary)
            );
        else return new ArrayList<>();
    }

    private List<? extends Statistic> getIntendedStatistics(final GeoFile geoFile) throws FactoryException, TransformException, IOException {
        return Arrays.asList(
                new DistanceStatistic(geoFile, geoHelper),
                new ClimbingStatistic(geoFile, servletContext),
                new DoubleWidthCommentStatistic("(Contour counting)")
        );
    }

    private List<String> renderReferralAreas(final List<List<Coordinate>> referralAreas) {
        final List<String> renderedStrings = new ArrayList<>();
        for (final List<Coordinate> referralArea : referralAreas) {
            final Geometry envelope = geoHelper.getEnvelope(referralArea);
            final List<String> latLongs = new ArrayList<>();
            for (final Coordinate coordinate : envelope.getCoordinates()) {
                latLongs.add(String.format("[%.4f,%.4f]", coordinate.y, coordinate.x));
            }
            final String referralAreaString = "[" + String.join(",", latLongs) + "]";
            renderedStrings.add(referralAreaString);
        }
        return renderedStrings;
    }


    private static List<List<Coordinate>> getAllLines(final GeoFile gpx) {
        final List<List<Coordinate>> lines = new ArrayList<>();
        for (final Track track : gpx.getTracks()) {
            for (final TrackSegment trackSegment : track.getTrackSegments()) {
                final List<Coordinate> line = new ArrayList<>();
                for (final TrackPoint trackPoint : trackSegment.getTrackPoints()) {
                    final Coordinate coordinate = new Coordinate(trackPoint.getLon(), trackPoint.getLat());
                    line.add(coordinate);
                }
                lines.add(line);
            }
        }
        return lines;
    }


    private Double getMinDistance(final Coordinate point, final List<List<Coordinate>> lines) throws FactoryException, TransformException {
        Double minDistance = null;
        for (final List<Coordinate> line : lines) {
            final double dist = geoHelper.lineToPoint(line, point);
            if (minDistance == null || dist < minDistance) minDistance = dist;
        }
        return minDistance;
    }
}
