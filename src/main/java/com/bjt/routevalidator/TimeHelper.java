package com.bjt.routevalidator;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Created by Ben.Taylor on 13/05/2016.
 */
public class TimeHelper {
    public static DateTime toLocal(final DateTime dateTime) {
        return dateTime.withZone(DateTimeZone.forID("Europe/London"));
    }
}
