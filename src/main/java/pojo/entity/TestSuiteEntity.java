package pojo.entity;

import common.Status;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "test_suite")
public class TestSuiteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "fk_test_configuration_entity_id", nullable = false)
    private TestConfigurationEntity testConfigurationEntity;

    @OneToMany(mappedBy = "suiteEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<TestEntity> testEntities;


    public TestSuiteEntity() {
    }

    @Builder
    public TestSuiteEntity(Long id, Date startDate, Date endDate, Status status,
                           TestConfigurationEntity testConfigurationEntity, Set<TestEntity> testEntities) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.testConfigurationEntity = testConfigurationEntity;
        this.testEntities = testEntities;
    }
}
