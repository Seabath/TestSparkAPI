package endpoint;

import com.beerboy.ss.SparkSwagger;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pojo.delivery.NewDeliveryRequest;
import pojo.delivery.NewDeliveryResponse;

class DeliveryEndpointTest extends AbstractEndPointTest {

    @BeforeAll
    public static void initService() {
        SparkSwagger.of(sparkService)
            .endpoints(() -> Collections.singletonList(new DeliveryEndpoint()));
    }


    @Test
    public void shouldCreateNewDelivery() throws UnirestException {

        String path = "path";
        String version = "version";
        String platform = "platform";
        NewDeliveryRequest request = NewDeliveryRequest.builder()
            .path(path)
            .version(version)
            .platform(platform)
            .build();
        NewDeliveryResponse expectedBody = NewDeliveryResponse.builder()
            .path(path)
            .platform(platform)
            .version(version)
            .build();


        HttpResponse<String> response = testPost(DeliveryEndpoint.ENDPOINT_PATH, request);


        assertThat(response.getStatus())
            .isEqualTo(201);
        assertThat(new Gson().fromJson(response.getBody(), NewDeliveryResponse.class))
            .isEqualToIgnoringNullFields(expectedBody);
    }

}