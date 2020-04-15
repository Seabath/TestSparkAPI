package com.seabath.pojo.test.configuration;

import com.seabath.common.Status;

public record PostTestConfigurationRequest(Long deliveryId, Status statusMini, String env) {
}
