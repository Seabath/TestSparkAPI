package com.seabath.pojo.entity;

import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;

/**
 * In case of a test failure, it's the entity that record and keeps informations about it.
 */
@Getter
@Entity
@Table(name = "failure")
public class FailureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "stacktrace")
    private String stacktrace;

    @Column(name = "message")
    private String message;

    @Column(name = "java_exception")
    private String exception;

    @ManyToOne
    @JoinColumn(name = "fk_test_run_entity_id", nullable = false)
    private TestRunEntity testRunEntity;

    public FailureEntity() {
    }

    @Builder
    public FailureEntity(Long id, String stacktrace, String message, String exception,
                         TestRunEntity testRunEntity) {
        this.id = id;
        this.stacktrace = stacktrace;
        this.message = message;
        this.exception = exception;
        this.testRunEntity = testRunEntity;
    }
}