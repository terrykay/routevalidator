package com.bjt.routevalidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by Ben.Taylor on 25/10/2015.
 */
public class ErrorHandler {
    private static Logger logger = Logger.getLogger(ErrorHandler.class.getName());

    public static void handleError(final String text,
                              final Exception exception,
                              final HttpServletRequest req,
                              final HttpServletResponse resp) throws ServletException, IOException {

        logger.info("Logging an error!");
        logger.log(Level.SEVERE, text, exception);
        req.setAttribute("error", text);
        req.setAttribute("exception", exception);
        req.getRequestDispatcher("/error.jsp").forward(req, resp);
    }
}
