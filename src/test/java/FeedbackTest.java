import com.bjt.gpxparser.GeoFileParser;
import com.bjt.routevalidator.ClimbingServerUrlProvider;
import com.bjt.routevalidator.GpxFile;
import com.bjt.routevalidator.TrackUsePreference;
import com.bjt.routevalidator.Validator;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben.Taylor on 13/05/2016.
 */
public class FeedbackTest {
    @Test
    public void trackWithOnlyOneTrackpointDoesntThrow() throws Exception {
        final GpxFile actual = getGpxFile("/AcceptableWithSinglePointTrack.gpx");
        final GpxFile intended = getGpxFile("/Intended.gpx");
        final ClimbingServerUrlProvider climbingServerUrlProvider = new HardcodedClimbingServerUrlProvider();
        final Validator validator = new Validator(climbingServerUrlProvider);
        final List<TrackUsePreference> trackUsePreferences = TrackUsePreference.getDefault(actual.getGpx());
        validator.validate(intended,actual, 200, trackUsePreferences);
    }

    private GpxFile getGpxFile(String name) throws Exception {
        final InputStream inputStream = getClass().getResourceAsStream(name);
        final GeoFileParser gpxparser = new GeoFileParser();
        return new GpxFile(name, gpxparser.parseGeoFile(inputStream, name));
    }
}
