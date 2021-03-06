package com.seabath.pojo.entity;

import com.seabath.common.Status;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * This is the entity gathering all tests and containing data about your run as a whole.
 */
@Getter
@Entity
@Table(name = "test_suite")
public class TestSuiteEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Setter
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Setter
    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "fk_test_configuration_entity_id", nullable = false)
    private TestConfigurationEntity testConfigurationEntity;

    @OneToMany(mappedBy = "testSuiteEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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

    /**
     * Adds a test entity to this' collection.
     *
     * @param testEntity entity to add
     */
    public void addTest(TestEntity testEntity) {
        testEntities.add(testEntity);
        testEntity.setTestSuiteEntity(this);
    }
}
