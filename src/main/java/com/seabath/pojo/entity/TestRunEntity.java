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
 * Coupled to the TestEntity, it's the entity that will record everything on a specific test run.
 */
@Getter
@Entity
@Table(name = "test_run")
public class TestRunEntity implements Serializable {

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
    @Enumerated(EnumType.STRING)
    private Status status;

    @Setter
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
