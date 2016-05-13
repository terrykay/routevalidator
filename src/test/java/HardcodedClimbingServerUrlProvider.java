import com.bjt.routevalidator.ClimbingServerUrlProvider;

/**
 * Created by Ben.Taylor on 13/05/2016.
 */
public class HardcodedClimbingServerUrlProvider implements ClimbingServerUrlProvider {
    @Override
    public String getClimbingServerUrl() {
        return "http://52.25.237.100:8080/hdsrvgatewayjson/climbdata";
    }
}
