package com.seabath.filter;

import spark.Filter;
import spark.Request;
import spark.Response;

/**
 * Filter that does nothing.
 */
public class DoNothingFilter implements Filter {
    @Override
    public void handle(Request request, Response response) {

    }
}
