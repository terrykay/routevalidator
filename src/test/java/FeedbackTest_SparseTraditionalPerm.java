import com.bjt.gpxparser.GeoFileParser;
import com.bjt.routevalidator.*;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Ben.Taylor on 13/05/2016.
 */
public class FeedbackTest_SparseTraditionalPerm {
    @Test
    public void sparseTraditionalPerm() throws Exception {
        final GpxFile actual = getGpxFile("/20160430b.gpx");
        final GpxFile intended = getGpxFile("/test400.gpx");
        final ClimbingServerUrlProvider climbingServerUrlProvider = new HardcodedClimbingServerUrlProvider();
        final Validator validator = new Validator(climbingServerUrlProvider);
        final List<TrackUsePreference> trackUsePreferences = TrackUsePreference.getDefault(actual.getGpx());
        final Result validate = validator.validate(intended, actual, 200, trackUsePreferences);
    }

    private GpxFile getGpxFile(String name) throws Exception {
        final InputStream inputStream = getClass().getResourceAsStream(name);
        final GeoFileParser gpxparser = new GeoFileParser();
        return new GpxFile(name, gpxparser.parseGeoFile(inputStream, name));
    }
}
