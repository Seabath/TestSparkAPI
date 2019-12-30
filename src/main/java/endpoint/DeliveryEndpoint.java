package endpoint;

import com.beerboy.ss.SparkSwagger;
import static com.beerboy.ss.descriptor.EndpointDescriptor.endpointPath;
import com.beerboy.ss.descriptor.MethodDescriptor;
import com.beerboy.ss.rest.Endpoint;
import com.google.gson.Gson;
import factory.DeliveryFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojo.delivery.NewDeliveryRequest;
import pojo.delivery.NewDeliveryResponse;
import pojo.entity.DeliveryEntity;
import spark.Request;
import spark.Response;

public class DeliveryEndpoint implements Endpoint {

    private static final Logger logger = LogManager.getLogger(DeliveryEndpoint.class);

    public static final String ENDPOINT_PATH = "/delivery";

    @Override
    public void bind(SparkSwagger sparkSwagger) {
        sparkSwagger.endpoint(endpointPath(ENDPOINT_PATH), (q, a) -> logger.info("Received request for delivery."))

            .post(MethodDescriptor.path("")
                    .withRequestType(NewDeliveryRequest.class)
                    .withResponseType(NewDeliveryResponse.class)
                    .withDescription("Creates a new delivery.")
                , this::createDelivery);
    }

    private String createDelivery(Request request, Response response) {
        NewDeliveryRequest newDeliveryRequest = new Gson().fromJson(request.body(), NewDeliveryRequest.class);

        DeliveryEntity entity = DeliveryFactory.build(newDeliveryRequest);

        NewDeliveryResponse newDeliveryResponse = DeliveryFactory.build(entity);

        response.status(201);
        return new Gson().toJson(newDeliveryResponse);
    }
}
