import com.bjt.routevalidator.DurationStatistic;
import com.bjt.routevalidator.StartElevationStatistic;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Ben.Taylor on 27/03/2016.
 */
public class ElevationStatisticTest {
    @Test
    public void elevation1() {
        Assert.assertEquals("41 m", StartElevationStatistic.formatElevation(41d));
    }

    @Test
    public void elevation2() {
        Assert.assertEquals("3,412 m", StartElevationStatistic.formatElevation(3412.12));
    }
}
