package pojo.entities;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DeliveryEntity {

    private Long id;
    private String version;
    private String platform;
    private String path;
    private Date date;
}
