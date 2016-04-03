import com.bjt.routevalidator.ClimbServerResult;
import com.bjt.routevalidator.ClimbingStatistic;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vividsolutions.jts.geom.Coordinate;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ben.Taylor on 27/03/2016.
 */
public class ClimbServerTest {
    @Test
    public void jsonTest() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();
        System.out.println(gson.toJson(new ClimbServerResult(true, 42)));

        System.out.println(String.format("%,.2f km", 1234.567));
        System.out.println(String.format("%,.2f km", 0.123456789));


        System.out.println(StringEscapeUtils.escapeJavaScript("The 'actual' file did not look very \"actual\" to me."));
    }

    //@Test
/*
    public void getClimbing() throws IOException {
        final List<List<Coordinate>> paths =Arrays.asList(
                Arrays.asList(
                        new Coordinate(-1.47507, 53.247),
                        new Coordinate(-1.5357, 53.2352)
                        )
        );
        final ClimbServerResult climbing = ClimbingStatistic.getClimbing(paths, "http://52.25.237.100:8080/hdsrvgatewayjson/climbdata");

        Assert.assertTrue(climbing.isSuccess());
        Assert.assertTrue(climbing.getClimbing() > 0);
        System.out.println(String.format("Climbing: %d", climbing.getClimbing()));

    }


    @Test
    public void noExceptionForWrongServer1() throws IOException {
        final List<List<Coordinate>> paths =Arrays.asList(
                Arrays.asList(
                        new Coordinate(-1.47507, 53.247),
                        new Coordinate(-1.5357, 53.2352)
                        )
        );
        final ClimbServerResult climbing = ClimbingStatistic.getClimbing(paths, "http://blah/guff");

        Assert.assertFalse(climbing.isSuccess());
        Assert.assertTrue(climbing.getClimbing() == 0);

    }
    @Test
    public void noExceptionForWrongServer2() throws IOException {
        final List<List<Coordinate>> paths =Arrays.asList(
                Arrays.asList(
                        new Coordinate(-1.47507, 53.247),
                        new Coordinate(-1.5357, 53.2352)
                        )
        );
        final ClimbServerResult climbing = ClimbingStatistic.getClimbing(paths, "http://52.25.237.100:8080/hdsrvgatewayjson/unknownclimbdata");

        Assert.assertFalse(climbing.isSuccess());
        Assert.assertTrue(climbing.getClimbing() == 0);

    }
    */
}
