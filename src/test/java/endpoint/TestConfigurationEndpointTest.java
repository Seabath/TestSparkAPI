package endpoint;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import static endpoint.TestConfigurationEndpoint.TEST_CONFIGURATION_ENDPOINT_ROUTE;
import factory.TestConfigurationFactory;
import java.lang.reflect.Field;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import pojo.delivery.GetDeliveryResponse;
import pojo.entity.DeliveryEntity;
import pojo.entity.TestConfigurationEntity;
import pojo.test.configuration.GetTestConfigurationResponse;
import pojo.test.configuration.PostTestConfigurationRequest;
import service.SimpleService;

class TestConfigurationEndpointTest extends AbstractEndPointTest {

    private static TestConfigurationEndpoint endpoint;
    private static SimpleService mockedDeliveryService;
    private static SimpleService mockedTestConfigurationService;

    @BeforeAll
    public static void initService() throws NoSuchFieldException, IllegalAccessException {
        endpoint = new TestConfigurationEndpoint();
        mockedDeliveryService = mock(SimpleService.class);
        mockedTestConfigurationService = mock(SimpleService.class);

        final Field deliveryServiceField = endpoint.getClass().getDeclaredField("deliveryService");
        deliveryServiceField.setAccessible(true);
        deliveryServiceField.set(endpoint, mockedDeliveryService);

        final Field testConfigurationServiceField = endpoint.getClass().getDeclaredField("testConfigurationService");
        testConfigurationServiceField.setAccessible(true);
        testConfigurationServiceField.set(endpoint, mockedTestConfigurationService);
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