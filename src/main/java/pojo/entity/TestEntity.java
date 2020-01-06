package pojo.entity;

import common.Status;
import java.util.Set;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "test_run")
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "test_name")
    private String testName;

    @Column(name = "class_name")
    private String className;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "fk_test_suite_entity_id", nullable = false)
    private TestSuiteEntity testSuiteEntity;

    @OneToMany(mappedBy = "testEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<TestRunEntity> testRunEntities;

    public TestEntity() {
    }

    @Builder
    public TestEntity(Long id, String testName, String className, Status status,
                      TestSuiteEntity testSuiteEntity, Set<TestRunEntity> testRunEntities) {
        this.id = id;
        this.testName = testName;
        this.className = className;
        this.status = status;
        this.testSuiteEntity = testSuiteEntity;
        this.testRunEntities = testRunEntities;
    }
}