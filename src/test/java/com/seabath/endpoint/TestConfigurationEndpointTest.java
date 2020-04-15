package com.seabath.endpoint;

import com.google.gson.Gson;
import static com.seabath.endpoint.TestConfigurationEndpoint.PARAM_ID;
import com.seabath.pojo.delivery.GetDeliveryResponse;
import com.seabath.pojo.entity.DeliveryEntity;
import com.seabath.pojo.entity.TestConfigurationEntity;
import com.seabath.pojo.test.configuration.GetTestConfigurationResponse;
import com.seabath.pojo.test.configuration.PostTestConfigurationRequest;
import com.seabath.service.SimpleService;
import javassist.NotFoundException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import spark.Request;
import spark.Response;

class TestConfigurationEndpointTest {

    private SimpleService<DeliveryEntity> mockedDeliveryService;
    private SimpleService<TestConfigurationEntity> mockedTestConfigurationService;
    private TestConfigurationEndpoint endpoint;
    private Request mockedRequest;
    private Response mockedResponse;


    @BeforeEach
    public void initEndpoint() {
        mockedDeliveryService = mock(SimpleService.class);
        mockedTestConfigurationService = mock(SimpleService.class);
        this.endpoint = new TestConfigurationEndpoint(mockedTestConfigurationService, mockedDeliveryService);
        mockedRequest = mock(Request.class);
        mockedResponse = mock(Response.class);

    }

    @Test
    public void shouldNotGetConfiguration() {
        final Long id = 42L;

        when(mockedRequest.params(eq(PARAM_ID))).thenReturn("" + id);
        Assertions.assertThrows(NotFoundException.class,
            () -> endpoint.getTestConfiguration(mockedRequest, mockedResponse)
        );

        verify(mockedTestConfigurationService, times(1)).get(eq(id));
    }

    @Test
    public void shouldGetConfiguration() {

        final long id = 42L;
        final TestConfigurationEntity configurationEntity = TestConfigurationEntity.builder()
            .id(id)
            .deliveryEntity(DeliveryEntity.builder().id(id).build())
            .build();
        final GetTestConfigurationResponse expected = new GetTestConfigurationResponse(
            id,
            null,
            new GetDeliveryResponse(id, null, null, null, null),
            null
        );
        when(mockedTestConfigurationService.get(eq(id))).thenReturn(configurationEntity);
        when(mockedRequest.params(eq(PARAM_ID))).thenReturn("" + id);

        final String response = endpoint.getTestConfiguration(mockedRequest, mockedResponse);

        assertThat(response)
            .isEqualTo(new Gson().toJson(expected));
    }

    @Test
    public void shouldNotCreateConfiguration() {
        final long id = 42L;
        final PostTestConfigurationRequest configurationRequest = new PostTestConfigurationRequest(
            id,
            null,
            null
        );
        final String body = new Gson().toJson(configurationRequest);

        when(mockedRequest.body()).thenReturn(body);

        Assertions.assertThrows(
            NotFoundException.class,
            () -> endpoint.createTestConfiguration(mockedRequest, mockedResponse)
        );
    }

    @Test
    public void shouldCreateConfiguration() {
        final long deliveryId = 42L;
        final DeliveryEntity deliveryEntity = DeliveryEntity.builder()
            .id(deliveryId)
            .build();
        final PostTestConfigurationRequest configurationRequest = new PostTestConfigurationRequest(
            deliveryId,
            null,
            null
        );
        final GetTestConfigurationResponse expected = new GetTestConfigurationResponse(
            null,
            null,
            new GetDeliveryResponse(deliveryId, null, null, null, null),
            null
        );

        when(mockedDeliveryService.get(eq(deliveryId))).thenReturn(deliveryEntity);
        when(mockedTestConfigurationService.create(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(mockedRequest.params(PARAM_ID)).thenReturn(String.valueOf(deliveryId));
        when(mockedRequest.body()).thenReturn(new Gson().toJson(configurationRequest));

        final String response = endpoint.createTestConfiguration(mockedRequest, mockedResponse);

        verify(mockedResponse, times(1)).status(eq(201));
        assertThat(response)
            .isEqualTo(new Gson().toJson(expected));
    }

}