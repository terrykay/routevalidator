package com.bjt.routevalidator;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by Ben.Taylor on 25/10/2015.
 */
public class ValidateServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ValidateServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doPost(req, resp);
        final ServletFileUpload servletFileUpload = new ServletFileUpload();


    try {
        final FileItemIterator itemIterator = servletFileUpload.getItemIterator(req);
        while(itemIterator.hasNext()) {
            final FileItemStream file = itemIterator.next();
            logger.info("Uploaded " + file.getContentType() + " to " + file.getFieldName());
        }
        req.getRequestDispatcher("/result.jsp").include(req, resp);


    } catch (FileUploadException e) {
        logger.throwing(ValidateServlet.class.getName(), "doPost", e);
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        e.printStackTrace(resp.getWriter());
    }
}
}
