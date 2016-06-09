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
    @Test
    public void twentyFiveHours() {
        String tcxTime1 = "2010-01-01T08:00:00Z";
        String tcxTime2 = "2010-01-02T09:01:00Z";
        String duration = DurationStatistic.getDuration(tcxTime1, tcxTime2);
        Assert.assertEquals("25:01", duration);
    }

    @Test
    public void oneWeekApart() {
        String tcxTime1 = "2010-01-01T00:00:00Z";
        String tcxTime2 = "2010-01-08T00:00:00Z";
        String duration = DurationStatistic.getDuration(tcxTime1, tcxTime2);
        Assert.assertEquals("168:00", duration);

    }
    @Test
    public void threeWeeksApart() { //should be ok
        String tcxTime1 = "2010-01-01T00:00:00Z";
        String tcxTime2 = "2010-01-22T00:00:00Z";
        String duration = DurationStatistic.getDuration(tcxTime1, tcxTime2);
        Assert.assertEquals("504:00", duration);

    }

    @Test
    public void moreThanAMonthApart() { //shouldn't ever have to encounter this.. .but should throw meaningful exception rather than a silly one
        String tcxTime1 = "2010-01-01T00:00:00Z";
        String tcxTime2 = "2010-02-01T00:00:00Z";
        try {
            String duration = DurationStatistic.getDuration(tcxTime1, tcxTime2);
            Assert.fail("Exception was not thrown...duration was apparently " + duration);
        }catch(final Exception e) {
            Assert.assertEquals("Times in the actual track(s) were too far (more than a month) apart.", e.getMessage());
        }

    }
}
