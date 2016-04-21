package com.bjt.routevalidator;

import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.TrackPoint;

import java.util.ArrayList;
import java.util.List;

import static com.bjt.routevalidator.TerrainType.*;
import static com.bjt.routevalidator.TerrainType.FLAT;

/**
 * Created by Ben.Taylor on 21/04/2016.
 */
public class TrackSummary {
    private double timeCumulative;
    private double distanceCumulative;
    private List<TrackpointWrapper> trkPoints;
    private double totalTime;
    private double totalDistance;
    private boolean inRange;
    private int distanceDeltaOver150m;
    private double maxDistanceDelta;
    private double rangeTime;
    private double rangeDistance;
    private double totalTimeMoving;
    private double totalTimeAtRest;
    private long trackPointCount;
    private long rangeTrackPointCount;

    private long trackPointForClimbingCount;
    private long rangeTrackPointForClimbingCount;
    private double lastUnsmoothedClimbElevation = 500000;

    private double lastClimbElevation = 500000;

    private double thresholdForClimbPoint = 0.1;
    private int algorithm = 1;
    private double totalClimb;
    private double rangeClimb;
    private double totalUnsmoothedClimb;
    private double rangeUnsmoothedClimb;
    private double rangeKmLow = -1;
    private double rangeKmHigh = -1;
    private List<TrackpointWrapper> trackSummary;
    private double proportionOk;


    public static TrackSummary AnalyzeTrack(final GeoFile geoFile) {
        return new TrackSummary(geoFile);
    }

    private TrackSummary(final GeoFile geoFile) {
        boolean flag = false;
        TrackpointWrapper totalDistance = null;
        TrackpointWrapper item = null;
        trkPoints = new ArrayList<>();

        for (final TrackPoint xmlNodes : GeoHelper.getAllPoints(geoFile)) {
            if (flag) {
                totalDistance = new TrackpointWrapper(xmlNodes);
                if (item != null) {
                    TrackLeg trackLeg = new TrackLeg(item, totalDistance);
                    if (!trackLeg.LooksDodgy()) {
                        if (trackLeg.TimeDelta() > (long) 60) {
                            TrackpointWrapper trackPoint = new TrackpointWrapper(totalDistance.getTrackpoint());
                            trackPoint.setDistanceCumulative(this.totalDistance);
                            trackPoint.setTimeCumulative(this.totalTime);
                            this.trkPoints.add(trackPoint);
                        }
                        totalDistance.setDistanceDelta(trackLeg.CalculateLegLength());
                        totalDistance.setTimeDelta(trackLeg.TimeDelta());
                        totalDistance.setAltitudeDelta(trackLeg.ElevationDelta());
                        UpdateTotals(totalDistance);
                        totalDistance.setDistanceCumulative(this.totalDistance);
                        totalDistance.setTimeCumulative(this.totalTime);
                        this.trkPoints.add(totalDistance);
                    } else {
                        item = totalDistance;
                        continue;
                    }
                }
                item = totalDistance;
            } else {
                flag = true;
            }
        }
    }

    private void UpdateTotals(final TrackpointWrapper trackPoint) {
        this.totalTime = this.totalTime + trackPoint.getTimeDelta();
        this.totalDistance = this.totalDistance + trackPoint.getDistanceDelta();
        if (this.isInRange() || this.IsFullRoute()) {
            if (trackPoint.getDistanceDelta() > 0.15) {
                this.distanceDeltaOver150m = this.distanceDeltaOver150m + 1;
            }
            if (trackPoint.getDistanceDelta() > this.maxDistanceDelta) {
                this.maxDistanceDelta = trackPoint.getDistanceDelta();
            }
        }
        if (this.isInRange()) {
            this.rangeTime = this.rangeTime + trackPoint.getTimeDelta();
            TrackSummary distanceDelta1 = this;
            distanceDelta1.rangeDistance = distanceDelta1.rangeDistance + trackPoint.getDistanceDelta();
        }
        if (trackPoint.Speed() != 0) {
            TrackSummary trackSummary1 = this;
            trackSummary1.totalTimeMoving = trackSummary1.totalTimeMoving + (double) trackPoint.getTimeDelta();
        } else {
            TrackSummary timeDelta2 = this;
            timeDelta2.totalTimeAtRest = timeDelta2.totalTimeAtRest + (double) trackPoint.getTimeDelta();
        }
        this.trackPointCount = this.trackPointCount + (long) 1;
        if (this.isInRange()) {
            TrackSummary trackSummary3 = this;
            trackSummary3.rangeTrackPointCount = trackSummary3.rangeTrackPointCount + (long) 1;
        }
        if (this.totalDistance > this.thresholdForClimbPoint) {
            TrackSummary trackSummary4 = this;
            trackSummary4.trackPointForClimbingCount = trackSummary4.trackPointForClimbingCount + (long) 1;
            if (this.isInRange()) {
                TrackSummary trackSummary5 = this;
                trackSummary5.rangeTrackPointForClimbingCount = trackSummary5.rangeTrackPointForClimbingCount + (long) 1;
            }
            int num = 0;
            if (this.algorithm == 4) {
                num = 1;
            }
            if (trackPoint.getTrackpoint().getElevation() > this.lastClimbElevation + (double) num) {
                this.totalClimb = this.totalClimb + (trackPoint.getTrackpoint().getElevation() - this.lastClimbElevation);
                if (this.isInRange()) {
                    this.rangeClimb = this.rangeClimb + (trackPoint.getTrackpoint().getElevation() - this.lastClimbElevation);
                }
            }
            this.lastClimbElevation = trackPoint.getTrackpoint().getElevation();
            this.thresholdForClimbPoint = this.thresholdForClimbPoint + 0.1;
            if (this.algorithm != 2) {
                while (this.totalDistance > this.thresholdForClimbPoint) {
                    TrackSummary trackSummary7 = this;
                    trackSummary7.thresholdForClimbPoint = trackSummary7.thresholdForClimbPoint + 0.1;
                }
            }
        }
        if (trackPoint.getTrackpoint().getElevation() > this.lastUnsmoothedClimbElevation) {
            this.totalUnsmoothedClimb = this.totalUnsmoothedClimb + (trackPoint.getTrackpoint().getElevation() - this.lastUnsmoothedClimbElevation);
            if (this.isInRange()) {
                this.rangeUnsmoothedClimb = this.rangeUnsmoothedClimb + (trackPoint.getTrackpoint().getElevation()- this.lastUnsmoothedClimbElevation);
            }
        }
        this.lastUnsmoothedClimbElevation = trackPoint.getTrackpoint().getElevation();
        if (this.algorithm == 3 && trackPoint.getTrackpoint().getElevation() < this.lastClimbElevation) {
            this.lastClimbElevation = trackPoint.getTrackpoint().getElevation();
        }

    }

    private boolean IsFullRoute() {
        return this.rangeKmLow < 0;
    }

    public boolean isInRange() {
        if (this.rangeKmLow < 0) {
            return false;
        }
        if (this.totalDistance < this.rangeKmLow) {
            return false;
        }
        return this.totalDistance <= this.rangeKmHigh;

    }

    public double getProportionOk() {
        return proportionOk;
    }

    private static int nextid = 1;
    private static int id;
    public void AddSpeed() {
        final int bmpHeight = 180;
        int height = bmpHeight;
        double num = 0.125036949617652;
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
                final double smoothSpeed = (double) num2 * (num1 / (double) height);
                id++;

                item.setSmoothSpeed(smoothSpeed);
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

        proportionOk = ((double)(trkPoints.size() - num11)) / trkPoints.size();
    }


    public void AddMaxSpeedToTrackPoints() {
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
        smoothTerrain();
    }

    public void smoothTerrain() {
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
