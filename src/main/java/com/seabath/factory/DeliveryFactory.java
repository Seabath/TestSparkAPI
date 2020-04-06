package com.seabath.factory;

import java.util.Date;
import com.seabath.pojo.delivery.GetDeliveryResponse;
import com.seabath.pojo.delivery.PostDeliveryRequest;
import com.seabath.pojo.entity.DeliveryEntity;

/**
 * Factory to build DeliveryEntity and response bodys related to DeliveryEntity
 */
public class DeliveryFactory {

    /**
     * Builds a DeliveryEntity from a request's body
     *
     * @param postDeliveryRequest Body from a post request.
     * @return DeliveryEntity
     */
    public static DeliveryEntity build(PostDeliveryRequest postDeliveryRequest) {
        if (postDeliveryRequest == null) {
            return null;
        }
        return DeliveryEntity.builder()
            .date(new Date())
            .path(postDeliveryRequest.getPath())
            .version(postDeliveryRequest.getVersion())
            .platform(postDeliveryRequest.getPlatform())
            .build();
    }

    /**
     * Builds a response from a DeliveryEntity.
     *
     * @param deliveryEntity Body from a post request.
     * @return GetDeliveryResponse to put in response body
     */
    public static GetDeliveryResponse build(DeliveryEntity deliveryEntity) {
        if (deliveryEntity == null) {
            return null;
        }
        return GetDeliveryResponse.builder()
            .date(deliveryEntity.getDate())
            .path(deliveryEntity.getPath())
            .version(deliveryEntity.getVersion())
            .platform(deliveryEntity.getPlatform())
            .id(deliveryEntity.getId())
            .build();
    }
}
