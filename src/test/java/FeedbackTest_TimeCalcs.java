import com.bjt.gpxparser.GeoFileParser;
import com.bjt.routevalidator.*;
import org.joda.time.DateTime;
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

    @Test
    public void utcTimeShowsAsBst() { //UTC is 'equivalent' to GMT, and is indicated by the Z suffix.. should show as BST (it's in may)
        final DateTime utcTime = DateTime.parse("2016-04-30T05:09:04Z");
        final String formattedDateTime = DistancePoint.formatDateTime(utcTime);

        Assert.assertEquals("6:09 am", formattedDateTime);
    }
}
