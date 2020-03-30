package pojo.entity;

import common.Status;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;

/**
 * Gathers all informations about you tests suite.
 */
@Getter
@Entity
@Table(name = "test_configuration")
public class TestConfigurationEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_delivery_entity_id", nullable = false)
    private DeliveryEntity deliveryEntity;

    @Column(name = "minimal_status")
    @Enumerated(EnumType.STRING)
    private Status statusMini;

    @Column(name = "env")
    private String env;

    @OneToMany(mappedBy = "testConfigurationEntity", fetch = FetchType.LAZY,
        cascade = CascadeType.ALL)
    private Set<TestSuiteEntity> testSuiteEntities;

    public TestConfigurationEntity() {
    }

    @Builder
    public TestConfigurationEntity(Long id, DeliveryEntity deliveryEntity, Status statusMini,
                                   String env, Set<TestSuiteEntity> testSuiteEntities) {
        this.id = id;
        this.deliveryEntity = deliveryEntity;
        this.statusMini = statusMini;
        this.env = env;
        this.testSuiteEntities = testSuiteEntities;
    }
}
