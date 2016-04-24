import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.GeoFileParser;
import com.bjt.routevalidator.TrackSummary;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ben.Taylor on 23/04/2016.
 */
public class PruneTests {
    @Test
    public void keep() throws Exception {
        final GeoFile geoFile = getGeoFile("/Driving.gpx");
        Assert.assertEquals(1, geoFile.getTracks().size());
        final ArrayList<String> collection = new ArrayList<>(Arrays.asList("Current Track: 20 APR 2016 16:05"));
        geoFile.pruneTracks(collection);
        Assert.assertEquals(1, geoFile.getTracks().size());
    }

    @Test
    public void getRidOf() throws Exception {
        final GeoFile geoFile = getGeoFile("/Driving.gpx");
        Assert.assertEquals(1, geoFile.getTracks().size());
        final ArrayList<String> collection = new ArrayList<>();
        geoFile.pruneTracks(collection);
        Assert.assertEquals(0, geoFile.getTracks().size());
    }

    private GeoFile getGeoFile(String name) throws Exception {
        final InputStream inputStream = getClass().getResourceAsStream(name);
        final GeoFileParser gpxparser = new GeoFileParser();
        return gpxparser.parseGeoFile(inputStream, name);
    }
}
