package com.seabath.factory;

import com.seabath.pojo.entity.TestEntity;
import com.seabath.pojo.test.run.GetTestRunResponse;
import com.seabath.pojo.test.test.GetTestResponse;
import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.Hibernate;

public class TestFactory {
    public static GetTestResponse build(TestEntity testEntity) {
        if (testEntity == null) {
            return null;
        }
        final Set<GetTestRunResponse> testRuns = testEntity.getTestRunEntities()
            .parallelStream()
            .peek(Hibernate::initialize)
            .map(TestRunFactory::build)
            .collect(Collectors.toSet());

        return new GetTestResponse(
            testEntity.getId(),
            testEntity.getTestName(),
            testEntity.getPackageName(),
            testEntity.getStatus(),
            testEntity.getTestSuiteEntity().getId(),
            testRuns
        );
    }
}
