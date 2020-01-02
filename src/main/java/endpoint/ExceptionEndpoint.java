package endpoint;

import com.beerboy.ss.SparkSwagger;
import com.beerboy.ss.rest.Endpoint;
import com.google.gson.Gson;
import javassist.NotFoundException;
import spark.Request;
import spark.Response;
import spark.Service;

public class ExceptionEndpoint implements Endpoint {


    @Override
    public void bind(SparkSwagger sparkSwagger) {
        final Service spark = sparkSwagger.getSpark();

        spark.exception(NotFoundException.class, this::handleNotFound);
    }

    private void handleNotFound(NotFoundException t, Request request, Response response) {
        response.status(404);
        response.body(t.getMessage() + " not found.");
    }
}
