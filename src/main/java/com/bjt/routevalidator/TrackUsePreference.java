package com.bjt.routevalidator;

import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.Track;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben.Taylor on 23/03/2016.
 */
public class TrackUsePreference {
    private String trackName;
    private boolean render;

    public TrackUsePreference(String trackName, boolean render) {
        this.trackName = trackName;
        this.render = render;
    }

    public String getTrackName() {
        return trackName;
    }

    public boolean isRender() {
        return render;
    }

    public static final double DEFAULT_MIN_DISTANCE = 2500;
    public static List<TrackUsePreference> getDefault(final GeoFile geoFile) throws FactoryException, TransformException {
        final List<TrackUsePreference> trackUsePreferences = new ArrayList<>();
        final GeoHelper geoHelper = new GeoHelper();
        for(final Track track : geoFile.getTracks()) {
            boolean render = geoHelper.getDistance(track) >= DEFAULT_MIN_DISTANCE;
            final TrackUsePreference trackUsePreference = new TrackUsePreference(track.getName(), render);
            trackUsePreferences.add(trackUsePreference);
        }
        return trackUsePreferences;
    }
}
