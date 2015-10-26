package com.bjt.routevalidator;

import com.bjt.gpxparser.Gpx;
import com.bjt.gpxparser.GpxParser;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by Ben.Taylor on 25/10/2015.
 */
public class ValidateServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ValidateServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doPost(req, resp);
        final ServletFileUpload servletFileUpload = new ServletFileUpload();

        try {
            final FileItemIterator itemIterator = servletFileUpload.getItemIterator(req);
            Gpx intendedGpx = null;
            Gpx actualGpx = null;
            final GpxParser gpxParser = new GpxParser();
            while (itemIterator.hasNext()) {
                final FileItemStream file = itemIterator.next();
                if(file.getFieldName().equals("intended")) {
                    intendedGpx = readGpx(file, gpxParser);
                }
                if(file.getFieldName().equals("actual")) {
                    actualGpx = readGpx(file, gpxParser);
                }
            }
            if(intendedGpx == null || actualGpx == null) {
                ErrorHandler.handleError("Both intended and actual gpx files must be uploaded.", null, req, resp);
            } else {

            }
            req.getRequestDispatcher("/result.jsp").include(req, resp);


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
