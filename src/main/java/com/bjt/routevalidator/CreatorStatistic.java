package com.bjt.routevalidator;

import com.bjt.gpxparser.GeoFile;

/**
 * Created by Ben.Taylor on 28/04/2016.
 */
public class CreatorStatistic extends StandardStatistic {
    public CreatorStatistic(GeoFile geoFile) {
        super("Creator", geoFile.getCreator());
    }
}
