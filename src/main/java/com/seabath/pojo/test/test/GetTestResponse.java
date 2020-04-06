package com.seabath.pojo.test.test;

import com.seabath.common.Status;
import java.util.Set;
import lombok.Builder;
import com.seabath.pojo.test.run.GetTestRunResponse;

@Builder
public class GetTestResponse {

    private Long id;
    private String testName;
    private String packageName;
    private Status status;
    private Long testSuiteId;

    private Set<GetTestRunResponse> testRuns;
}
