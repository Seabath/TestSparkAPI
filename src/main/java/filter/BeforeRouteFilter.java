package filter;

import org.apache.logging.log4j.Logger;
import spark.Filter;
import spark.Request;
import spark.Response;

/**
 * Filter for before spark's method
 */
public class BeforeRouteFilter implements Filter {

    private final Logger logger;

    public BeforeRouteFilter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void handle(Request request, Response response) {
        final String formatedLog = String.format("Request %s received:\nRoute %s\nBody: %s",
            request.requestMethod(), request.pathInfo(), request.body());

        logger.info(formatedLog);
    }
}
