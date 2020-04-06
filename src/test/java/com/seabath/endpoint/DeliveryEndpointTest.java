package com.seabath.endpoint;

import com.google.gson.Gson;
import static com.seabath.endpoint.DeliveryEndpoint.PARAM_ID;
import com.seabath.pojo.delivery.GetDeliveryResponse;
import com.seabath.pojo.delivery.PostDeliveryRequest;
import com.seabath.pojo.entity.DeliveryEntity;
import com.seabath.service.SimpleService;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javassist.NotFoundException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import spark.Request;
import spark.Response;

class DeliveryEndpointTest {

    private SimpleService<DeliveryEntity> mockedService;
    private DeliveryEndpoint deliveryEndpoint;
    private Request mockedRequest;
    private Response mockedResponse;


    @BeforeEach
    public void resetMock() {
        mockedService = mock(SimpleService.class);
        mockedResponse = mock(Response.class);
        mockedRequest = mock(Request.class);
        deliveryEndpoint = new DeliveryEndpoint(mockedService);
    }

    @Test
    public void shouldCreateNewDelivery() {

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


        when(mockedRequest.body()).thenReturn(new Gson().toJson(request));

        final String delivery = deliveryEndpoint.createDelivery(mockedRequest, mockedResponse);


        verify(mockedResponse, times(1)).status(eq(201));
        assertThat(new Gson().fromJson(delivery, GetDeliveryResponse.class))
            .isEqualToIgnoringNullFields(expectedBody);
    }


    @Test
    public void shouldGetDelivery() {
        final long id = 42L;
        final Date date = Date.from(new Date().toInstant().truncatedTo(ChronoUnit.SECONDS));
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
        when(mockedRequest.params(PARAM_ID)).thenReturn("" + id);

        final String delivery = deliveryEndpoint.getDelivery(mockedRequest, mockedResponse);

        assertThat(new Gson().fromJson(delivery, GetDeliveryResponse.class))
            .isEqualToIgnoringNullFields(expectedBody);
        verify(mockedService, times(1)).get(id);
    }


    @Test
    public void shouldNotGetDelivery() {
        final long id = 42L;

        when(mockedService.get(eq(id))).thenReturn(null);
        when(mockedRequest.params(PARAM_ID)).thenReturn("" + id);

        Assertions.assertThrows(NotFoundException.class,
            () -> deliveryEndpoint.getDelivery(mockedRequest, mockedResponse));

        verify(mockedService, times(1)).get(id);
    }

}