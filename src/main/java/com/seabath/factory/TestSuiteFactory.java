package com.seabath.factory;

import com.seabath.common.Status;
import com.seabath.pojo.entity.TestConfigurationEntity;
import com.seabath.pojo.entity.TestSuiteEntity;
import com.seabath.pojo.test.suite.GetTestSuiteResponse;
import java.util.Date;
/**
 * Factory to build TestSuiteEntity and response bodys related to TestSuiteEntity
 */
public class TestSuiteFactory {

    /**
     * Builds a default TestSuiteEntity.
     * with its status as in progress and its date to actual date.
     *
     * @param testConfigurationEntity Body from a post request.
     * @return TestSuiteEntity
     */
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

    /**
     * Builds a response from a TestSuiteEntity.
     *
     * @param testSuiteEntity Entity from the database.
     * @return GetTestSuiteResponse to put in response body
     */
    public static GetTestSuiteResponse build(TestSuiteEntity testSuiteEntity) {
        if (testSuiteEntity == null) {
            return null;
        }
        return new GetTestSuiteResponse(
            testSuiteEntity.getId(),
            testSuiteEntity.getStartDate(),
            testSuiteEntity.getEndDate(),
            testSuiteEntity.getStatus(),
            TestConfigurationFactory.build(testSuiteEntity.getTestConfigurationEntity())
        );
    }
}
