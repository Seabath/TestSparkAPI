package pojo.test.suite;

import common.Status;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import pojo.test.configuration.GetTestConfigurationResponse;

@Getter
@Builder
public class GetTestSuiteResponse {
    private Long id;
    private Date startDate;
    private Date endDate;
    private Status status;
    private GetTestConfigurationResponse getTestConfigurationResponse;
}
