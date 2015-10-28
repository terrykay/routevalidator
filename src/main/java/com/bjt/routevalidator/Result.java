package com.bjt.routevalidator;

/**
 * Created by Ben.Taylor on 25/10/2015.
 */
public class Result {
    public static final String STATUS_ACCEPT = "Accept";
    public static  final String STATUS_REFER = "Refer";

    private String status;
    private int tolerance;

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


}
