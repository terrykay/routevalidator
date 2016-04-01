package com.bjt.routevalidator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vividsolutions.jts.geom.Coordinate;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ben.Taylor on 27/03/2016.
 */
public class ClimbingStatistic  extends  StandardStatistic{
    public ClimbingStatistic(List<List<Coordinate>> intendedPaths, ServletContext servletContext) throws IOException {
        super("Climbing", getClimbing(intendedPaths, servletContext));
    }

    private static String getClimbing(List<List<Coordinate>> intendedPaths, ServletContext servletContext) throws IOException {
        final String climbServerUrl = servletContext.getInitParameter("ClimbingServerUrl");
        final ClimbServerResult climbServerResult = getClimbing(intendedPaths, climbServerUrl);

        if(climbServerResult != null && climbServerResult.isSuccess()) {
            final String climbing = String.format("%,d m", climbServerResult.getClimbing());
            return climbing;
        } else {
            return "?";
        }
    }

    public static ClimbServerResult getClimbing(List<List<Coordinate>> intendedPaths, String climbServerUrl) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            final HttpPost httpPost = new HttpPost(climbServerUrl);
            final String coordsAsStringForClimbingServer = getCoordsAsStringForClimbingServer(intendedPaths);
            httpPost.setEntity(new StringEntity(coordsAsStringForClimbingServer));
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
                final HttpEntity entity = httpResponse.getEntity();
                final GsonBuilder gsonBuilder = new GsonBuilder();
                final Gson gson = gsonBuilder.create();
                final String s = EntityUtils.toString(entity);
                final ClimbServerResult result = gson.fromJson(s, ClimbServerResult.class);
                return result;
            }
        } catch(final Exception exception) {
            Logger.getLogger(ClimbingStatistic.class.getName()).log(Level.SEVERE, "Error in getClimbing", exception);
            return new ClimbServerResult(false, 0);
        }
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
