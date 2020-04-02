package service;

import common.Status;
import dao.SimpleDAO;
import java.util.Date;
import pojo.entity.TestRunEntity;

public class TestRunService extends SimpleService<TestRunEntity> {
    public TestRunService(SimpleDAO<TestRunEntity> simpleDAO) {
        super(simpleDAO);
    }

    /**
     * Sets status to interrupted and endate to actual date.
     *
     * @param testRunEntity entity to interrupt
     */
    public void interrupt(TestRunEntity testRunEntity) {
        testRunEntity.setEndDate(new Date());
        testRunEntity.setStatus(Status.INTERRUPTED);
        this.createOrUpdate(testRunEntity);
    }
}
