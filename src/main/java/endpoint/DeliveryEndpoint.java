package endpoint;

import com.beerboy.ss.SparkSwagger;
import static com.beerboy.ss.descriptor.EndpointDescriptor.endpointPath;
import com.beerboy.ss.descriptor.MethodDescriptor;
import com.beerboy.ss.rest.Endpoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.SimpleDAO;
import factory.DeliveryFactory;
import java.text.DateFormat;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojo.delivery.NewDeliveryRequest;
import pojo.delivery.NewDeliveryResponse;
import pojo.entity.DeliveryEntity;
import service.SimpleService;
import spark.Request;
import spark.Response;

public class DeliveryEndpoint implements Endpoint {

    private static final Logger logger = LogManager.getLogger(DeliveryEndpoint.class);

    public static final String ENDPOINT_PATH = "/delivery";
    public static final String PARAM_ID = ":id";
    private final SimpleService<DeliveryEntity> deliveryService;

    public DeliveryEndpoint() {
        deliveryService = new SimpleService<>(new SimpleDAO<>(DeliveryEntity.class));
    }

    @Override
    public void bind(SparkSwagger sparkSwagger) {
        sparkSwagger.endpoint(endpointPath(ENDPOINT_PATH), (q, a) -> logger.info("Received request for delivery."))

            .post(MethodDescriptor.path("")
                    .withRequestType(NewDeliveryRequest.class)
                    .withResponseType(NewDeliveryResponse.class)
                    .withDescription("Creates a new delivery.")
                , this::createDelivery)
            .get(MethodDescriptor.path("/" + PARAM_ID)
                .withResponseType(NewDeliveryResponse.class), this::getDelivery);
    }

    @SneakyThrows
    private String getDelivery(Request request, Response response) {
        final long id = Long.parseLong(request.params(PARAM_ID));

        final DeliveryEntity deliveryEntity = deliveryService.get(id);
        if (deliveryEntity == null) {
            throw new NotFoundException("Delivery");
        }

        final NewDeliveryResponse newDeliveryResponse = DeliveryFactory.build(deliveryEntity);
        return new Gson().toJson(newDeliveryResponse);
    }

    private String createDelivery(Request request, Response response) {
        NewDeliveryRequest newDeliveryRequest = new Gson().fromJson(request.body(), NewDeliveryRequest.class);

        DeliveryEntity entity = DeliveryFactory.build(newDeliveryRequest);
        deliveryService.create(entity);

        NewDeliveryResponse newDeliveryResponse = DeliveryFactory.build(entity);

        response.status(201);
        return new Gson().toJson(newDeliveryResponse);
    }
}
