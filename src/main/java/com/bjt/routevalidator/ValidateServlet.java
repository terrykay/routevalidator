package com.bjt.routevalidator;

import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.GeoFileParser;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.taglibs.standard.tag.common.core.Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ben.Taylor on 25/10/2015.
 */
public class ValidateServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ValidateServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long start = System.currentTimeMillis();
            final String actual = req.getParameter("actual");
            final String intended = req.getParameter("intended");
            if (actual == null || actual.isEmpty()) throw new Exception("Actual URL must be specified.");
            if (intended == null || intended.isEmpty()) throw new Exception("Actual URL must be specified.");
            final String toleranceString = req.getParameter("tolerance");
            final int tolerance = toleranceString != null && !toleranceString.isEmpty() ? Integer.parseInt(toleranceString) : Result.DEFAULT_TOLERANCE;

            final GeoFileParser geoFileParser = new GeoFileParser();
            final HttpGet actualGet = new HttpGet(actual);
            final HttpGet intendedGet = new HttpGet(intended);
            try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                 final CloseableHttpResponse actualResponse = httpClient.execute(actualGet);
                 final CloseableHttpResponse intendedResponse = httpClient.execute(intendedGet)) {
                final GpxFile actualGpxFile;
                final GpxFile intendedGpxFile;

                try {
                    actualGpxFile = new GpxFile(actual, geoFileParser.parseGeoFile(actualResponse.getEntity().getContent(), actual));
                } catch (Exception ex) {
                    ErrorHandler.handleError("The actual GPX file could not be parsed. Please check the URL points to the GPX file itself (not merely a page containing it).", ex, req, resp);
                    return;
                }
                try {
                    intendedGpxFile = new GpxFile(intended, geoFileParser.parseGeoFile(intendedResponse.getEntity().getContent(), intended));
                } catch (Exception ex) {
                    ErrorHandler.handleError("The intended GPX file could not be parsed. Please check the URL points to the GPX file itself (not merely a page containing it).", ex, req, resp);
                    return;
                }

                final ClimbingServerUrlProvider climbingServerUrlProvider = new ConfigClimbingServerUrlProvider(getServletContext());
                final Validator validator = new Validator(climbingServerUrlProvider);
                final List<? extends TrackUsePreference> trackUsePreferences = TrackUsePreference.getDefault(actualGpxFile.getGpx());

                final Result result = validator.validate(intendedGpxFile, actualGpxFile, tolerance, trackUsePreferences);
                req.setAttribute("taken", (System.currentTimeMillis()-start));
                result.setIsUrlLoaded(true);
                req.getSession().setAttribute("result", result);
                req.setAttribute("result", result);
                req.getRequestDispatcher("/index.jsp").include(req, resp);
            }
        } catch (final Exception ex) {
            logger.throwing(ValidateServlet.class.getName(), "doGet", ex);
            ErrorHandler.handleError("The URL for one of the files did not work.", ex, req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        final ServletFileUpload servletFileUpload = new ServletFileUpload();

        logger.info("ValidateServlet.doPost working!");
        try {
            final FileItemIterator itemIterator = servletFileUpload.getItemIterator(req);
            GpxFile intendedGpxFile = null;
            GpxFile actualGpxFile = null;
            final GeoFileParser geoFileParser = new GeoFileParser();
            Integer tolerance = null;
            while (itemIterator.hasNext()) {
                final FileItemStream file = itemIterator.next();

                if (file.getFieldName().equals("intended")) {
                    final GeoFile intendedGpx;
                    try {
                        intendedGpx = readGpx(file, geoFileParser, file.getName());
                    } catch (final Exception ex) {
                        throw new FriendlyException("The intended GPX file was invalid.");
                    }
                    final String intendedFilename = file.getName();
                    intendedGpxFile = new GpxFile(intendedFilename, intendedGpx);
                }
                if (file.getFieldName().equals("actual")) {
                    final GeoFile actualGpx;
                    try {
                        actualGpx = readGpx(file, geoFileParser, file.getName());
                    } catch (final Exception ex) {
                        throw new FriendlyException("The actual GPX file was invalid.");
                    }
                    final String actualFilename = file.getName();
                    actualGpxFile = new GpxFile(actualFilename, actualGpx);
                }
                if (file.getFieldName().equals("tolerance") && file.isFormField()) {
                    try (final InputStream stream = file.openStream()) {
                        final String toleranceString = Streams.asString(stream);
                        tolerance = Integer.parseInt(toleranceString);
                    }
                }
            }
            if (intendedGpxFile == null || actualGpxFile == null) {

                logger.log(Level.SEVERE, "Both intended and actual gpx files must be uploaded.", (Exception) null);
            } else if (tolerance == null) {

                logger.log(Level.SEVERE, "Tolerance must be specified.", (Exception) null);
            } else {
                final ClimbingServerUrlProvider climbingServerUrlProvider = new ConfigClimbingServerUrlProvider(getServletContext());
                final Validator validator = new Validator(climbingServerUrlProvider);
                final List<? extends TrackUsePreference> trackUsePreferences = TrackUsePreference.getDefault(actualGpxFile.getGpx());
                final Result result = validator.validate(intendedGpxFile, actualGpxFile, tolerance, trackUsePreferences);
                req.setAttribute("taken", (System.currentTimeMillis()-start));
                req.getSession().setAttribute("result", result);
                req.setAttribute("result", result);
                req.getRequestDispatcher("/index.jsp").include(req, resp);
            }
        } catch (final FriendlyException e) {
            req.getSession().setAttribute("FriendlyErrorMessage", e.getMessage());
            resp.sendRedirect("/");
        } catch (final Exception e) {
            ErrorHandler.handleError("There was an error processing the GPX files: " + e.getMessage(), e, req, resp);
        }
    }

    private static GeoFile readGpx(final FileItemStream file, final GeoFileParser geoFileParser, final String fileName) throws Exception {
        try (final InputStream inputStream = file.openStream()) {
            final GeoFile geoFile = geoFileParser.parseGeoFile(inputStream, fileName);
            return geoFile;
        }
    }
}
