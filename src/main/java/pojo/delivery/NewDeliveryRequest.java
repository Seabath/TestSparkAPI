package pojo.delivery;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewDeliveryRequest {

    private String version;
    private String platform;
    private String path;

}
