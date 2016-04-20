package com.bjt.routevalidator;

import com.bjt.gpxparser.TrackPoint;

/**
 * Created by Ben.Taylor on 19/04/2016.
 */
public class TrackpointWrapper  {
    private final TrackPoint trackPoint;
    private double altitudeDelta;
    private double distanceDelta;
    private TerrainType terrain;

    public TrackpointWrapper(final TrackPoint trackPoint) {
        this.trackPoint = trackPoint;
    }

    public TrackPoint getTrackpoint() {
        return trackPoint;
    }

    public void setAltitudeDelta(double altitudeDelta) {
        this.altitudeDelta = altitudeDelta;
    }

    public double getAltitudeDelta() {
        return altitudeDelta;
    }

    public void setDistanceDelta(double distanceDelta) {
        this.distanceDelta = distanceDelta;
    }

    public double getDistanceDelta() {
        return distanceDelta;
    }

    public void setTerrain(final TerrainType terrain) {
        this.terrain = terrain;
    }
}
