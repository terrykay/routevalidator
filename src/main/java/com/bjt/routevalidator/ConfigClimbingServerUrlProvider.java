package com.bjt.routevalidator;

import com.bjt.routevalidator.ClimbingServerUrlProvider;

import javax.servlet.ServletContext;

public class ConfigClimbingServerUrlProvider implements ClimbingServerUrlProvider {
    private final ServletContext servletContext;

    public ConfigClimbingServerUrlProvider(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public String getClimbingServerUrl() {
        return servletContext.getInitParameter("ClimbingServerUrl");
    }
}
