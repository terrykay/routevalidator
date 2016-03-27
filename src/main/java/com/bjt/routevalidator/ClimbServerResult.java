package com.bjt.routevalidator;

/**
 * Created by Ben.Taylor on 27/03/2016.
 */
public class ClimbServerResult {
    private boolean success;
    private int climbing;

    public ClimbServerResult() {
    }

    public ClimbServerResult(boolean success, int climbing) {
        this.success = success;
        this.climbing = climbing;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getClimbing() {
        return climbing;
    }
}
