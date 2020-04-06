package com.seabath.pojo.test.run;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostStartTestBody {

    private String testName;

    private String packageName;
}
