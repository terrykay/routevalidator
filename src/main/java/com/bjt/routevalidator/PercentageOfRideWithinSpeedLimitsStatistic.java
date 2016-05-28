package com.bjt.routevalidator;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Ben.Taylor on 19/04/2016.
 */
public class PercentageOfRideWithinSpeedLimitsStatistic extends StandardStatistic implements ValidationContributor {
    private final double proportionOk;

    public PercentageOfRideWithinSpeedLimitsStatistic(final TrackSummary trackSummary) throws FactoryException, TransformException {
        this("Percentage of ride within speed limits", getProportionOk(trackSummary), "%.1f");
    }

    private PercentageOfRideWithinSpeedLimitsStatistic(final String name, final double proportionOk, final String format) {
        super(name, String.format(format, proportionOk * 100) + "%");
        this.proportionOk = proportionOk;
    }

    public static String getPercentage(final TrackSummary trackSummary, final String format) throws FactoryException, TransformException {
        final double proportionOk = getProportionOk(trackSummary);
        return getStatString(format, proportionOk);
    }

    public static String getStatString(String format, double proportionOk) {
        final String statString = String.format(format, proportionOk * 100) + "%";
        return statString;
    }

    public static double getProportionOk(final TrackSummary trackSummary) {
        return trackSummary.getProportionOk();
    }

    @Override
    public boolean validate() {
        return proportionOk >= 0.99;
    }

    @Override
    public List<TableCell[]> getAcceptanceRows() {
        final TableCell[][] tableCells = { getRow() };
        return Arrays.asList(tableCells);
    }

    public TableCell[] getRow() {
        final String speedLimitCompliance = validate() ? Result.STATUS_ACCEPT : Result.STATUS_REFER;
        final TableCell[] tableCells = {new TableCell(1, "Speed Limit Compliance"), new TableCell(1, speedLimitCompliance, "result " + speedLimitCompliance)};
        return tableCells;
    }
}
