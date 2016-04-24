import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.GeoFileParser;
import com.bjt.routevalidator.TrackSummary;
import org.junit.Test;

import java.io.InputStream;
import java.io.PrintWriter;

/**
 * Created by Ben.Taylor on 23/04/2016.
 */
public class WriteWorkingsTests {
    @Test
    public void outputWorkings() throws Exception {
        final GeoFile geoFile = getGeoFile("/Acceptable.gpx");
        final TrackSummary trackSummary = TrackSummary.AnalyzeTrack(geoFile);
        try(PrintWriter pw = new PrintWriter(System.out)) {
            trackSummary.writeWorkings(pw);
        }
    }

    private GeoFile getGeoFile(String name) throws Exception {
        final InputStream inputStream = getClass().getResourceAsStream(name);
        final GeoFileParser gpxparser = new GeoFileParser();
        return gpxparser.parseGeoFile(inputStream, name);
    }
}
