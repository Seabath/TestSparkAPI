package factory;

import common.Status;
import java.util.Date;
import pojo.entity.TestConfigurationEntity;
import pojo.entity.TestSuiteEntity;
import pojo.test.suite.GetTestSuiteResponse;

public class TestSuiteFactory {

    public static TestSuiteEntity build(TestConfigurationEntity testConfigurationEntity) {
        if (testConfigurationEntity == null) {
            return null;
        }
        return TestSuiteEntity.builder()
            .startDate(new Date())
            .status(Status.IN_PROGRESS)
            .testConfigurationEntity(testConfigurationEntity)
            .build();
    }


    public static GetTestSuiteResponse build(TestSuiteEntity testSuiteEntity) {
        if (testSuiteEntity == null) {
            return null;
        }
        return GetTestSuiteResponse.builder()
            .id(testSuiteEntity.getId())
            .startDate(testSuiteEntity.getStartDate())
            .endDate(testSuiteEntity.getEndDate())
            .status(testSuiteEntity.getStatus())
            .getTestConfigurationResponse(TestConfigurationFactory.build(testSuiteEntity.getTestConfigurationEntity()))
            .build();
    }
}
