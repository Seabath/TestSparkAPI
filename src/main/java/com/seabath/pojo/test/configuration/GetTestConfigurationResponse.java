package com.seabath.pojo.test.configuration;

import com.seabath.common.Status;
import com.seabath.pojo.delivery.GetDeliveryResponse;

public record GetTestConfigurationResponse(Long id, Status statusMini,
                                           GetDeliveryResponse getDeliveryResponse, String env) {
}
