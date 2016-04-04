package com.bjt.routevalidator;

import com.bjt.gpxparser.GeoFile;

/**
 * Created by Ben.Taylor on 04/04/2016.
 */
public class TotalTrackpointsStatistic extends  StandardStatistic {
    public TotalTrackpointsStatistic(GeoFile geoFile) {
        super("Total trackpoints in file", getTotalTrackpointsInFile(geoFile));
    }

    private static String getTotalTrackpointsInFile(GeoFile geoFile) {
        final long totalTrackpointCount = GeoHelper.getTrackpointsAsStream(geoFile).count();
        final String totalTrackpointsString = formatTotalTrackpointsInFile(totalTrackpointCount);
        return totalTrackpointsString;
    }

    public static String formatTotalTrackpointsInFile(long totalTrackpoints) {
        return String.format("%,d", totalTrackpoints);
    }
}
