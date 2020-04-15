package com.seabath.factory;

import com.seabath.pojo.entity.TestRunEntity;
import com.seabath.pojo.test.run.GetTestRunResponse;

public class TestRunFactory {
    public static GetTestRunResponse build(TestRunEntity testEntity) {
        if (testEntity == null) {
            return null;
        }
        return new GetTestRunResponse(
            testEntity.getId(),
            testEntity.getStartDate(),
            testEntity.getEndDate(),
            testEntity.getStatus()
        );
    }
}
