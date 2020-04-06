package com.seabath.pojo.entity;

import com.seabath.common.Status;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * This is the information about your test run. In case of multiple run, it contains only generic
 * information about it like it's name.
 */
@Getter
@Entity
@Table(name = "test")
public class TestEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "test_name")
    private String testName;

    @Column(name = "package_name")
    private String packageName;

    @Setter
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Setter
    @ManyToOne
    @JoinColumn(name = "fk_test_suite_entity_id", nullable = false)
    private TestSuiteEntity testSuiteEntity;

    @OneToMany(mappedBy = "testEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<TestRunEntity> testRunEntities;

    public TestEntity() {
    }

    @Builder
    public TestEntity(Long id, String testName, String packageName, Status status,
                      TestSuiteEntity testSuiteEntity, Set<TestRunEntity> testRunEntities) {
        this.id = id;
        this.testName = testName;
        this.packageName = packageName;
        this.status = status;
        this.testSuiteEntity = testSuiteEntity;
        this.testRunEntities = testRunEntities;
    }

    /**
     * Adds a test run entity to this' collections.
     *
     * @param testRunEntity entity to add
     */
    public void addRun(TestRunEntity testRunEntity) {
        testRunEntities.add(testRunEntity);
        testRunEntity.setTestEntity(this);
    }
}
