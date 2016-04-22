import com.bjt.routevalidator.ClimbServerResult;
import com.bjt.routevalidator.ClimbingStatistic;
import com.bjt.routevalidator.DurationStatistic;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vividsolutions.jts.geom.Coordinate;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ben.Taylor on 27/03/2016.
 */
public class DurationStatisticTest {
    @Test
    public void tcxTime() {
        String tcxTime1 = "2010-01-01T08:00:00Z";
        String tcxTime2 = "2010-01-01T21:27:00Z";
        String duration = DurationStatistic.getDuration(tcxTime1, tcxTime2);
        Assert.assertEquals("13:27", duration);
    }

    @Test
    public void oneHourOneMinute() {
        String tcxTime1 = "2010-01-01T08:00:00Z";
        String tcxTime2 = "2010-01-01T09:01:00Z";
        String duration = DurationStatistic.getDuration(tcxTime1, tcxTime2);
        Assert.assertEquals("01:01", duration);
    }
}
