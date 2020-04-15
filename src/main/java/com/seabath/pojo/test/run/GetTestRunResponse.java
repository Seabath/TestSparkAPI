package com.seabath.pojo.test.run;

import com.seabath.common.Status;
import java.util.Date;

public record GetTestRunResponse(Long id, Date startDate, Date endDate, Status status) {
}
