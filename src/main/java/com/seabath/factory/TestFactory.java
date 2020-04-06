package com.seabath.factory;

import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.Hibernate;
import com.seabath.pojo.entity.TestEntity;
import com.seabath.pojo.test.run.GetTestRunResponse;
import com.seabath.pojo.test.test.GetTestResponse;

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

        return GetTestResponse.builder()
            .id(testEntity.getId())
            .testSuiteId(testEntity.getTestSuiteEntity().getId())
            .testName(testEntity.getTestName())
            .packageName(testEntity.getPackageName())
            .testRuns(testRuns)
            .build();
    }
}
