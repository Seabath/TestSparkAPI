package com.seabath.endpoint;

import com.google.gson.Gson;
import static com.seabath.endpoint.TestEndpoint.PARAM_ID;
import com.seabath.pojo.entity.TestEntity;
import com.seabath.pojo.entity.TestRunEntity;
import com.seabath.pojo.entity.TestSuiteEntity;
import com.seabath.pojo.test.run.GetTestRunResponse;
import com.seabath.pojo.test.run.PostStartTestBody;
import com.seabath.pojo.test.test.GetTestResponse;
import com.seabath.service.TestService;
import com.seabath.service.TestSuiteService;
import java.util.Collections;
import javassist.NotFoundException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import spark.Request;
import spark.Response;

class TestEndpointTest {

    private TestService mockedTestService;
    private TestSuiteService mockedTestSuiteService;
    private TestEndpoint endpoint;
    private Request mockedRequest;
    private Response mockedResponse;


    @BeforeEach
    public void initEndpoint() {
        mockedTestService = mock(TestService.class);
        mockedTestSuiteService = mock(TestSuiteService.class);
        this.endpoint =
            new TestEndpoint(mockedTestSuiteService, mockedTestService);
        mockedRequest = mock(Request.class);
        mockedResponse = mock(Response.class);

    }


    @Test
    public void shouldNotFindSuite() {
        final Long id = 42L;

        when(mockedRequest.params(PARAM_ID)).thenReturn(String.valueOf(id));
        Assertions.assertThrows(
            NotFoundException.class,
            () -> endpoint.startTest(mockedRequest, mockedResponse)
        );

        verify(mockedTestSuiteService, times(1)).get(eq(id));
    }

    @Test
    public void shouldStartTest() {
        final Long idSuite = 42L;
        final String testName = "testname";
        final String packageName = "packagename";
        final PostStartTestBody requestBody = new PostStartTestBody(
            testName,
            packageName
        );
        final TestSuiteEntity testSuiteEntity = TestSuiteEntity.builder()
            .id(idSuite)
            .build();
        final TestEntity testEntity = TestEntity.builder()
            .testName(testName)
            .packageName(packageName)
            .testSuiteEntity(testSuiteEntity)
            .testRunEntities(Collections.singleton(TestRunEntity.builder().build()))
            .build();

        GetTestResponse expected = new GetTestResponse(
            null,
            testName,
            packageName,
            null,
            idSuite,
            Collections.singleton(new GetTestRunResponse(null, null, null, null))
        );

        when(mockedTestSuiteService.get(eq(idSuite))).thenReturn(testSuiteEntity);
        when(mockedTestService.startTest(eq(testSuiteEntity), eq(testName), eq(packageName))).thenReturn(testEntity);
        when(mockedRequest.params(eq(PARAM_ID))).thenReturn(String.valueOf(idSuite));
        when(mockedRequest.body()).thenReturn(new Gson().toJson(requestBody));

        final String response = endpoint.startTest(mockedRequest, mockedResponse);

        verify(mockedResponse, times(1)).status(eq(201));
        final GetTestResponse getTestResponse = new Gson().fromJson(response, GetTestResponse.class);
        assertThat(getTestResponse)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    public void shouldNotFindTest() {
        final Long id = 42L;

        when(mockedRequest.params(PARAM_ID)).thenReturn(String.valueOf(id));
        Assertions.assertThrows(
            NotFoundException.class,
            () -> endpoint.stopSuccessTest(mockedRequest, mockedResponse)
        );

        verify(mockedTestService, times(1)).get(eq(id));
    }

    @Test
    public void shouldSucceedTest() {
        final Long id = 42L;
        final long testSuiteId = 24L;
        final TestSuiteEntity testSuiteEntity = TestSuiteEntity.builder()
            .id(testSuiteId)
            .build();
        final TestEntity testEntity = TestEntity.builder()
            .id(id)
            .testRunEntities(Collections.emptySet())
            .testSuiteEntity(testSuiteEntity)
            .build();
        final GetTestResponse expected = new GetTestResponse(
            id,
            null,
            null,
            null,
            testSuiteId,
            Collections.emptySet()
        );

        when(mockedTestService.get(eq(id))).thenReturn(testEntity);
        when(mockedRequest.params(eq(PARAM_ID))).thenReturn(String.valueOf(id));

        final String response = endpoint.stopSuccessTest(mockedRequest, mockedResponse);

        verify(mockedTestService, times(1)).succeed(eq(testEntity));
        final GetTestResponse body = new Gson().fromJson(response, GetTestResponse.class);
        assertThat(body)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

}