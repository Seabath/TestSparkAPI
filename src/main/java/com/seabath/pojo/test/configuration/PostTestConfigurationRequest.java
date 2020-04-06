package com.seabath.pojo.test.configuration;

import com.seabath.common.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostTestConfigurationRequest {

    private Long deliveryId;
    private Status statusMini;
    private String env;
}
