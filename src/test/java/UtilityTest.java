import com.bjt.routevalidator.Utility;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Ben.Taylor on 27/04/2016.
 */
public class UtilityTest {
    @Test
    public void chopUrl() {
        Assert.assertEquals(".../Acceptable.gpx", Utility.urlToShortFileName("https://dl.dropboxusercontent.com/u/55634526/Acceptable.gpx"));
    }
    @Test
    public void chopUrl2() {
        Assert.assertEquals(".../Acceptable.gpx", Utility.urlToShortFileName("http://Acceptable.gpx"));

    }

    @Test
    public void encodeMailto() throws UnsupportedEncodingException, URIException {
        System.out.println(URIUtil.encodeQuery("AAA Validation for DIY"));
    }
}
