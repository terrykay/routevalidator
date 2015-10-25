package com.bjt.routevalidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Ben.Taylor on 25/10/2015.
 */
public class ErrorHandler {
    public static void handleError(final String text,
                              final Exception exception,
                              final HttpServletRequest req,
                              final HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("error", text);
        req.setAttribute("exception", exception);
        req.getRequestDispatcher("/error.jsp").forward(req, resp);
    }
}
