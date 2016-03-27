package com.bjt.routevalidator;

import com.bjt.gpxparser.TrackPoint;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben.Taylor on 25/10/2015.
 */
public class Result {
    public static final String STATUS_ACCEPT = "ACCEPT";
    public static  final String STATUS_REFER = "REFER";

    private String status;
    private final GpxFile intendedGpx;
    private final GpxFile actualGpx;
    private final int tolerance;
    private final List<? extends TrackUsePreference> trackUsePreferences;
    private final List<? extends Statistic> intendedStatistics;
    private final List<? extends Statistic> actualStatistics;
    private String toleranceString;
    private List<String> referralAreas;

    public Result(GpxFile intendedGpx, GpxFile actualGpx, int tolerance, List<? extends TrackUsePreference> trackUsePreferences, List<? extends Statistic> intendedStatistics, List<? extends Statistic> actualStatistics) {
        this.intendedGpx = intendedGpx;
        this.actualGpx = actualGpx;
        this.tolerance = tolerance;
        if(trackUsePreferences == null) trackUsePreferences = new ArrayList<>();
        this.trackUsePreferences = trackUsePreferences;
        this.intendedStatistics = intendedStatistics;
        this.actualStatistics = actualStatistics;

    }

    public boolean hasTrackUsePreferences() {
        final boolean b = getTrackUsePreferences().size() > 0;
        return b;
    }

    public boolean isProcessed() {
        return intendedGpx != null && actualGpx != null;
    }

    public String getSubmitAction() {
        return isProcessed() ? "revalidate" : "validate";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTolerance() {
        return tolerance;
    }

    public String getToleranceString() {
        if(tolerance == 1000) return "1km";
        else return String.valueOf(tolerance) + "m";
    }

    public GpxFile getIntendedGpx() {
        return intendedGpx;
    }

    public GpxFile getActualGpx() {
        return actualGpx;
    }

    public void setReferralAreas(List<String> referralAreas) {
        this.referralAreas = referralAreas;
    }

    public List<String> getReferralAreas() {
        return this.referralAreas;
    }

    public String getReferralAreasString() {
        return "[" + String.join(",", referralAreas) + "]";
    }

    public List<? extends TrackUsePreference> getTrackUsePreferences() {
        return trackUsePreferences;
    }

    public List<? extends Statistic> getIntendedStatistics() {
        return intendedStatistics;
    }

    public List<? extends Statistic> getActualStatistics() {
        return actualStatistics;
    }
}
