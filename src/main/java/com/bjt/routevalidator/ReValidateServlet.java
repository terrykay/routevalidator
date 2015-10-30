package com.bjt.routevalidator;

import com.bjt.gpxparser.Gpx;
import com.bjt.gpxparser.GpxParser;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
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
public class ReValidateServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ReValidateServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final Result lastResult = (Result) req.getSession().getAttribute("result");
            final Validator validator = new Validator();
            final int newTolerance = Integer.parseInt(req.getParameter("tolerance"));
            final Result result = validator.validate(lastResult.getIntendedGpx(), lastResult.getActualGpx(), newTolerance);
            req.getSession().setAttribute("result", result);
            req.setAttribute("result", result);
            req.getRequestDispatcher("/result.jsp").include(req, resp);
        } catch (Exception e) {
            e.printStackTrace(resp.getWriter());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
