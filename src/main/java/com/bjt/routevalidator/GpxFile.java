package com.bjt.routevalidator;

import com.bjt.gpxparser.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben.Taylor on 29/10/2015.
 */
public class GpxFile {
    public GpxFile(String fileName, GeoFile geoFile) {
        this.fileName = fileName;
        this.gpx = geoFile;
    }

    private final String fileName;
    private final GeoFile gpx;

    public String getFileName() {
        return fileName;
    }

    public GeoFile getGpx() {
        return gpx;
    }

    public String getSimpleLatLngArray() {
        final List<String> trackSegmentStrings = new ArrayList<>();
        for(final Track track : gpx.getTracks()) {
            for(final TrackSegment trackSegment : track.getTrackSegments()) {
                final List<String> trackpointStrings = new ArrayList<>();
                for(final TrackPoint trackPoint : trackSegment.getTrackPoints()) {
                    trackpointStrings.add(String.format("[%.4f,%.4f]", trackPoint.getLat(), trackPoint.getLon()));
                }
                final String trackSegmentString = "[" + String.join(",", trackpointStrings)+ "]";
                trackSegmentStrings.add(trackSegmentString);
            }
        }
        final String returnValue = "[" + String.join(",", trackSegmentStrings) + "]";
        return returnValue;
    }
}
