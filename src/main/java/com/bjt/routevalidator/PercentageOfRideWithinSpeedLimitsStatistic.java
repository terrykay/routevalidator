package com.bjt.routevalidator;

import com.bjt.gpxparser.GeoFile;
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
        super("Percentage of ride within speed limits", getPercentage(geoFile));
    }

    public static String getPercentage(GeoFile geoFile) throws FactoryException, TransformException {
        final List<TrackpointWrapper> trackpointWrappers = GeoHelper.getTrackpointsAsStream(geoFile).map(o -> new TrackpointWrapper(o)).collect(Collectors.toList());
        final String stat = AnalyzeTrack(trackpointWrappers);
        return stat;
    }

    private static String AnalyzeTrack(final List<TrackpointWrapper> trackpointWrappers) throws FactoryException, TransformException {
        double distanceCumulative = 0;
        for(int i = 2; i < trackpointWrappers.size(); i++) {
            final TrackpointWrapper current = trackpointWrappers.get(i);
            final TrackpointWrapper prev = trackpointWrappers.get(i-1);
            final double altitutdeDelta = current.getTrackpoint().getElevation() - prev.getTrackpoint().getElevation();
            final double distanceDelta = calculateLegLength(prev, current);
            distanceCumulative += distanceDelta;
            final double timeDelta = getTimeDelta(prev, current);
            current.setAltitudeDelta(altitutdeDelta);
            current.setDistanceDelta(distanceDelta);
            current.setDistanceCumulative(distanceCumulative);
            current.setTimeDelta(timeDelta);
        }
        if(trackpointWrappers.size() > 0) trackpointWrappers.remove(0);
        if(trackpointWrappers.size() > 0) trackpointWrappers.remove(0);//ValidateGPX ignores the first TWO trackpoints (for reasons best only known to itself)
        AddMaxSpeedToTrackPoints(trackpointWrappers);
        final double fractionOk = AddSpeed(trackpointWrappers);
        final String percentString = String.format("%.1f", fractionOk * 100) + "%";
        return percentString;
    }

    private static double getTimeDelta(final TrackpointWrapper legStart, final TrackpointWrapper legEnd) {
        if (legStart == null || legEnd == null)
        {
            return (long)0;
        }
        final Seconds seconds = Seconds.secondsBetween(legStart.getDateTime(), legEnd.getDateTime());
        final long timeDelta = seconds.getSeconds();
        return timeDelta;
    }

    private static double AddSpeed(final List<TrackpointWrapper> trkPoints)
    {
        final int bmpHeight = 180;
        int height = bmpHeight;
        double num = 0.12507046194445573;
        double num1 = 80;
        double height1 = num1 / bmpHeight;
        int num2 = 0;
        int num3 = 0;
        int num4 = 14;
        int num5 = 14;

        boolean flag = false;
        boolean flag1 = false;
        int num6 = 4;
        int num7 = 0;
        int num8 = -1;

        final List<Double> speedPoints = new ArrayList<>();
        for (int i = 0; i < trkPoints.size(); i++)
        {
            TrackpointWrapper item = trkPoints.get(i);
            double num9 = 0;
            num9 = item.getDistanceCumulative();
            num7 = (int) Math.round(num9 / num);
            int num10 = (int)Math.round( item.Speed() / height1);
            if (num10 < num6)
            {
                num2 = 1;
                flag = false;
                flag1 = false;
            }
            else if (num10 > num2)
            {
                if (num10 <= num2 + num4)
                {
                    num2 = num10;
                    flag = false;
                    flag1 = false;
                }
                else if (!flag1)
                {
                    num2 = num2 + num4;
                    flag = true;
                }
                else
                {
                    flag = false;
                    flag1 = false;
                }
            }
            else if (num10 >= num2 - num5)
            {
                num2 = num10;
                flag1 = false;
                flag = false;
            }
            else if (!flag)
            {
                num2 = num2 - num5;
                flag1 = true;
                flag = false;
            }
            else
            {
                flag1 = false;
                flag = false;
            }
            if (num2 == 1)
            {
                if (num8 >= 0 && num3 > 1)
                {
                }
            }
            else if (!flag && !flag1)
            {
                num2 = (num3 + num2) / 2;
            }
            num3 = num2;
            if (num7 != num8)
            {
                while (num7 > num8)
                {
                    speedPoints.add(item.Speed());
                    num8++;
                }
                item.setSmoothSpeed((double)num2 * (num1 / (double)height));
/*
                point = new Point(num7, height - num2);
                arrayLists.Add(point);
*/
            }
            else
            {
//                point.Y = height - num2;
            }
        }
/*
        if (arrayLists.Count > 1)
        {
            grfx.DrawLines(pen, (Point[])arrayLists.ToArray(typeof(Point)));
        }
*/
        final double MAX_SPEED_UP = 24;
        final double MAX_SPEED_FLAT = 44;
        final double MAX_SPEED_DOWN = 68;
        TerrainType terrainType = UNDECIDED;
        TrackpointWrapper trackPoint = trkPoints.get(0);
        for (int j = 1; j < trkPoints.size(); j++)
        {
            TrackpointWrapper item1 = trkPoints.get(j);
            TerrainType terrain = item1.getTerrain();
            TerrainType terrainType1 = terrain;
            if (terrain == FLAT && terrainType == DOWN)
            {
                terrainType1 = DOWN;
            }
            else if (terrain == UP && terrainType == FLAT)
            {
                terrainType1 = FLAT;
            }
            else if (terrain == UP && terrainType == DOWN)
            {
                terrainType1 = DOWN;
            }
            if (terrain == DOWN && terrainType == FLAT)
            {
                trackPoint.setTerrain(DOWN);
            }
            else if (terrain == FLAT && terrainType == UP)
            {
                trackPoint.setTerrain(FLAT);
            }
            else if (terrain == DOWN && terrainType == UP)
            {
                trackPoint.setTerrain(DOWN);
            }
            terrainType = terrain;
            item1.setTerrain(terrainType1);
        }
        int num11 = 0;
        for (int k = 0; k < trkPoints.size(); k++)
        {
            TrackpointWrapper trackPoint1 = trkPoints.get(k);
            double mAXSPEEDFLAT = MAX_SPEED_FLAT;
            switch (trackPoint1.getTerrain())
            {
                case UP:
                {
                    mAXSPEEDFLAT = MAX_SPEED_UP;
                    break;
                }
                case DOWN:
                {
                    mAXSPEEDFLAT = MAX_SPEED_DOWN;
                    break;
                }
                default:
                {
                    mAXSPEEDFLAT = MAX_SPEED_FLAT;
                    break;
                }
            }
            if (trackPoint1.getSmoothSpeed() > mAXSPEEDFLAT)
            {
                num11++;
            }
        }

        double fractionOk = ((double)(trkPoints.size() - num11)) / trkPoints.size();
        return fractionOk;
    }

    private static double calculateLegLength(final TrackpointWrapper legStart, final TrackpointWrapper legEnd) {
        //in ValidateGPX's way
        if (legStart == null || legEnd == null)
        {
            return 0;
        }
        double radian = legStart.LongitudeToRadian();
        double num = legEnd.LongitudeToRadian();
        double radian1 = legStart.LatitudeToRadian();
        double num1 = legEnd.LatitudeToRadian();

        double num2 = Math.cos(radian) * Math.cos(radian1) * Math.cos(num) * Math.cos(num1);
        num2 = num2 + Math.sin(radian) * Math.cos(radian1) * Math.sin(num) * Math.cos(num1);
        num2 = num2 + Math.sin(radian1) * Math.sin(num1);
        if (num2 > 1)
        {
            num2 = 1;
        }
        num2 = Math.acos(num2);
        return num2 * 6378;
    }

    private static void AddMaxSpeedToTrackPoints(final List<TrackpointWrapper> trkPoints)
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
                item.setTerrain(DOWN);
            }
            else if (num1 > 0.02)
            {
                item.setTerrain(UP);
            }
            else
            {
                item.setTerrain(FLAT);
            }
        }
        smoothTerrain(trkPoints);
    }

    private static void smoothTerrain(final List<TrackpointWrapper> trkPoints) {
        if (trkPoints == null || trkPoints.size() == 0)
        {
            return;
        }
        TerrainType terrain = trkPoints.get(0).getTerrain();
        TerrainType terrainType;
        int num = 5;
        int num1 = 4;
        for (int i = 2; i < trkPoints.size() ; i++)
        {
            TrackpointWrapper item = trkPoints.get(i);
            terrainType = item.getTerrain();
            if (terrain != terrainType)
            {
                if ((terrain != FLAT || terrainType != DOWN) && (terrain != UP || terrainType != FLAT))
                {
                    int num2 = num;
                    for (int j = i + 1; j < trkPoints.size(); j++)
                    {
                        int num3 = num2;
                        num2 = num3 - 1;
                        if (num3 <= 0 || trkPoints.get(j).getTerrain() == terrain)
                        {
                            break;
                        }
                    }
                    if (num2 > 0)
                    {
                        item.setTerrain(terrain);
                    }
                    else
                    {
                        terrain = terrainType;
                    }
                }
                else
                {
                    int num4 = num1;
                    int num5 = 0;
                    for (int k = i + 1; k < trkPoints.size() && num5 < num1 - 1 && num4 > 0; k++)
                    {
                        if (trkPoints.get(k).getTerrain() != terrainType)
                        {
                            num4--;
                        }
                        else
                        {
                            num4 = num1;
                            num5++;
                        }
                    }
                    if (num5 < num1 - 2)
                    {
                        item.setTerrain( terrain);
                    }
                    else
                    {
                        terrain = terrainType;
                    }
                }
            }
        }

    }

}
