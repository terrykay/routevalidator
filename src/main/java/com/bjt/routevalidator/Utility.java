package com.bjt.routevalidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ben.Taylor on 27/04/2016.
 */
public class Utility {
    private static final Pattern pattern = Pattern.compile("/[^/]+$");
    public static String urlToShortFileName(final String url) {
        final Matcher matcher = pattern.matcher(url);
        if(matcher.find()) {
            return "..." + matcher.group(0);
        }
        return url;
    }
}
