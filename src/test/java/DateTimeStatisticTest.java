import com.bjt.routevalidator.DurationStatistic;
import com.bjt.routevalidator.StartDateTimeStatistic;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Ben.Taylor on 27/03/2016.
 */
public class DateTimeStatisticTest {
    @Test
    public void tcxTime() {
        String trackpointTime = "2016-04-04T11:06:00Z";
        Assert.assertEquals("Mon 04/04/16 11:06", StartDateTimeStatistic.reformatTime(trackpointTime));
    }
}
