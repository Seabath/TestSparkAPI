package com.seabath.pojo.delivery;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetDeliveryResponse {

    private Long id;
    private String version;
    private String platform;
    private String path;
    private Date date;
}
