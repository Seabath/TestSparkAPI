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
     * Finish with given status and endate to actual date.
     *
     * @param testRunEntity entity to interrupt
     * @param status        Status to set
     */
    public void finish(TestRunEntity testRunEntity, Status status) {
        testRunEntity.setEndDate(new Date());
        testRunEntity.setStatus(status);
        this.createOrUpdate(testRunEntity);
    }
}
