package factory;

import java.util.Set;
import java.util.stream.Collectors;
import org.hibernate.Hibernate;
import pojo.entity.TestEntity;
import pojo.test.run.GetTestRunResponse;
import pojo.test.test.GetTestResponse;

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
