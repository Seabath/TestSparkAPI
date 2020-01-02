package factory;

import java.util.Date;
import pojo.delivery.NewDeliveryRequest;
import pojo.delivery.NewDeliveryResponse;
import pojo.entity.DeliveryEntity;

public class DeliveryFactory {

    public static DeliveryEntity build(NewDeliveryRequest newDeliveryRequest) {
        return DeliveryEntity.builder()
            .date(new Date())
            .path(newDeliveryRequest.getPath())
            .version(newDeliveryRequest.getVersion())
            .platform(newDeliveryRequest.getPlatform())
            .build();
    }

    public static NewDeliveryResponse build(DeliveryEntity deliveryEntity) {
        return NewDeliveryResponse.builder()
            .date(deliveryEntity.getDate())
            .path(deliveryEntity.getPath())
            .version(deliveryEntity.getVersion())
            .platform(deliveryEntity.getPlatform())
            .id(deliveryEntity.getId())
            .build();
    }
}
