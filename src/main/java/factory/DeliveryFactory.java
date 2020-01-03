package factory;

import java.util.Date;
import pojo.delivery.PostDeliveryRequest;
import pojo.delivery.GetDeliveryResponse;
import pojo.entity.DeliveryEntity;

public class DeliveryFactory {

    public static DeliveryEntity build(PostDeliveryRequest postDeliveryRequest) {
        return DeliveryEntity.builder()
            .date(new Date())
            .path(postDeliveryRequest.getPath())
            .version(postDeliveryRequest.getVersion())
            .platform(postDeliveryRequest.getPlatform())
            .build();
    }

    public static GetDeliveryResponse build(DeliveryEntity deliveryEntity) {
        return GetDeliveryResponse.builder()
            .date(deliveryEntity.getDate())
            .path(deliveryEntity.getPath())
            .version(deliveryEntity.getVersion())
            .platform(deliveryEntity.getPlatform())
            .id(deliveryEntity.getId())
            .build();
    }
}
