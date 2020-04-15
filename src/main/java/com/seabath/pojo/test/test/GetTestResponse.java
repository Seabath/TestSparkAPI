package com.seabath.pojo.test.test;

import com.seabath.common.Status;
import com.seabath.pojo.test.run.GetTestRunResponse;
import java.util.Set;

public record GetTestResponse(Long id, String testName, String packageName, Status status,
                              Long testSuiteId, Set<GetTestRunResponse>testRuns) {
}
