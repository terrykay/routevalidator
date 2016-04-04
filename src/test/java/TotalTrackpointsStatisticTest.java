import com.bjt.routevalidator.StartElevationStatistic;
import com.bjt.routevalidator.TotalTrackpointsStatistic;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Ben.Taylor on 27/03/2016.
 */
public class TotalTrackpointsStatisticTest {
    @Test
    public void count1() {
        Assert.assertEquals("1", TotalTrackpointsStatistic.formatTotalTrackpointsInFile(1));
    }

    @Test
    public void count2() {
        Assert.assertEquals("3,412", TotalTrackpointsStatistic.formatTotalTrackpointsInFile(3412));
    }
}
