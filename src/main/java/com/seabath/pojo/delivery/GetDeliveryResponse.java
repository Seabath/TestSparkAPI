package com.seabath.pojo.delivery;

import java.util.Date;

public record GetDeliveryResponse(Long id, String version, String platform, String path,
                                  Date date) {
}
