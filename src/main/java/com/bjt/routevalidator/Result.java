package com.bjt.routevalidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Ben.Taylor on 25/10/2015.
 */
public class Result {
    public static final String STATUS_ACCEPT = "ACCEPT";
    public static  final String STATUS_REFER = "REFER";

    private String status;

    public void setSpeedLimitStatus(String speedLimitStatus) {
        this.speedLimitStatus = speedLimitStatus;
    }

    private String speedLimitStatus;
    private final GpxFile intendedGpx;
    private final GpxFile actualGpx;
    private final int tolerance;
    private final TrackSummary trackSummary;
    private final List<? extends TrackUsePreference> trackUsePreferences;
    private final List<? extends Statistic> intendedStatistics;
    private final List<? extends Statistic> actualStatistics;
    private String toleranceString;
    private List<String> referralAreas;

    public Result() {
        this(null, null, 200, null, null, null, null);
    }

    public Result(GpxFile intendedGpx, GpxFile actualGpx, int tolerance, List<? extends TrackUsePreference> trackUsePreferences, List<? extends Statistic> intendedStatistics, List<? extends Statistic> actualStatistics, TrackSummary trackSummary) {
        this.intendedGpx = intendedGpx;
        this.actualGpx = actualGpx;
        this.tolerance = tolerance;
        this.trackSummary = trackSummary;
        if(trackUsePreferences == null) trackUsePreferences = new ArrayList<>();
        this.trackUsePreferences = trackUsePreferences;
        this.intendedStatistics = intendedStatistics;
        this.actualStatistics = actualStatistics;
    }

    public List<TableCell[]> getAcceptanceRows() {
        final List<TableCell[]> acceptanceRows = actualStatistics.stream().flatMap(o -> o.getAcceptanceRows().stream()).collect(Collectors.toList());
        return acceptanceRows;
    }

    public boolean hasTrackUsePreferences() {
        final boolean b = getTrackUsePreferences().size() > 0;
        return b;
    }

    public String getAltitudeGraphJson() {
        return trackSummary.getAltitudeGraphJson();
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
