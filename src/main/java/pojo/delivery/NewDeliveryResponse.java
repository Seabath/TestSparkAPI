package pojo.delivery;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewDeliveryResponse {

    private Long id;
    private String version;
    private String platform;
    private String path;
    private Date date;
}
