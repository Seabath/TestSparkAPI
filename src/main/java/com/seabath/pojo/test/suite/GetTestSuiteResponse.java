package com.seabath.pojo.test.suite;

import com.seabath.common.Status;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import com.seabath.pojo.test.configuration.GetTestConfigurationResponse;

@Getter
@Builder
public class GetTestSuiteResponse {
    private Long id;
    private Date startDate;
    private Date endDate;
    private Status status;
    private GetTestConfigurationResponse getTestConfigurationResponse;
}
