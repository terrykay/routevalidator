package com.bjt.routevalidator;

/**
 * Created by Ben.Taylor on 25/10/2015.
 */
public class Result {
    public static final String STATUS_ACCEPT = "ACCEPT";
    public static  final String STATUS_REFER = "REFER";

    private String status;
    private GpxFile intendedGpx;
    private GpxFile actualGpx;
    private int tolerance;

    public Result(GpxFile intendedGpx, GpxFile actualGpx, int tolerance) {
        this.intendedGpx = intendedGpx;
        this.actualGpx = actualGpx;
        this.tolerance = tolerance;
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

    public void setTolerance(int tolerance) {
        this.tolerance = tolerance;
    }

    public GpxFile getIntendedGpx() {
        return intendedGpx;
    }

    public void setIntendedGpx(GpxFile intendedGpx) {
        this.intendedGpx = intendedGpx;
    }

    public GpxFile getActualGpx() {
        return actualGpx;
    }

    public void setActualGpx(GpxFile actualGpx) {
        this.actualGpx = actualGpx;
    }
}
