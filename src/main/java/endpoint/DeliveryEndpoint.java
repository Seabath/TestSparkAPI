package endpoint;

import com.beerboy.ss.SparkSwagger;
import static com.beerboy.ss.descriptor.EndpointDescriptor.endpointPath;
import com.beerboy.ss.descriptor.MethodDescriptor;
import com.beerboy.ss.rest.Endpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Request;
import spark.Response;

public class DeliveryEndpoint implements Endpoint {

    private static final Logger logger = LogManager.getLogger(DeliveryEndpoint.class);

    private static final String ENDPOINT_PATH = "delivery";

    @Override
    public void bind(SparkSwagger sparkSwagger) {
        sparkSwagger.endpoint(endpointPath(ENDPOINT_PATH), (q, a) -> logger.info("Received request for delivery."))
            // endpoint methods
            .post(MethodDescriptor.path("")
                .withDescription("Creates a new delivery.")
                , this::createDelivery);
    }

    private String createDelivery(Request request, Response response) {
        return null;
    }
}
