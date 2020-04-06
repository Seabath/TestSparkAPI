package com.seabath.factory;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class TestConfigurationFactoryTest {

    @Test
    public void should() {
        new TestConfigurationFactory();
    }


    @Test
    public void shouldGetNullEntity() {
        assertThat(TestConfigurationFactory.build(null, null))
            .isNull();
    }

    @Test
    public void shouldGetNullResponse() {
        assertThat(TestConfigurationFactory.build(null))
            .isNull();
    }
}