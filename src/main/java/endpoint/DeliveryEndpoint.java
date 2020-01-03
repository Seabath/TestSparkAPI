package endpoint;

import com.beerboy.ss.SparkSwagger;
import static com.beerboy.ss.descriptor.EndpointDescriptor.endpointPath;
import com.beerboy.ss.descriptor.MethodDescriptor;
import com.beerboy.ss.rest.Endpoint;
import com.google.gson.Gson;
import dao.SimpleDAO;
import factory.DeliveryFactory;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojo.delivery.PostDeliveryRequest;
import pojo.delivery.GetDeliveryResponse;
import pojo.entity.DeliveryEntity;
import service.SimpleService;
import spark.Request;
import spark.Response;

public class DeliveryEndpoint implements Endpoint {

    private static final Logger logger = LogManager.getLogger(DeliveryEndpoint.class);

    public static final String DELIVERY_ENDPOINT_ROUTE = "/delivery";
    private static final String PARAM_ID = ":id";

    private final SimpleService<DeliveryEntity> deliveryService;

    public DeliveryEndpoint() {
        deliveryService = new SimpleService<>(new SimpleDAO<>(DeliveryEntity.class));
    }

    @Override
    public void bind(SparkSwagger sparkSwagger) {
        sparkSwagger.endpoint(endpointPath(DELIVERY_ENDPOINT_ROUTE), (q, a) -> logger.info("Received request for delivery:\nPath: {}\nBody: {}", q.pathInfo(), q.body()))

            .get(MethodDescriptor.path("/" + PARAM_ID)
                .withResponseType(GetDeliveryResponse.class), this::getDelivery)

            .post(MethodDescriptor.path("")
                    .withRequestType(PostDeliveryRequest.class)
                    .withResponseType(GetDeliveryResponse.class)
                    .withDescription("Creates a new delivery.")
                , this::createDelivery);
    }

    @SneakyThrows
    private String getDelivery(Request request, Response response) {
        final long id = Long.parseLong(request.params(PARAM_ID));

        final DeliveryEntity deliveryEntity = deliveryService.get(id);
        if (deliveryEntity == null) {
            throw new NotFoundException("Delivery");
        }

        final GetDeliveryResponse getDeliveryResponse = DeliveryFactory.build(deliveryEntity);
        return new Gson().toJson(getDeliveryResponse);
    }

    private String createDelivery(Request request, Response response) {
        PostDeliveryRequest postDeliveryRequest = new Gson().fromJson(request.body(), PostDeliveryRequest.class);

        DeliveryEntity entity = DeliveryFactory.build(postDeliveryRequest);
        deliveryService.create(entity);

        GetDeliveryResponse getDeliveryResponse = DeliveryFactory.build(entity);

        response.status(201);
        return new Gson().toJson(getDeliveryResponse);
    }
}
