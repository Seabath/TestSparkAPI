package com.seabath.pojo.test.suite;

import com.seabath.common.Status;
import com.seabath.pojo.test.configuration.GetTestConfigurationResponse;
import java.util.Date;

public record GetTestSuiteResponse(Long id, Date startDate, Date endDate, Status status,
                                   GetTestConfigurationResponse getTestConfigurationResponse) {
}
