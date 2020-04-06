package com.seabath.endpoint;

import com.beerboy.ss.SparkSwagger;
import static com.beerboy.ss.descriptor.EndpointDescriptor.endpointPath;
import com.beerboy.ss.descriptor.MethodDescriptor;
import com.beerboy.ss.rest.Endpoint;
import com.google.gson.Gson;
import com.seabath.factory.TestConfigurationFactory;
import com.seabath.filter.DoNothingFilter;
import com.seabath.pojo.entity.DeliveryEntity;
import com.seabath.pojo.entity.TestConfigurationEntity;
import com.seabath.pojo.test.configuration.GetTestConfigurationResponse;
import com.seabath.pojo.test.configuration.PostTestConfigurationRequest;
import com.seabath.service.SimpleService;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import spark.Request;
import spark.Response;

/**
 * Endpoint that handles configuration entity.
 */
public class TestConfigurationEndpoint implements Endpoint {

    private static final String TEST_CONFIGURATION_ENDPOINT_ROUTE = "/configuration";
    public static final String PARAM_ID = ":id";

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
    public String getTestConfiguration(Request request, Response response) {
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
    public String createTestConfiguration(Request request, Response response) {
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
