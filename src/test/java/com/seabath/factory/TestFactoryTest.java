package com.seabath.factory;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import com.seabath.pojo.test.test.GetTestResponse;

class TestFactoryTest {

    @Test
    public void should() {
        new TestFactory();
    }

    @Test
    public void shouldReturnNullResponse() {
        final GetTestResponse response = TestFactory.build(null);
        assertThat(response)
            .isNull();
    }
}