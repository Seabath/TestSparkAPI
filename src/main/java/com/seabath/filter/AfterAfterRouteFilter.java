package com.seabath.filter;

import org.apache.logging.log4j.Logger;
import spark.Filter;
import spark.Request;
import spark.Response;

/**
 * Filter for afterAfter spark's method
 */
public class AfterAfterRouteFilter implements Filter {

    private final Logger logger;

    public AfterAfterRouteFilter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void handle(Request request, Response response) {
        final String formatedLog = String.format("""
                Response for %s at route %s
                Request body: %s
                Response status: %s
                Response body: %s""", request.requestMethod(),
            request.pathInfo(), request.body(), response.status(), response.body());

        logger.info(formatedLog);
    }
}
