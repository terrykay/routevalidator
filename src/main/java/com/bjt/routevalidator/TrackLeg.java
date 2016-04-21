package com.bjt.routevalidator;

import com.bjt.gpxparser.TrackPoint;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

/**
 * Created by Ben.Taylor on 21/04/2016.
 */
public class TrackLeg {
    private final TrackpointWrapper legStart;
    private final TrackpointWrapper legEnd;

    public TrackLeg(final TrackpointWrapper legStart, final TrackpointWrapper legEnd) {

        this.legStart = legStart;
        this.legEnd = legEnd;
    }

    public boolean LooksDodgy() {
        if (this.legEnd == null || this.legStart == null)
        {
            return true;
        }
        if (this.CalculateLegLength() > 10)
        {
            return true;
        }
        if (this.legStart.getTrackpoint().getLat() > -0.5 && this.legStart.getTrackpoint().getLat() < 0.5)
        {
            return true;
        }
        if (this.legEnd.getTrackpoint().getLat() > -0.5 && this.legEnd.getTrackpoint().getLat() < 0.5)
        {
            return true;
        }
        return false;
    }

    public double CalculateLegLength() {
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

    public long TimeDelta() {
        if (legStart == null || legEnd == null)
        {
            return (long)0;
        }
        final DateTime startDateTime = DateTime.parse(legStart.getTrackpoint().getTime());
        final DateTime endDateTime= DateTime.parse(legEnd.getTrackpoint().getTime());
        final Seconds seconds = Seconds.secondsBetween(startDateTime, endDateTime);
        final long timeDelta = seconds.getSeconds();
        return timeDelta;
    }

    public double ElevationDelta() {
        return legEnd.getTrackpoint().getElevation() - legStart.getTrackpoint().getElevation();
    }
}
