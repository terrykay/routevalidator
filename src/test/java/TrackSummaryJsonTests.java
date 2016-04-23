import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.GeoFileParser;
import com.bjt.routevalidator.TimeAtRestStatistic;
import com.bjt.routevalidator.TrackSummary;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

/**
 * Created by Ben.Taylor on 23/04/2016.
 */
public class TrackSummaryJsonTests {
    @Test
    public void outputGraphJson() throws Exception {
        final GeoFile geoFile = getGeoFile("/Acceptable.gpx");
        final TrackSummary trackSummary = TrackSummary.AnalyzeTrack(geoFile);
        final String graphJson = trackSummary.getAltitudeGraphJson();
        System.out.println(graphJson);
    }

    @Test
    public void outputDistancePointsJson() throws Exception {
        final GeoFile geoFile = getGeoFile("/Acceptable.gpx");
        final TrackSummary trackSummary = TrackSummary.AnalyzeTrack(geoFile);
        final String graphJson = trackSummary.getDistancePointsJson();
        System.out.println(graphJson);
    }

    private GeoFile getGeoFile(String name) throws Exception {
        final InputStream inputStream = getClass().getResourceAsStream(name);
        final GeoFileParser gpxparser = new GeoFileParser();
        return gpxparser.parseGeoFile(inputStream, name);
    }
}
