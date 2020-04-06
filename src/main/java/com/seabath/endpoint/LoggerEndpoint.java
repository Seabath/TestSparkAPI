package com.seabath.endpoint;

import com.beerboy.ss.SparkSwagger;
import com.beerboy.ss.rest.Endpoint;
import com.seabath.filter.AfterAfterRouteFilter;
import com.seabath.filter.BeforeRouteFilter;
import org.apache.logging.log4j.Logger;

public class LoggerEndpoint implements Endpoint {


    private final Logger logger;

    public LoggerEndpoint(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void bind(SparkSwagger sparkSwagger) {
        sparkSwagger.before(new BeforeRouteFilter(logger));
        sparkSwagger.after(new AfterAfterRouteFilter(logger));
    }
}
