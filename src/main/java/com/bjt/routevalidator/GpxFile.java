package com.bjt.routevalidator;

import com.bjt.gpxparser.Gpx;
import com.bjt.gpxparser.Track;
import com.bjt.gpxparser.TrackPoint;
import com.bjt.gpxparser.TrackSegment;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben.Taylor on 29/10/2015.
 */
public class GpxFile {
    public GpxFile(String fileName, Gpx gpx) {
        this.fileName = fileName;
        this.gpx = gpx;
    }

    private final String fileName;
    private final Gpx gpx;

    public String getFileName() {
        return fileName;
    }

    public Gpx getGpx() {
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
