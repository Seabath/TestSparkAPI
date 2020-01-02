package pojo.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "delivery")
public class DeliveryEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "version")
    private String version;

    @Column(name = "platform")
    private String platform;

    @Column(name = "path")
    private String path;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public DeliveryEntity() {
    }

    @Builder
    public DeliveryEntity(Long id, String version, String platform, String path, Date date) {
        this.id = id;
        this.version = version;
        this.platform = platform;
        this.path = path;
        this.date = date;
    }
}
