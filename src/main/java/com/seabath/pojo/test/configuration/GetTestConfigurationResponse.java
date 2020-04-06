package com.seabath.pojo.test.configuration;

import com.seabath.common.Status;
import lombok.Builder;
import lombok.Getter;
import com.seabath.pojo.delivery.GetDeliveryResponse;

@Getter
@Builder
public class GetTestConfigurationResponse {

    private Long id;

    private Status statusMini;
    private GetDeliveryResponse getDeliveryResponse;
    private String env;
}
