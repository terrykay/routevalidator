import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.GeoFileParser;
import com.bjt.routevalidator.MovingTimeStatistic;
import com.bjt.routevalidator.TimeAtRestStatistic;
import com.bjt.routevalidator.TrackSummary;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

/**
 * Created by Ben.Taylor on 21/04/2016.
 */
public class TimeStatisticTests {
    @Test
    public void Moving() throws Exception {
        final GeoFile geoFile = getGeoFile("/Acceptable.gpx");
        final TrackSummary trackSummary = TrackSummary.AnalyzeTrack(geoFile);
        final String stat = MovingTimeStatistic.getStatistic(trackSummary);
        Assert.assertEquals("08:39", stat);
    }

    @Test
    public void AtRest() throws Exception {
        final GeoFile geoFile = getGeoFile("/Acceptable.gpx");
        final TrackSummary trackSummary = TrackSummary.AnalyzeTrack(geoFile);
        final String stat = TimeAtRestStatistic.getStatistic(trackSummary);
        Assert.assertEquals("01:44", stat);
    }

    private GeoFile getGeoFile(String name) throws Exception {
        final InputStream inputStream = getClass().getResourceAsStream(name);
        final GeoFileParser gpxparser = new GeoFileParser();
        return gpxparser.parseGeoFile(inputStream, name);
    }

}
