package endpoint;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import static endpoint.TestEndpoint.START_ROUTE;
import static endpoint.TestEndpoint.TEST_ENDPOINT_ROUTE;
import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import pojo.entity.TestEntity;
import pojo.entity.TestRunEntity;
import pojo.entity.TestSuiteEntity;
import pojo.test.run.GetTestRunResponse;
import pojo.test.run.PostStartTestBody;
import pojo.test.test.GetTestResponse;
import service.TestService;
import service.TestSuiteService;

class TestEndpointTest extends AbstractEndPointTest {

    private static TestService mockedTestService;
    private static TestSuiteService mockedTestSuiteService;

    @BeforeAll
    public static void initService() {
        mockedTestService = mock(TestService.class);
        mockedTestSuiteService = mock(TestSuiteService.class);
        TestEndpoint endpoint =
            new TestEndpoint(mockedTestSuiteService, mockedTestService);

        sparkSwagger.endpoint(endpoint);
    }


    @Test
    public void shouldNotFindSuite() throws UnirestException {
        final HttpResponse<String> response = testPost(TEST_ENDPOINT_ROUTE + START_ROUTE + "/42", "");

        assertThat(response.getStatus())
            .isEqualTo(404);
    }

    @Test
    public void shouldStartTest() throws UnirestException {
        final Long idSuite = 42L;
        final String testName = "testname";
        final String packageName = "packagename";
        final PostStartTestBody requestBody = PostStartTestBody.builder()
            .packageName(packageName)
            .testName(testName)
            .build();
        final TestSuiteEntity testSuiteEntity = TestSuiteEntity.builder()
            .id(idSuite)
            .build();
        final TestEntity testEntity = TestEntity.builder()
            .testName(testName)
            .packageName(packageName)
            .testSuiteEntity(testSuiteEntity)
            .testRunEntities(Collections.singleton(TestRunEntity.builder().build()))
            .build();

        GetTestResponse expected = GetTestResponse.builder()
            .testSuiteId(idSuite)
            .packageName(packageName)
            .testName(testName)
            .testRuns(Collections.singleton(GetTestRunResponse.builder().build()))
            .build();

        when(mockedTestSuiteService.get(eq(idSuite))).thenReturn(testSuiteEntity);
        when(mockedTestService.startTest(eq(testSuiteEntity), eq(testName), eq(packageName))).thenReturn(testEntity);
        final HttpResponse<String> response = testPost(TEST_ENDPOINT_ROUTE + START_ROUTE + "/" + idSuite,
            requestBody);


        assertThat(response.getStatus())
            .isEqualTo(201);
        final String responseBody = response.getBody();
        final GetTestResponse getTestResponse = new Gson().fromJson(responseBody, GetTestResponse.class);
        assertThat(getTestResponse)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

}