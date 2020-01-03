package pojo.test.configuration;

import common.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostTestConfigurationRequest {

    private Long deliveryId;
    private Status statusMini;
    private String env;
}
