package pojo.entity;

import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "attachment")
public class AttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "path")
    private String path;

    @ManyToOne
    @JoinColumn(name = "fk_test_run_entity_id", nullable = false)
    private TestRunEntity testRunEntity;

    public AttachmentEntity() {
    }

    @Builder
    public AttachmentEntity(Long id, String type, String description, String path,
                            TestRunEntity testRunEntity) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.path = path;
        this.testRunEntity = testRunEntity;
    }
}