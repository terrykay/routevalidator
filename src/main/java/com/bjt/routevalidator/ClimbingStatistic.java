package com.bjt.routevalidator;

import com.vividsolutions.jts.geom.Coordinate;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben.Taylor on 27/03/2016.
 */
public class ClimbingStatistic  extends  StandardStatistic{
    public ClimbingStatistic(List<List<Coordinate>> intendedPaths, ServletContext servletContext) throws IOException {
        super("Climbing", getClimbing(intendedPaths, servletContext));
    }

    private static String getClimbing(List<List<Coordinate>> intendedPaths, ServletContext servletContext) throws IOException {
        final String climbServerUrl = servletContext.getInitParameter("ClimbingServerUrl");
        final URL url = new URL(climbServerUrl);
        final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        try(final OutputStream outputStream = urlConnection.getOutputStream()) {
            try(final PrintWriter printWriter = new PrintWriter(outputStream)) {
                final String coordsAsStringForClimbingServer = getCoordsAsStringForClimbingServer(intendedPaths);
                printWriter.println(coordsAsStringForClimbingServer);
            }
        }
        urlConnection.

        return null;
    }

    private static String getCoordsAsStringForClimbingServer(List<List<Coordinate>> paths) {
        final List<String> latLongs = new ArrayList<>();
        for(final List<Coordinate> path : paths) {
            for(final Coordinate coord : path) {
                final String coordString = String.format("%.4f,%.4f", coord.y, coord.x);
                latLongs.add(coordString);
            }
        }
        final String latLongStrings = String.join(";", latLongs);
        return latLongStrings;
    }
}
