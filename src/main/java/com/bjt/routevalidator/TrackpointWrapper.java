package com.bjt.routevalidator;

import com.bjt.gpxparser.TrackPoint;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * Created by Ben.Taylor on 19/04/2016.
 */
public class TrackpointWrapper  {
    private final TrackPoint trackPoint;
    private final DateTime dateTime;
    private double altitudeDelta;
    private double distanceDelta;
    private TerrainType terrain;
    private long timeDelta;
    private double distanceCumulative;
    private double smoothSpeed;
    private long timeCumulative;

    public TrackpointWrapper(final TrackPoint trackPoint) {
        this.trackPoint = trackPoint;
        this.dateTime = DateTime.parse(trackPoint.getTime());
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

    public double LongitudeToRadian() {
        return trackPoint.getLon() * 3.14159265358979 / 180;
    }

    public double LatitudeToRadian() {
        return trackPoint.getLat() * 3.14159265358979 / 180;
    }

    public TerrainType getTerrain() {
        return terrain;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setTimeDelta(long timeDelta) {
        this.timeDelta = timeDelta;
    }

    public void setDistanceCumulative(double distanceCumulative) {
        this.distanceCumulative = distanceCumulative;
    }

    public double getDistanceCumulative() {
        return distanceCumulative;
    }

    public double Speed() {
        double num = 0;
        if (this.distanceDelta > 0 && this.timeDelta > (long)0)
        {
            num = this.distanceDelta * 3600 / (double)this.timeDelta;
        }
        if (num <= 4)
        {
            num = 0;
        }
        return num;

    }

    public void setSmoothSpeed(double smoothSpeed) {
        this.smoothSpeed = smoothSpeed;
    }

    public double getSmoothSpeed() {
        return smoothSpeed;
    }

    public void setTimeCumulative(long timeCumulative) {
        this.timeCumulative = timeCumulative;
    }

    public Duration getTimeCumulative() {
        final Duration duration = Duration.standardSeconds(timeCumulative);
        return duration;
    }

    public long getTimeDelta() {
        return timeDelta;
    }
}
