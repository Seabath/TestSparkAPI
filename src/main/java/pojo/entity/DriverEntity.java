package pojo.entity;

import java.util.Date;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;

/**
 * For end to end tests, it's drivers on which you run your test.
 */
@Getter
@Entity
@Table(name = "attachment")
public class DriverEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "request_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "grid_url")
    private String gridUrl;

    @ManyToOne
    @JoinColumn(name = "fk_test_run_entity_id", nullable = false)
    private TestRunEntity testRunEntity;

    public DriverEntity() {
    }

    @Builder
    public DriverEntity(Long id, Date requestDate, Date startDate, Date endDate, String gridUrl,
                        TestRunEntity testRunEntity) {
        this.id = id;
        this.requestDate = requestDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.gridUrl = gridUrl;
        this.testRunEntity = testRunEntity;
    }
}