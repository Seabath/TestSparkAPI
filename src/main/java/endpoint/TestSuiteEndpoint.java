package endpoint;

import com.beerboy.ss.SparkSwagger;
import static com.beerboy.ss.descriptor.EndpointDescriptor.endpointPath;
import com.beerboy.ss.descriptor.MethodDescriptor;
import com.beerboy.ss.descriptor.ParameterDescriptor;
import com.beerboy.ss.rest.Endpoint;
import com.google.gson.Gson;
import factory.TestSuiteFactory;
import filter.DoNothingFilter;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import pojo.entity.TestConfigurationEntity;
import pojo.entity.TestSuiteEntity;
import pojo.test.suite.GetTestSuiteResponse;
import service.SimpleService;
import spark.Request;
import spark.Response;

/**
 * Endpoin that handles TestSuite entity.
 */
public class TestSuiteEndpoint implements Endpoint {

    public static final String TEST_SUITE_ENDPOINT_ROUTE = "/suite";
    public static final String START_ROUTE = "/start";
    private static final String PARAM_ID = ":id";
    private final SimpleService<TestSuiteEntity> testSuiteService;
    private final SimpleService<TestConfigurationEntity> testConfigurationService;

    public TestSuiteEndpoint(SimpleService<TestSuiteEntity> testSuiteService,
                             SimpleService<TestConfigurationEntity> testConfigurationService) {
        this.testSuiteService = testSuiteService;
        this.testConfigurationService = testConfigurationService;
    }

    @Override
    public void bind(SparkSwagger sparkSwagger) {

        sparkSwagger.endpoint(endpointPath(TEST_SUITE_ENDPOINT_ROUTE), new DoNothingFilter())

            .post(MethodDescriptor.path(START_ROUTE + "/" + PARAM_ID)
                    .withParam(ParameterDescriptor.newBuilder()
                        .withName("id")
                        .withDescription("Id of run configuration")
                        .build())
                    .withResponseType(GetTestSuiteResponse.class)
                , this::startSuite);

    }


    /**
     * Create a test suite with given configuration ID and stores it in database.
     *
     * @param request  Request of the request with a PostTestConfigurationRequest as json in its
     *                 body.
     * @param response Response of the request
     * @return Json formated GetTestSuiteResponse object
     */
    @SneakyThrows
    private String startSuite(Request request, Response response) {
        final long id = Long.parseLong(request.params(PARAM_ID));
        final TestConfigurationEntity testConfigurationEntity = testConfigurationService.get(id);
        if (testConfigurationEntity == null) {
            throw new NotFoundException("Test Configuration");
        }

        TestSuiteEntity testSuiteEntity = TestSuiteFactory.build(testConfigurationEntity);
        testSuiteService.create(testSuiteEntity);

        final GetTestSuiteResponse getTestSuiteResponse = TestSuiteFactory.build(testSuiteEntity);
        response.status(201);
        return new Gson().toJson(getTestSuiteResponse);
    }
}
