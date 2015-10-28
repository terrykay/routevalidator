package com.bjt.routevalidator;

import com.bjt.gpxparser.Gpx;

/**
 * Created by Ben.Taylor on 25/10/2015.
 */
public class Validator {
    public static Result validate(Gpx intendedGpx, Gpx actualGpx, int tolerance) {
        final Result result = new Result();
        result.setTolerance(tolerance);
        return result;
    }
}
