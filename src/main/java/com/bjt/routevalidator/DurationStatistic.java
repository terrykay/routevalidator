package com.bjt.routevalidator;

import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.TrackPoint;
import com.bjt.gpxparser.TrackpointT;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.Date;
import java.util.List;

/**
 * Created by Ben.Taylor on 03/04/2016.
 */
public class DurationStatistic extends StandardStatistic {
    public DurationStatistic(final GeoFile geoFile) {
        super("Duration", getDuration(geoFile));
    }

    private static String getDuration(final GeoFile geoFile) {
        final List<? extends TrackPoint> trackPoints = GeoHelper.getAllPoints(geoFile);
        final TrackPoint lastTrackpoint = trackPoints.get(trackPoints.size() - 1);
        final TrackPoint firstTrackpoint = trackPoints.get(0);
        final String duration = getDuration(firstTrackpoint.getTime(), lastTrackpoint.getTime());
        return duration;
    }

    public static String getDuration(String time1, String time2) {
        final DateTime dateTime1 = DateTime.parse(time1);
        final DateTime dateTime2 = DateTime.parse(time2);
        final Period period = new Period(dateTime1, dateTime2);
        final String durationString = getDurationString(period);
        return durationString;
    }

    public static String getDurationString(final Period period) {
        final PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
                .printZeroAlways()
                .minimumPrintedDigits(2)
                .appendHours()
                .appendSeparator(":")
                .appendMinutes()
                .toFormatter();
        final Period hoursAndMinutes = period.normalizedStandard(PeriodType.time());
        return hoursAndMinutes.toString(periodFormatter);
    }
}
