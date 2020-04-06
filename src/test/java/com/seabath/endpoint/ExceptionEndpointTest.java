package com.seabath.endpoint;

import com.beerboy.ss.SparkSwagger;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import spark.Request;
import spark.Response;

class ExceptionEndpointTest {

    private ExceptionEndpoint endpoint;
    private Request mockedRequest;
    private Response mockedResponse;


    @BeforeEach
    public void initEndpoint() {
        endpoint = new ExceptionEndpoint();
        mockedRequest = mock(Request.class);
        mockedResponse = mock(Response.class);
    }

    @Test
    public void shouldBindExceptions() {
        final SparkSwagger mockedSparkSwagger = mock(SparkSwagger.class);
        endpoint.bind(mockedSparkSwagger);

        verify(mockedSparkSwagger, times(2)).exception(any(), any());
    }

    @Test
    public void should_catch_500() {
        final Exception mock = new Exception();
        endpoint.handleDefaultException(mock, mockedRequest, mockedResponse);

        verify(mockedResponse, times(1)).status(eq(500));
        verify(mockedResponse, times(1)).body(anyString());
    }

    @Test
    public void should_catch_404() {
        final NotFoundException mock = mock(NotFoundException.class);
        endpoint.handleNotFound(mock, mockedRequest, mockedResponse);

        verify(mockedResponse, times(1)).status(eq(404));
        verify(mockedResponse, times(1)).body(anyString());
    }
}