package com.seabath.endpoint;

import com.beerboy.ss.SparkSwagger;
import static com.beerboy.ss.descriptor.EndpointDescriptor.endpointPath;
import com.beerboy.ss.descriptor.MethodDescriptor;
import com.beerboy.ss.rest.Endpoint;
import com.google.gson.Gson;
import com.seabath.factory.TestFactory;
import com.seabath.filter.DoNothingFilter;
import com.seabath.pojo.entity.TestEntity;
import com.seabath.pojo.entity.TestSuiteEntity;
import com.seabath.pojo.test.run.PostStartTestBody;
import com.seabath.pojo.test.test.GetTestResponse;
import com.seabath.service.TestService;
import com.seabath.service.TestSuiteService;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import spark.Request;
import spark.Response;

public class TestEndpoint implements Endpoint {

    private static final String TEST_ENDPOINT_ROUTE = "/test";
    private static final String START_ROUTE = "/start";
    private static final String SUCCESS_ROUTE = "/success";
    public static final String PARAM_ID = ":id";
    private final TestService testService;
    private final TestSuiteService testSuiteService;


    public TestEndpoint(TestSuiteService testSuiteService, TestService testService) {
        this.testSuiteService = testSuiteService;
        this.testService = testService;
    }

    @Override
    public void bind(SparkSwagger sparkSwagger) {
        sparkSwagger.endpoint(endpointPath(TEST_ENDPOINT_ROUTE), new DoNothingFilter())

            .post(MethodDescriptor.path(START_ROUTE + "/" + PARAM_ID)
                    .withRequestType(PostStartTestBody.class)
                    .withResponseType(GetTestResponse.class)
                , this::startTest)
            .put(MethodDescriptor.path(SUCCESS_ROUTE + "/" + PARAM_ID)
                    .withResponseType(GetTestResponse.class)
                , this::stopSuccessTest);
    }

    /**
     * Create a run suite with given test suite ID and stores it in database.
     *
     * @param request  Request of the request with a PostTestRunBody as json in its
     *                 body.
     * @param response Response of the request.
     * @return Json formated GetTestResponse object
     */
    @SneakyThrows
    public String startTest(Request request, Response response) {
        final long id = Long.parseLong(request.params(PARAM_ID));
        final TestSuiteEntity testSuiteEntity = testSuiteService.get(id);
        if (testSuiteEntity == null) {
            throw new NotFoundException("Test suite");
        }
        final PostStartTestBody body = new Gson().fromJson(request.body(), PostStartTestBody.class);

        final TestEntity testEntity = testService.startTest(testSuiteEntity, body.getTestName(), body.getPackageName());

        final GetTestResponse responseBody = TestFactory.build(testEntity);

        response.status(201);
        return new Gson().toJson(responseBody);
    }


    /**
     * Stops a test with a success status.
     *
     * @param request  Request of the request with a PostTestRunBody as json in its
     *                 body.
     * @param response Response of the request.
     * @return Json formated GetTestResponse object
     */
    @SneakyThrows
    public String stopSuccessTest(Request request, Response response) {
        final long id = Long.parseLong(request.params(PARAM_ID));
        final TestEntity testEntity = testService.get(id);
        if (testEntity == null) {
            throw new NotFoundException("Test");
        }

        testService.succeed(testEntity);

        final GetTestResponse responseBody = TestFactory.build(testEntity);
        return new Gson().toJson(responseBody);
    }
}
