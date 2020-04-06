package factory;

import pojo.entity.TestRunEntity;
import pojo.test.run.GetTestRunResponse;

public class TestRunFactory {
    public static GetTestRunResponse build(TestRunEntity testEntity) {
        if (testEntity == null) {
            return null;
        }
        return GetTestRunResponse.builder()
            .id(testEntity.getId())
            .endDate(testEntity.getEndDate())
            .startDate(testEntity.getStartDate())
            .status(testEntity.getStatus())
            .build();
    }
}
