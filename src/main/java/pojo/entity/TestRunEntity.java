package pojo.entity;

import common.Status;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;

/**
 * Coupled to the TestEntity, it's the entity that will record everything on a specific test run.
 */
@Getter
@Entity
@Table(name = "test_run")
public class TestRunEntity {

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
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "fk_test_entity_id", nullable = false)
    private TestEntity testEntity;

    @OneToMany(mappedBy = "testRunEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<AttachmentEntity> attachmentEntities;

    @OneToMany(mappedBy = "testRunEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<FailureEntity> failureEntities;

    @OneToMany(mappedBy = "testRunEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<DriverEntity> driverEntities;

    public TestRunEntity() {
    }

    @Builder
    public TestRunEntity(Long id, Date startDate, Date endDate, Status status,
                         TestEntity testEntity, Set<AttachmentEntity> attachmentEntities,
                         Set<FailureEntity> failureEntities, Set<DriverEntity> driverEntities) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.testEntity = testEntity;
        this.attachmentEntities = attachmentEntities;
        this.failureEntities = failureEntities;
        this.driverEntities = driverEntities;
    }
}
