package endpoint;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.lang.reflect.Field;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import pojo.delivery.PostDeliveryRequest;
import pojo.delivery.GetDeliveryResponse;
import pojo.entity.DeliveryEntity;
import service.SimpleService;

class DeliveryEndpointTest extends AbstractEndPointTest {

    private static DeliveryEndpoint endpoint;
    private static SimpleService mockedService;

    @BeforeAll
    public static void initService() throws NoSuchFieldException, IllegalAccessException {
        endpoint = new DeliveryEndpoint();
        mockedService = mock(SimpleService.class);

        final Field deliveryServiceField = endpoint.getClass().getDeclaredField("deliveryService");
        deliveryServiceField.setAccessible(true);
        deliveryServiceField.set(endpoint, mockedService);
        sparkSwagger.endpoint(endpoint);
    }

    @BeforeEach
    public void resetMock() {
        Mockito.reset(mockedService);
    }

    @Test
    public void shouldCreateNewDelivery() throws UnirestException {

        String path = "path";
        String version = "version";
        String platform = "platform";
        PostDeliveryRequest request = PostDeliveryRequest.builder()
            .path(path)
            .version(version)
            .platform(platform)
            .build();
        GetDeliveryResponse expectedBody = GetDeliveryResponse.builder()
            .path(path)
            .platform(platform)
            .version(version)
            .build();


        HttpResponse<String> response = testPost(DeliveryEndpoint.ENDPOINT_PATH, request);


        assertThat(response.getStatus())
            .isEqualTo(201);
        assertThat(new Gson().fromJson(response.getBody(), GetDeliveryResponse.class))
            .isEqualToIgnoringNullFields(expectedBody);
    }


    @Test
    public void shouldGetDelivery() throws UnirestException {
        final long id = 42L;
        final Date date = new Date();
        DeliveryEntity entity = DeliveryEntity.builder()
            .date(date)
            .id(id)
            .path("path")
            .version("version")
            .platform("platform")
            .build();
        GetDeliveryResponse expectedBody = GetDeliveryResponse.builder()
            .date(entity.getDate())
            .id(entity.getId())
            .path(entity.getPath())
            .version(entity.getVersion())
            .platform(entity.getPlatform())
            .build();

        when(mockedService.get(eq(id))).thenReturn(entity);

        HttpResponse<String> response = testGet(DeliveryEndpoint.ENDPOINT_PATH + "/" + id);


        assertThat(response.getStatus())
            .isEqualTo(200);
        assertThat(response.getBody())
            .isEqualTo(new Gson().toJson(expectedBody));
    }


    @Test
    public void shouldNotGetDelivery() throws UnirestException {
        final long id = 42L;

        when(mockedService.get(eq(id))).thenReturn(null);

        HttpResponse<String> response = testGet(DeliveryEndpoint.ENDPOINT_PATH + "/" + id);


        assertThat(response.getStatus())
            .isEqualTo(404);
    }

}