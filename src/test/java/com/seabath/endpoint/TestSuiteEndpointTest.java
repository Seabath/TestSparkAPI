package com.seabath.endpoint;

import com.google.gson.Gson;
import com.seabath.common.Status;
import static com.seabath.endpoint.TestSuiteEndpoint.PARAM_ID;
import com.seabath.pojo.entity.TestConfigurationEntity;
import com.seabath.pojo.entity.TestSuiteEntity;
import com.seabath.pojo.test.configuration.GetTestConfigurationResponse;
import com.seabath.pojo.test.suite.GetTestSuiteResponse;
import com.seabath.service.SimpleService;
import com.seabath.service.TestSuiteService;
import javassist.NotFoundException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import spark.Request;
import spark.Response;

class TestSuiteEndpointTest {

    private TestSuiteService mockedTestSuiteService;
    private SimpleService<TestConfigurationEntity> mockedTestConfigurationService;
    private TestSuiteEndpoint endpoint;
    private Request mockedRequest;
    private Response mockedResponse;


    @BeforeEach
    public void initEndpoint() {
        mockedTestSuiteService = mock(TestSuiteService.class);
        mockedTestConfigurationService = mock(SimpleService.class);
        endpoint = new TestSuiteEndpoint(mockedTestSuiteService, mockedTestConfigurationService);
        mockedRequest = mock(Request.class);
        mockedResponse = mock(Response.class);
    }

    @Test
    public void shouldNotFindConfiguration() {
        final long id = 42L;
        when(mockedRequest.params(PARAM_ID)).thenReturn(String.valueOf(id));

        Assertions.assertThrows(
            NotFoundException.class,
            () -> endpoint.startSuite(mockedRequest, mockedResponse)
        );

        verify(mockedTestConfigurationService, only()).get(id);
    }


    @Test
    public void shouldStartSuite() {
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
        when(mockedRequest.params(PARAM_ID)).thenReturn(String.valueOf(idTestConfig));

        final String response = endpoint.startSuite(mockedRequest, mockedResponse);

        final GetTestSuiteResponse actual = new Gson().fromJson(response, GetTestSuiteResponse.class);
        verify(mockedResponse, times(1)).status(eq(201));
        assertThat(actual.getStatus())
            .isEqualTo(expected.getStatus());
        assertThat(actual.getStartDate())
            .isNotNull();
        assertThat(actual.getGetTestConfigurationResponse())
            .isEqualToComparingFieldByField(expected.getGetTestConfigurationResponse());
    }


    @Test
    public void shouldStopSuiteNotFound() {
        final long id = 42L;

        when(mockedRequest.params(PARAM_ID)).thenReturn(String.valueOf(id));

        Assertions.assertThrows(
            NotFoundException.class,
            () -> endpoint.stopSuite(mockedRequest, mockedResponse)
        );


        verify(mockedTestSuiteService, only()).get(eq(id));
    }


    @Test
    public void shouldStopSuite() {
        final long idTestConfig = 42L;
        TestSuiteEntity testSuiteEntity = TestSuiteEntity.builder().build();

        when(mockedTestSuiteService.get(eq(idTestConfig))).thenReturn(testSuiteEntity);
        when(mockedRequest.params(PARAM_ID)).thenReturn(String.valueOf(idTestConfig));

        final String response = endpoint.stopSuite(mockedRequest, mockedResponse);

        verify(mockedTestSuiteService, times(1)).updateStatus(eq(testSuiteEntity));
        assertThat(response)
            .isEqualTo(new Gson().toJson(testSuiteEntity));
    }
}