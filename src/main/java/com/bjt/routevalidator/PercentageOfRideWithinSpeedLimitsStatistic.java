package com.bjt.routevalidator;

import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.TrackPoint;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Ben.Taylor on 19/04/2016.
 */
public class PercentageOfRideWithinSpeedLimitsStatistic extends StandardStatistic{
    public PercentageOfRideWithinSpeedLimitsStatistic(GeoFile geoFile) throws FactoryException, TransformException {
        super("Percentage of ride within speed limits", getPercentage(geoFile));
    }

    private static String getPercentage(GeoFile geoFile) throws FactoryException, TransformException {
        final List<TrackpointWrapper> trackpointWrappers = GeoHelper.getTrackpointsAsStream(geoFile).map(o -> new TrackpointWrapper(o)).collect(Collectors.toList());
        AnalyzeTrack(trackpointWrappers);
        return "unknown";
    }

    private static GeoHelper geoHelper = new GeoHelper();

    private static void AnalyzeTrack(final List<TrackpointWrapper> trackpointWrappers) throws FactoryException, TransformException {
        for(int i = 2; i < trackpointWrappers.size(); i++) {
            final TrackpointWrapper crnt = trackpointWrappers.get(i);
            final TrackpointWrapper prev = trackpointWrappers.get(i-1);
            final double altitutdeDelta = crnt.getTrackpoint().getElevation() - prev.getTrackpoint().getElevation();
            final double distanceDelta = geoHelper.getDistanceTrackpoints(Arrays.asList(prev.getTrackpoint(), crnt.getTrackpoint()));
            crnt.setAltitudeDelta(altitutdeDelta);
            crnt.setDistanceDelta(distanceDelta);
        }
    }

    private void AddMaxSpeedToTrackPoints(final List<TrackpointWrapper> trkPoints)
    {
        if (trkPoints == null || trkPoints.size() == 0)
        {
            return;
        }
        double num = 0.025;
        double num1 = 0;
        TrackpointWrapper item = trkPoints.get(0);
        double altitudeDelta = item.getAltitudeDelta();
        double distanceDelta = item.getDistanceDelta();
        for (int i = 0; i < trkPoints.size(); i++)
        {
            item = trkPoints.get(i);
            if (item.getDistanceDelta() > 0)
            {
                double altitudeDelta1 = (item.getAltitudeDelta() + altitudeDelta) / (1000 * (item.getDistanceDelta() + distanceDelta)) - num1;
                if (altitudeDelta1 < 0)
                {
                    altitudeDelta1 = 0 - altitudeDelta1;
                    num1 = (altitudeDelta1 >= num ? num1 - num : num1 - altitudeDelta1);
                }
                else if (altitudeDelta1 > 0)
                {
                    num1 = (altitudeDelta1 >= num ? num1 + num : num1 + altitudeDelta1);
                }
                altitudeDelta = item.getAltitudeDelta();
                distanceDelta = item.getDistanceDelta();
            }
            if (num1 < -0.03)
            {
                item.setTerrain(TerrainType.DOWN);
            }
            else if (num1 > 0.02)
            {
                item.setTerrain(TerrainType.UP);
            }
            else
            {
                item.setTerrain(TerrainType.FLAT);
            }
        }
        this.smoothTerrain(trkPoints);
    }

    private void smoothTerrain(final List<TrackpointWrapper> trkPoints) {

    }

}
