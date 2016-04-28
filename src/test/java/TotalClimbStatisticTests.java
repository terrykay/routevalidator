import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.GeoFileParser;
import com.bjt.routevalidator.MovingTimeStatistic;
import com.bjt.routevalidator.TimeAtRestStatistic;
import com.bjt.routevalidator.TotalClimbStatistic;
import com.bjt.routevalidator.TrackSummary;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

/**
 * Created by Ben.Taylor on 21/04/2016.
 */
public class TotalClimbStatisticTests {
    @Test
    public void Acceptable() throws Exception {
        final GeoFile geoFile = getGeoFile("/Acceptable.gpx");
        final TrackSummary trackSummary = TrackSummary.AnalyzeTrack(geoFile);
        final String stat = TotalClimbStatistic.getTotalClimb(trackSummary);
        Assert.assertEquals("2,168 m", stat);
    }

    private GeoFile getGeoFile(String name) throws Exception {
        final InputStream inputStream = getClass().getResourceAsStream(name);
        final GeoFileParser gpxparser = new GeoFileParser();
        return gpxparser.parseGeoFile(inputStream, name);
    }

}
