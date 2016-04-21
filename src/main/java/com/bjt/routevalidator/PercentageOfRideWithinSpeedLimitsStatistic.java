package com.bjt.routevalidator;

import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.TrackPoint;
import org.joda.time.Duration;
import org.joda.time.Seconds;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bjt.routevalidator.TerrainType.*;

/**
 * Created by Ben.Taylor on 19/04/2016.
 */
public class PercentageOfRideWithinSpeedLimitsStatistic extends StandardStatistic{
    public PercentageOfRideWithinSpeedLimitsStatistic(GeoFile geoFile) throws FactoryException, TransformException {
        super("Percentage of ride within speed limits", getPercentage(geoFile, "%.1f"));
    }

    public static String getPercentage(final GeoFile geoFile, final String format) throws FactoryException, TransformException {
        final TrackSummary trackSummary = TrackSummary.AnalyzeTrack(geoFile);
        trackSummary.AddMaxSpeedToTrackPoints();
        trackSummary.AddSpeed();
        final double proportionOk = trackSummary.getProportionOk();
        final String statString = String.format(format, proportionOk * 100) + "%";
        return statString;
    }



}
