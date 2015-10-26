import com.bjt.routevalidator.GeoHelper;
import com.vividsolutions.jts.geom.Coordinate;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import java.util.ArrayList;
import java.util.List;

public class GeoHelperTest {
    @Test
    public void distanceToStraightLine() throws FactoryException, TransformException {
        final List<Coordinate> line = new ArrayList<>();
        line.add(new Coordinate(-0.758576, 53.220906));
        line.add(new Coordinate(-0.759388, 53.223704));

        final Coordinate point = new Coordinate(-0.770701, 53.222083);

        final GeoHelper geoHelper = new GeoHelper();
        final double distance = geoHelper.lineToPoint(line, point);

        Assert.assertTrue(distance > 774);
        Assert.assertTrue(distance < 777);
    }

    @Test
    public void distanceToCompoundLine() throws FactoryException, TransformException {
        final List<Coordinate> line = new ArrayList<>();
        line.add(new Coordinate(-0.758576, 53.220906));
        line.add(new Coordinate(-0.759388, 53.223704));
        line.add(new Coordinate( -0.780983, 53.224823));

        final Coordinate point = new Coordinate(-0.770701, 53.222083);

        final GeoHelper geoHelper = new GeoHelper();
        final double distance = geoHelper.lineToPoint(line, point);

        Assert.assertTrue(distance > 242);
        Assert.assertTrue(distance < 246);
    }
}