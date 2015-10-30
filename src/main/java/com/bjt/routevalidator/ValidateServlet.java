package com.bjt.routevalidator;

import com.bjt.gpxparser.Gpx;
import com.bjt.gpxparser.GpxParser;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Created by Ben.Taylor on 25/10/2015.
 */
public class ValidateServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ValidateServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final ServletFileUpload servletFileUpload = new ServletFileUpload();

        try {
            final FileItemIterator itemIterator = servletFileUpload.getItemIterator(req);
            GpxFile intendedGpxFile = null;
            GpxFile actualGpxFile = null;
            final GpxParser gpxParser = new GpxParser();
            Integer tolerance = null;
            while (itemIterator.hasNext()) {
                final FileItemStream file = itemIterator.next();
                if(file.getFieldName().equals("intended")) {
                    final Gpx intendedGpx = readGpx(file, gpxParser);
                    final String intendedFilename = file.getName();
                    intendedGpxFile = new GpxFile(intendedFilename, intendedGpx);
                }
                if(file.getFieldName().equals("actual")) {
                    final Gpx actualGpx = readGpx(file, gpxParser);
                    final String actualFilename = file.getName();
                    actualGpxFile = new GpxFile(actualFilename, actualGpx);
                }
                if(file.getFieldName().equals("tolerance") && file.isFormField()) {
                    try(final InputStream stream = file.openStream()) {
                        final String toleranceString = Streams.asString(stream);
                        tolerance = Integer.parseInt(toleranceString);
                    }
                }
            }
            if(intendedGpxFile == null || actualGpxFile == null) {
                ErrorHandler.handleError("Both intended and actual gpx files must be uploaded.", null, req, resp);
            }else if (tolerance == null) {
                ErrorHandler.handleError("Tolerance must be specified.", null, req, resp);
            } else {
                final Validator validator = new Validator();
                final Result result = validator.validate(intendedGpxFile, actualGpxFile, tolerance);
                req.getSession().setAttribute("result", result);
                req.setAttribute("result", result);
                req.getRequestDispatcher("/result.jsp").include(req, resp);
            }
        } catch (Exception e) {
            ErrorHandler.handleError("There was an error processing the GPX files.", e, req, resp);
        }
    }
    private static Gpx readGpx(final FileItemStream file, final GpxParser gpxParser) throws Exception {
        try(final InputStream inputStream = file.openStream()) {
            return gpxParser.parseGpx(inputStream);
        }
    }
}
