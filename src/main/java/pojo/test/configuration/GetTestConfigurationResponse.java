package pojo.test.configuration;

import common.Status;
import lombok.Builder;
import lombok.Getter;
import pojo.delivery.GetDeliveryResponse;

@Getter
@Builder
public class GetTestConfigurationResponse {

    private Long id;

    private Status statusMini;
    private GetDeliveryResponse getDeliveryResponse;
    private String env;
}
