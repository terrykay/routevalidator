package com.bjt.routevalidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Ben.Taylor on 24/04/2016.
 */
public class DownloadWorkingsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Result lastResult = (Result) req.getSession().getAttribute("result");
        if(lastResult != null) {
            resp.setContentType("text/csv");
            resp.setHeader("Content-Disposition", "attachment;filename=workings.csv");
            lastResult.getTrackSummary().writeWorkings(resp.getWriter());
        }

    }
}
