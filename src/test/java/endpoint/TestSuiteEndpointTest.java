package endpoint;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import common.Status;
import static endpoint.TestSuiteEndpoint.START_ROUTE;
import static endpoint.TestSuiteEndpoint.TEST_SUITE_ENDPOINT_ROUTE;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import pojo.entity.TestConfigurationEntity;
import pojo.entity.TestSuiteEntity;
import pojo.test.configuration.GetTestConfigurationResponse;
import pojo.test.suite.GetTestSuiteResponse;
import service.SimpleService;

class TestSuiteEndpointTest extends AbstractEndPointTest {

    private static TestSuiteEndpoint endpoint;
    private static SimpleService<TestSuiteEntity> mockedTestSuiteService;
    private static SimpleService<TestConfigurationEntity> mockedTestConfigurationService;

    @BeforeAll
    public static void initService() {
        mockedTestSuiteService = mock(SimpleService.class);
        mockedTestConfigurationService = mock(SimpleService.class);
        endpoint = new TestSuiteEndpoint(mockedTestSuiteService, mockedTestConfigurationService);

        sparkSwagger.endpoint(endpoint);
    }

    @Test
    public void shouldNotFindConfiguration() throws UnirestException {
        final long id = 42L;
        final HttpResponse<String> response = testPost(TEST_SUITE_ENDPOINT_ROUTE + START_ROUTE + "/" + id, "");


        verify(mockedTestConfigurationService, only()).get(id);
        assertThat(response.getStatus())
            .isEqualTo(404);
    }


    @Test
    public void shouldStartSuite() throws UnirestException {
        final long idTestConfig = 42L;
        final String env = "bloblo";
        final Status statusMini = Status.FAIL;
        final TestConfigurationEntity testConfigurationEntity = TestConfigurationEntity.builder()
            .id(idTestConfig)
            .statusMini(statusMini)
            .env(env)
            .build();

        final GetTestSuiteResponse expected = GetTestSuiteResponse.builder()
            .getTestConfigurationResponse(GetTestConfigurationResponse.builder()
                .env(env)
                .statusMini(statusMini)
                .id(idTestConfig)
                .build())
            .status(Status.IN_PROGRESS)
            .build();
        when(mockedTestConfigurationService.get(eq(idTestConfig))).thenReturn(testConfigurationEntity);

        final HttpResponse<String> response = testPost(TEST_SUITE_ENDPOINT_ROUTE + START_ROUTE + "/" + idTestConfig, "");

        final GetTestSuiteResponse actual = new Gson().fromJson(response.getBody(), GetTestSuiteResponse.class);
        assertThat(response.getStatus())
            .isEqualTo(201);
        assertThat(actual.getStatus())
            .isEqualTo(expected.getStatus());
        assertThat(actual.getStartDate())
            .isNotNull();
        assertThat(actual.getGetTestConfigurationResponse())
            .isEqualToComparingFieldByField(expected.getGetTestConfigurationResponse());
    }

}