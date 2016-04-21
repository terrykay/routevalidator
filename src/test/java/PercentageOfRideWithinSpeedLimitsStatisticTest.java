import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.GeoFileParser;
import com.bjt.routevalidator.PercentageOfRideWithinSpeedLimitsStatistic;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

/**
 * Created by Ben.Taylor on 21/04/2016.
 */
public class PercentageOfRideWithinSpeedLimitsStatisticTest {
    @Test
    public void Driving() throws Exception {
        final GeoFile geoFile = getGeoFile("/Driving.gpx");
        final String percentage = PercentageOfRideWithinSpeedLimitsStatistic.getPercentage(geoFile, "%.6f");
        Assert.assertEquals("71.03265%", percentage);
    }

    @Test
    public void Acceptble() throws Exception {
        final GeoFile geoFile = getGeoFile("/Acceptable.gpx");
        final String percentage = PercentageOfRideWithinSpeedLimitsStatistic.getPercentage(geoFile, "%.6f");
        Assert.assertEquals("99.9%", percentage);
    }

    private GeoFile getGeoFile(String name) throws Exception {
        final InputStream inputStream = getClass().getResourceAsStream(name);
        final GeoFileParser gpxparser = new GeoFileParser();
        return gpxparser.parseGeoFile(inputStream, name);
    }

}
