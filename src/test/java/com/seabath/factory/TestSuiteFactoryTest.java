package com.seabath.factory;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import com.seabath.pojo.entity.TestConfigurationEntity;
import com.seabath.pojo.entity.TestSuiteEntity;

class TestSuiteFactoryTest {

    @Test
    public void should() {
        new TestSuiteFactory();
    }

    @Test
    public void shouldReturnNullResponse() {
        TestSuiteEntity entity = null;
        assertThat(TestSuiteFactory.build(entity))
            .isNull();
    }


    @Test
    public void shouldReturnNullEntity() {
        TestConfigurationEntity entity = null;
        assertThat(TestSuiteFactory.build(entity))
            .isNull();
    }

}