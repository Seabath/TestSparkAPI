package com.seabath.common;

import static com.seabath.common.Status.*;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StatusTest {

    @ParameterizedTest
    @MethodSource("argumentsSetSucceed")
    public void shouldSucceed(Status input, Status expected) {
        final Status result = input.changeSuccess();

        assertThat(result)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> argumentsSetSucceed() {
        return Stream.of(
            Arguments.of(SUCCESS, SUCCESS),
            Arguments.of(FLAKY, FLAKY),
            Arguments.of(FAIL, FLAKY),
            Arguments.of(IN_PROGRESS, SUCCESS),
            Arguments.of(INTERRUPTED, INTERRUPTED)
        );
    }

}