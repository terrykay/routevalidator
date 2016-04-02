package com.bjt.routevalidator;

import com.bjt.gpxparser.GeoFile;
import com.bjt.gpxparser.GeoFileParser;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Ben.Taylor on 25/10/2015.
 */
public class ValidateServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ValidateServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                    }catch(final Exception ex) {
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
                ErrorHandler.handleError("Both intended and actual gpx files must be uploaded.", null, req, resp);
            } else if (tolerance == null) {
                ErrorHandler.handleError("Tolerance must be specified.", null, req, resp);
            } else {
                final Validator validator = new Validator(getServletContext());
                final List<? extends TrackUsePreference> trackUsePreferences = TrackUsePreference.getDefault(actualGpxFile.getGpx());
                final Result result = validator.validate(intendedGpxFile, actualGpxFile, tolerance, trackUsePreferences);
                req.getSession().setAttribute("result", result);
                req.setAttribute("result", result);
                req.getRequestDispatcher("/index.jsp").include(req, resp);
            }
        } catch (final FriendlyException e) {
            req.getSession().setAttribute("FriendlyErrorMessage", e.getMessage());
            resp.sendRedirect("/");
        } catch (final Exception e) {
            ErrorHandler.handleError("There was an error processing the GPX files.", e, req, resp);
        }
    }

    private static GeoFile readGpx(final FileItemStream file, final GeoFileParser geoFileParser, final String fileName) throws Exception {
        try (final InputStream inputStream = file.openStream()) {
            final GeoFile geoFile = geoFileParser.parseGeoFile(inputStream, fileName);
            return geoFile;
        }
    }
}
