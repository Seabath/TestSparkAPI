package service;

import common.Status;
import dao.SimpleDAO;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import org.hibernate.Hibernate;
import pojo.entity.TestEntity;
import pojo.entity.TestSuiteEntity;

public class TestSuiteService extends SimpleService<TestSuiteEntity> {

    private final TestService testService;

    public TestSuiteService(SimpleDAO<TestSuiteEntity> simpleDAO, TestService testService) {
        super(simpleDAO);
        this.testService = testService;
    }

    /**
     * Compute and update status of given entity according to its linked tests.
     * If at least one test didn't finished before updating suite status, the whole suite will be
     * marked as interrupted, and also non-finished tests.
     *
     * @param testSuiteEntity entity to update status.
     */
    public void updateStatus(TestSuiteEntity testSuiteEntity) {
        testSuiteEntity.setEndDate(new Date());

        final Set<TestEntity> testEntities = testSuiteEntity.getTestEntities();
        testEntities.forEach(Hibernate::initialize);

        Status testSuiteStatus = testEntities.parallelStream()
            .map(TestEntity::getStatus)
            .max(Comparator.comparingInt(Status::getValue))
            .orElse(Status.SUCCESS);


        if (testSuiteStatus == Status.IN_PROGRESS) {
            testSuiteStatus = Status.INTERRUPTED;
            testEntities.parallelStream()
                .filter(testEntity -> testEntity.getStatus() == Status.IN_PROGRESS)
                .forEach(testService::interrupt);
        }

        testSuiteEntity.setStatus(testSuiteStatus);
        this.createOrUpdate(testSuiteEntity);
    }
}
