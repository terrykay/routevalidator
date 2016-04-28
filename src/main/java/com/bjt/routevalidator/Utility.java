package com.bjt.routevalidator;

import java.io.*;
import java.util.logging.Logger;
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

    public static String getAAAEmailAddress() throws IOException {
        return getConfigItem("AAA_EMAIL");
    }

    public static String configFile = System.getenv("ROUTEVALIDATOR_CONF");
    public static String getConfigItem(final String key) throws IOException {
        Logger.getAnonymousLogger().info("configFile = " + configFile);
        if(configFile != null && !configFile.isEmpty()) {
            final File file = new File(configFile);
            if(file.exists()) {
                try(final FileInputStream fileInputStream = new FileInputStream(file);
                    final InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                    String line;
                    while((line = bufferedReader.readLine()) != null) {
                        final String[] parts = line.split("=");
                        if(parts.length == 2 && parts[0].equalsIgnoreCase(key)) {
                            return parts[1];
                        }
                    }
                }
            }
        }
        return null;
    }
}
