package service;

import common.Status;
import dao.SimpleDAO;
import java.util.Set;
import org.hibernate.Hibernate;
import pojo.entity.TestEntity;
import pojo.entity.TestRunEntity;

public class TestService extends SimpleService<TestEntity> {

    private final TestRunService testRunService;

    public TestService(SimpleDAO<TestEntity> simpleDAO, TestRunService testRunService) {
        super(simpleDAO);
        this.testRunService = testRunService;
    }

    /**
     * Set status to interrupted, and interrupts all test runs linked to given entity.
     *
     * @param testEntity entity to interrupt
     */
    public void interrupt(TestEntity testEntity) {
        final Set<TestRunEntity> testRunEntities = testEntity.getTestRunEntities();
        testRunEntities.parallelStream()
            .peek(Hibernate::initialize)
            .filter(testRunEntity -> testRunEntity.getStatus() == Status.IN_PROGRESS)
            .forEach(testRunService::interrupt);
        testEntity.setStatus(Status.INTERRUPTED);
        this.createOrUpdate(testEntity);
    }
}
