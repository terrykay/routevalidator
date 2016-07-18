package com.bjt.routevalidator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jdk.nashorn.internal.runtime.URIUtils;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.taglibs.standard.tag.common.core.Util;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    public static final String STATUS_REFER = "REFER";

    public List<String> getWarnings() {
        return warnings;
    }

    private final List<String> warnings;

    private String status;
    private boolean isUrlLoaded;

    public void setSpeedLimitStatus(String speedLimitStatus) {
        this.speedLimitStatus = speedLimitStatus;
    }

    private String speedLimitStatus;
    private final GpxFile intendedGpx;
    private final GpxFile actualGpx;
    private final int tolerance;

    public TrackSummary getTrackSummary() {
        return trackSummary;
    }

    private final TrackSummary trackSummary;
    private final List<? extends TrackUsePreference> trackUsePreferences;
    private final List<? extends Statistic> intendedStatistics;
    private final List<? extends Statistic> actualStatistics;
    private String toleranceString;
    private List<String> referralAreas;

    public static final int DEFAULT_TOLERANCE = 200;

    public Result() {
        this(null, null, DEFAULT_TOLERANCE, null, null, null, null);
    }

    public Result(GpxFile intendedGpx, GpxFile actualGpx, int tolerance, List<? extends TrackUsePreference> trackUsePreferences, List<? extends Statistic> intendedStatistics, List<? extends Statistic> actualStatistics, TrackSummary trackSummary) {
        this.intendedGpx = intendedGpx;
        this.actualGpx = actualGpx;
        this.tolerance = tolerance;
        this.trackSummary = trackSummary;
        if (trackUsePreferences == null) trackUsePreferences = new ArrayList<>();
        this.trackUsePreferences = trackUsePreferences;
        this.intendedStatistics = intendedStatistics;
        this.actualStatistics = actualStatistics;
        warnings = new ArrayList<>();
    }

    public void addWarning(final String warning) {
        warnings.add(warning);
    }

    public List<TableCell[]> getAcceptanceRows() {
        final List<TableCell[]> acceptanceRows = actualStatistics.stream().flatMap(o -> o.getAcceptanceRows().stream()).collect(Collectors.toList());
        return acceptanceRows;
    }

    public boolean hasTrackUsePreferences() {
        final boolean b = getTrackUsePreferences().size() > 0;
        return b;
    }

    public String getDistancePointsJson() {
        return trackSummary.getDistancePointsJson();
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
        if (tolerance == 1000) return "1km";
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

    public List<TableCell[]> getPreliminaryRows() throws FriendlyException {
        final PercentageOfRideWithinSpeedLimitsStatistic speedLimitsStatistic = (PercentageOfRideWithinSpeedLimitsStatistic)
                getActualStatistics().stream().filter(e -> e instanceof PercentageOfRideWithinSpeedLimitsStatistic)
                .findFirst().orElseThrow(() -> new FriendlyException("Speed limit statistic not found"));
        final boolean validate = referralAreas.isEmpty() && speedLimitsStatistic.validate();
        final TableCell[] speedLimitsStatisticRow = speedLimitsStatistic.getRow();
        final List<TableCell[]> tableCells = Arrays.asList(
                new TableCell[]{new TableCell(1, "Validate"), new TableCell(1, validate ? "Yes" : "TBC")},
                new TableCell[]{new TableCell(1, "Track adherence"), new TableCell(1, referralAreas.isEmpty() ? "ACCEPT" : "REFER")},
                new TableCell[]{new TableCell(1, "@ Tolerance distance"), new TableCell(1, getToleranceString())},
                speedLimitsStatisticRow
        );
        return tableCells;
    }

    public boolean isUrlLoaded() {
        return isUrlLoaded;
    }

    public void setIsUrlLoaded(boolean isUrlLoaded) {
        this.isUrlLoaded = isUrlLoaded;
    }

    public String getMailtoHref(HttpServletRequest request) throws IOException {

        final String aaaEmail = Utility.getAAAEmailAddress();
        final String rider = request.getParameter("rider");

        if (aaaEmail != null && !aaaEmail.isEmpty()) {
            String href = "mailto:" + aaaEmail + "?subject=" + URIUtil.encodeWithinQuery("AAA Validation for DIY");

            if (isUrlLoaded) {
                final String comparisonUrl = "http://routevalidator.com/validate?intended=" + getIntendedGpx().getFileName() + "&actual=" + getActualGpx().getFileName() + "&tolerance=" + getTolerance();
                String extraMessage = "Intended: " + getIntendedGpx().getFileName() + "\r\n" +
                        "Actual: " + getActualGpx().getFileName() + "\r\n" +
                        "Comparison: " + comparisonUrl;
                if(rider != null && !rider.isEmpty()) {
                    extraMessage = "Rider: " + rider + "\r\n" + extraMessage;
                }
                href += "&body=" + URIUtil.encodeWithinQuery(extraMessage);
            }
            return href;
        }
        else return null;
    }
}
