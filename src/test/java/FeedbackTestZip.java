import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.GeoFileParser;
import com.bjt.routevalidator.DurationStatistic;
import com.bjt.routevalidator.TrackSummary;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

/**
 * Created by Ben.Taylor on 09/06/2016.
 */
public class FeedbackTestZip {
    @Test
    public void DurationStatisticFailsToParse() throws Exception {

        final GeoFile geoFile = getGeoFile("/Refer1and2.zip");

        final String duration = DurationStatistic.getDuration(geoFile);

    }

    private GeoFile getGeoFile(String name) throws Exception {
        final InputStream inputStream = getClass().getResourceAsStream(name);
        final GeoFileParser gpxparser = new GeoFileParser();
        return gpxparser.parseGeoFile(inputStream, name);
    }

}
