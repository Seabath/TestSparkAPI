package endpoint;

import com.beerboy.ss.SparkSwagger;
import static com.beerboy.ss.descriptor.EndpointDescriptor.endpointPath;
import com.beerboy.ss.descriptor.MethodDescriptor;
import com.beerboy.ss.rest.Endpoint;
import com.google.gson.Gson;
import factory.TestConfigurationFactory;
import filter.DoNothingFilter;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import pojo.entity.DeliveryEntity;
import pojo.entity.TestConfigurationEntity;
import pojo.test.configuration.GetTestConfigurationResponse;
import pojo.test.configuration.PostTestConfigurationRequest;
import service.SimpleService;
import spark.Request;
import spark.Response;

/**
 * Endpoint that handles configuration entity.
 */
public class TestConfigurationEndpoint implements Endpoint {

    public static final String TEST_CONFIGURATION_ENDPOINT_ROUTE = "/configuration";
    private static final String PARAM_ID = ":id";

    private final SimpleService<TestConfigurationEntity> testConfigurationService;
    private final SimpleService<DeliveryEntity> deliveryService;

    public TestConfigurationEndpoint(SimpleService<TestConfigurationEntity> testConfigurationService,
                                     SimpleService<DeliveryEntity> deliveryService) {
        this.testConfigurationService = testConfigurationService;
        this.deliveryService = deliveryService;
    }

    @Override
    public void bind(SparkSwagger sparkSwagger) {
        sparkSwagger.endpoint(endpointPath(TEST_CONFIGURATION_ENDPOINT_ROUTE), new DoNothingFilter())

            .get(MethodDescriptor.path("/" + PARAM_ID)
                .withResponseType(GetTestConfigurationResponse.class), this::getTestConfiguration)

            .post(MethodDescriptor.path("")
                    .withRequestType(PostTestConfigurationRequest.class)
                    .withResponseType(GetTestConfigurationResponse.class)
                    .withDescription("Creates a new test configuration.")
                , this::createTestConfiguration);

    }

    /**
     * Get a configuration by its ID.
     *
     * @param request  Request java object.
     * @param response Response java object.
     * @return Json formated GetTestConfigurationResponse object
     */
    @SneakyThrows
    private String getTestConfiguration(Request request, Response response) {
        final long id = Long.parseLong(request.params(PARAM_ID));

        final TestConfigurationEntity testConfigurationEntity = testConfigurationService.get(id);
        if (testConfigurationEntity == null) {
            throw new NotFoundException("Test Configuration");
        }

        final GetTestConfigurationResponse testConfigurationResponse = TestConfigurationFactory.build(testConfigurationEntity);
        return new Gson().toJson(testConfigurationResponse);
    }

    /**
     * Create a test configuration with request's body and store it into database.
     *
     * @param request  Request of the request with a PostTestConfigurationRequest as json in its
     *                 body.
     * @param response Response of the request
     * @return Json formated GetTestConfigurationResponse object
     */
    @SneakyThrows
    private String createTestConfiguration(Request request, Response response) {
        final PostTestConfigurationRequest postTestConfigurationRequest = new Gson().fromJson(request.body(), PostTestConfigurationRequest.class);

        final DeliveryEntity deliveryEntity = deliveryService.get(postTestConfigurationRequest.getDeliveryId());
        if (deliveryEntity == null) {
            throw new NotFoundException("Delivery");
        }

        TestConfigurationEntity configurationEntity = TestConfigurationFactory.build(postTestConfigurationRequest, deliveryEntity);
        configurationEntity = testConfigurationService.create(configurationEntity);

        response.status(201);
        return new Gson().toJson(TestConfigurationFactory.build(configurationEntity));
    }
}
