package service;

import common.Status;
import dao.TestDAO;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.NonNull;
import org.hibernate.Hibernate;
import pojo.entity.TestEntity;
import pojo.entity.TestRunEntity;
import pojo.entity.TestSuiteEntity;

public class TestService extends SimpleService<TestEntity> {

    private final TestRunService testRunService;
    private final TestDAO testDAO;

    public TestService(TestDAO testDAO, TestRunService testRunService) {
        super(testDAO);
        this.testDAO = testDAO;
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
            .forEach(testRunEntity1 -> testRunService.finish(testRunEntity1, Status.INTERRUPTED));
        testEntity.setStatus(Status.INTERRUPTED);
        this.createOrUpdate(testEntity);
    }

    /**
     * Start a test entity by creating it, or getting it from database, and attaches it a new test
     * run entity.
     *
     * @param testSuiteEntity Test suite to attach it to.
     * @param testName        Name of the launched test.
     * @param packageName     Package of the launched test.
     * @return TestEntity created or retrieved from the database.
     */
    public TestEntity startTest(@NonNull TestSuiteEntity testSuiteEntity, String testName, String packageName) {
        TestEntity testEntity = this.get(testSuiteEntity.getId(), testName,
            packageName);

        if (testEntity == null) {
            testEntity = TestEntity.builder()
                .packageName(packageName)
                .testName(testName)
                .testSuiteEntity(testSuiteEntity)
                .testRunEntities(new HashSet<>())
                .build();
            testSuiteEntity.addTest(testEntity);
        }

        testEntity.setStatus(Status.IN_PROGRESS);
        final TestRunEntity runEntity = TestRunEntity.builder()
            .status(Status.IN_PROGRESS)
            .startDate(new Date())
            .testEntity(testEntity)
            .build();
        testEntity.addRun(runEntity);

        this.createOrUpdate(testEntity);
        return testEntity;
    }

    /**
     * Set status to success, and succeed all test runs linked to given entity.
     *
     * @param testEntity entity to succeed
     */
    public void succeed(@NonNull TestEntity testEntity) {
        final Status status = testEntity.getStatus();
        testEntity.setStatus(status.changeSuccess());

        testEntity.getTestRunEntities()
            .parallelStream()
            .peek(Hibernate::initialize)
            .filter(testRunEntity -> testRunEntity.getStatus() == Status.IN_PROGRESS)
            .forEach(testRunEntity -> testRunService.finish(testRunEntity, Status.SUCCESS));
        this.createOrUpdate(testEntity);
    }

    /**
     * Get a test entity from a suite with given testName and packageName.
     *
     * @param idSuite     Suite's id containing wanted test
     * @param testName    Name of the test
     * @param packageName Package name of the test
     * @return Matching test entity
     */
    public TestEntity get(Long idSuite, String testName, String packageName) {
        return executeQuery(session -> testDAO.find(session, idSuite, testName, packageName));
    }
}
