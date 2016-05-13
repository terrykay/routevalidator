import com.bjt.gpxparser.GeoFileParser;
import com.bjt.routevalidator.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Ben.Taylor on 13/05/2016.
 */
public class FeedbackTest_TimeCalcs {
    @Test
    public void timeAtRest() throws Exception {
        final GpxFile actual = getGpxFile("/20160430b.gpx"); //multiple tracks spanning midnight
        final TrackSummary trackSummary = TrackSummary.AnalyzeTrack(actual.getGpx());
        final String stat = TimeAtRestStatistic.getStatistic(trackSummary);
        Assert.assertEquals("04:45", stat);
    }

    private GpxFile getGpxFile(String name) throws Exception {
        final InputStream inputStream = getClass().getResourceAsStream(name);
        final GeoFileParser gpxparser = new GeoFileParser();
        return new GpxFile(name, gpxparser.parseGeoFile(inputStream, name));
    }
}
