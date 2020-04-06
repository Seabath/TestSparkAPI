package pojo.test.run;

import common.Status;
import java.util.Date;
import lombok.Builder;

@Builder
public class GetTestRunResponse {

    private Long id;
    private Date startDate;
    private Date endDate;
    private Status status;
}
