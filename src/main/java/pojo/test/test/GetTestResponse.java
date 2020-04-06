package pojo.test.test;

import common.Status;
import java.util.Set;
import lombok.Builder;
import pojo.test.run.GetTestRunResponse;

@Builder
public class GetTestResponse {

    private Long id;
    private String testName;
    private String packageName;
    private Status status;
    private Long testSuiteId;

    private Set<GetTestRunResponse> testRuns;
}
