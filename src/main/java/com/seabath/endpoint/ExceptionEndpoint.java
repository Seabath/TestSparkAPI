package com.seabath.endpoint;

import com.beerboy.ss.SparkSwagger;
import com.beerboy.ss.rest.Endpoint;
import com.google.gson.Gson;
import javassist.NotFoundException;
import spark.Request;
import spark.Response;
import spark.Service;

/**
 * Endpoint that handles all exceptions thrown by services.
 */
public class ExceptionEndpoint implements Endpoint {


    @Override
    public void bind(SparkSwagger sparkSwagger) {
        final Service spark = sparkSwagger.getSpark();

        spark.exception(NotFoundException.class, this::handleNotFound);
        spark.exception(Exception.class, this::handleDefaultException);
    }


    /**
     * Handles exception when an entity is not found.
     *
     * @param t        NotFoundException thrown
     * @param request  Request java object.
     * @param response Response java object.
     */
    private void handleNotFound(NotFoundException t, Request request, Response response) {
        response.status(404);
        response.body(new Gson().toJson(t.getMessage() + " not found."));
    }

    /**
     * Handles default exception when the API fall into a not handled exception.
     * Or if there is a logic problem within an API.
     *
     * @param t        Exception thrown
     * @param request  Request java object.
     * @param response Response java object.
     */
    private void handleDefaultException(Exception t, Request request, Response response) {
        response.status(500);
        response.body(new Gson().toJson(t));
    }
}
