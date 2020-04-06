package com.seabath.endpoint;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import static com.seabath.endpoint.TestConfigurationEndpoint.TEST_CONFIGURATION_ENDPOINT_ROUTE;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.seabath.pojo.delivery.GetDeliveryResponse;
import com.seabath.pojo.entity.DeliveryEntity;
import com.seabath.pojo.entity.TestConfigurationEntity;
import com.seabath.pojo.test.configuration.GetTestConfigurationResponse;
import com.seabath.pojo.test.configuration.PostTestConfigurationRequest;
import com.seabath.service.SimpleService;

class TestConfigurationEndpointTest extends AbstractEndPointTest {

    private static SimpleService<DeliveryEntity> mockedDeliveryService;
    private static SimpleService<TestConfigurationEntity> mockedTestConfigurationService;

    @BeforeAll
    public static void initService() {
        mockedDeliveryService = mock(SimpleService.class);
        mockedTestConfigurationService = mock(SimpleService.class);
        TestConfigurationEndpoint endpoint =
            new TestConfigurationEndpoint(mockedTestConfigurationService, mockedDeliveryService);

        sparkSwagger.endpoint(endpoint);
    }

    @Test
    public void shouldNotGetConfiguration() throws UnirestException {

        final HttpResponse<String> response = testGet(TEST_CONFIGURATION_ENDPOINT_ROUTE + "/42");

        assertThat(response.getStatus())
            .isEqualTo(404);
    }

    @Test
    public void shouldGetConfiguration() throws UnirestException {

        final long id = 42L;
        final TestConfigurationEntity configurationEntity = TestConfigurationEntity.builder()
            .id(id)
            .deliveryEntity(DeliveryEntity.builder().id(id).build())
            .build();
        final GetTestConfigurationResponse expected = GetTestConfigurationResponse.builder()
            .id(id)
            .getDeliveryResponse(GetDeliveryResponse.builder().id(id).build())
            .build();
        when(mockedTestConfigurationService.get(eq(id))).thenReturn(configurationEntity);

        final HttpResponse<String> response = testGet(TEST_CONFIGURATION_ENDPOINT_ROUTE + "/" + id);

        assertThat(response.getStatus())
            .isEqualTo(200);
        assertThat(response.getBody())
            .isEqualTo(new Gson().toJson(expected));
    }

    @Test
    public void shouldNotCreateConfiguration() throws UnirestException {
        final PostTestConfigurationRequest configurationRequest = PostTestConfigurationRequest.builder()
            .deliveryId(42L)
            .build();

        final HttpResponse<String> response = testPost(TEST_CONFIGURATION_ENDPOINT_ROUTE, configurationRequest);

        assertThat(response.getStatus())
            .isEqualTo(404);
    }

    @Test
    public void shouldCreateConfiguration() throws UnirestException {
        final long deliveryId = 42L;
        final DeliveryEntity deliveryEntity = DeliveryEntity.builder()
            .id(deliveryId)
            .build();
        final PostTestConfigurationRequest configurationRequest = PostTestConfigurationRequest.builder()
            .deliveryId(deliveryId)
            .build();
        final TestConfigurationEntity testConfigurationEntity = TestConfigurationEntity.builder()
            .deliveryEntity(deliveryEntity)
            .build();
        final GetTestConfigurationResponse expected = GetTestConfigurationResponse.builder()
            .getDeliveryResponse(GetDeliveryResponse.builder()
                .id(deliveryId)
                .build())
            .build();

        when(mockedDeliveryService.get(eq(deliveryId))).thenReturn(deliveryEntity);
        when(mockedTestConfigurationService.create(any())).thenReturn(testConfigurationEntity);

        final HttpResponse<String> response = testPost(TEST_CONFIGURATION_ENDPOINT_ROUTE, configurationRequest);

        assertThat(response.getStatus())
            .isEqualTo(201);
        assertThat(response.getBody())
            .isEqualTo(new Gson().toJson(expected));
    }

}