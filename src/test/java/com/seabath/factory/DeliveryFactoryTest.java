package com.seabath.factory;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import com.seabath.pojo.delivery.PostDeliveryRequest;
import com.seabath.pojo.entity.DeliveryEntity;

class DeliveryFactoryTest {

    @Test
    public void shouldGetException() {
        new DeliveryFactory();
    }

    @Test
    public void shouldGetNullEntity() {
        PostDeliveryRequest request = null;
        assertThat(DeliveryFactory.build(request))
            .isNull();
    }


    @Test
    public void shouldGetNullResponse() {
        DeliveryEntity request = null;
        assertThat(DeliveryFactory.build(request))
            .isNull();
    }

}