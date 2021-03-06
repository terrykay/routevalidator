package com.bjt.routevalidator;


import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
            final ClimbingServerUrlProvider climbingServerUrlProvider = new ConfigClimbingServerUrlProvider(getServletContext());
            final Validator validator = new Validator(climbingServerUrlProvider);
            final int newTolerance = Integer.parseInt(req.getParameter("tolerance"));
            final List<TrackUsePreference> trackUsePreferences = parseTrackUsePreferences(req);
            final Result result = validator.validate(lastResult.getIntendedGpx(), lastResult.getActualGpx(), newTolerance, trackUsePreferences);
            req.getSession().setAttribute("result", result);
            req.setAttribute("result", result);
            req.getRequestDispatcher("/index.jsp").include(req, resp);
        } catch (Exception e) {
            ErrorHandler.handleError("Error revalidating", e, req, resp);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private List<TrackUsePreference> parseTrackUsePreferences(HttpServletRequest req) throws FactoryException, TransformException {
        String trackName;
        Integer i = 0;
        final List<TrackUsePreference> trackUsePreferences = new ArrayList<>();
        while((trackName = req.getParameter("trackusepreference_name_" + i.toString())) != null) {
            final boolean checked = "on".equalsIgnoreCase(req.getParameter("trackusepreference_checked_" + i.toString()));
            final TrackUsePreference trackUsePreference = new TrackUsePreference(trackName, checked);
            trackUsePreferences.add(trackUsePreference);
            i++;
        }

        return trackUsePreferences;
    }
}
