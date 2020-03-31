package endpoint;

import com.beerboy.ss.SparkSwagger;
import static com.beerboy.ss.descriptor.EndpointDescriptor.endpointPath;
import com.beerboy.ss.descriptor.MethodDescriptor;
import com.beerboy.ss.rest.Endpoint;
import com.google.gson.Gson;
import factory.DeliveryFactory;
import filter.DoNothingFilter;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import pojo.delivery.GetDeliveryResponse;
import pojo.delivery.PostDeliveryRequest;
import pojo.entity.DeliveryEntity;
import service.SimpleService;
import spark.Request;
import spark.Response;

/**
 * Endpoint that handle every thing related to delivery entity.
 */
public class DeliveryEndpoint implements Endpoint {

    public static final String DELIVERY_ENDPOINT_ROUTE = "/delivery";
    private static final String PARAM_ID = ":id";

    private final SimpleService<DeliveryEntity> deliveryService;

    public DeliveryEndpoint(SimpleService<DeliveryEntity> deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Override
    public void bind(SparkSwagger sparkSwagger) {
        sparkSwagger.endpoint(endpointPath(DELIVERY_ENDPOINT_ROUTE), new DoNothingFilter())

            .get(MethodDescriptor.path("/" + PARAM_ID)
                .withResponseType(GetDeliveryResponse.class), this::getDelivery)

            .post(MethodDescriptor.path("")
                    .withRequestType(PostDeliveryRequest.class)
                    .withResponseType(GetDeliveryResponse.class)
                    .withDescription("Creates a new delivery.")
                , this::createDelivery);
    }

    /**
     * Get a delivery by its ID.
     *
     * @param request  Request java object.
     * @param response Response java object.
     * @return Json formated GetDeliveryResponse object
     */
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

    /**
     * Create a delivery with request's body and stores it in database.
     *
     * @param request  Request of the request with a PostDeliveryRequest as json in its body.
     * @param response Response of the request
     * @return Json formated GetDeliveryResponse object
     */
    private String createDelivery(Request request, Response response) {
        PostDeliveryRequest postDeliveryRequest = new Gson().fromJson(request.body(), PostDeliveryRequest.class);

        DeliveryEntity entity = DeliveryFactory.build(postDeliveryRequest);
        deliveryService.create(entity);

        GetDeliveryResponse getDeliveryResponse = DeliveryFactory.build(entity);

        response.status(201);
        return new Gson().toJson(getDeliveryResponse);
    }
}
