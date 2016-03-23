package com.bjt.routevalidator;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
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
            req.getRequestDispatcher("/index.jsp").include(req, resp);
        } catch (Exception e) {
            ErrorHandler.handleError("Error revalidating", e, req, resp);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
