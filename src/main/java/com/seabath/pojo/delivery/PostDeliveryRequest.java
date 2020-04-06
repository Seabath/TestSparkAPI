package com.seabath.pojo.delivery;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDeliveryRequest {

    private String version;
    private String platform;
    private String path;

}
