package pojo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;

/**
 * This is the application delivered by your developpement teams.
 */
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

    @OneToMany(mappedBy = "deliveryEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<TestConfigurationEntity> testConfigurationEntities;

    public DeliveryEntity() {
    }

    @Builder
    public DeliveryEntity(Long id, String version, String platform, String path, Date date,
                          Set<TestConfigurationEntity> testConfigurationEntities) {
        this.id = id;
        this.version = version;
        this.platform = platform;
        this.path = path;
        this.date = date;
        this.testConfigurationEntities = testConfigurationEntities;
    }
}
