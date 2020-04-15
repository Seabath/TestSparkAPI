package com.seabath.factory;

import com.seabath.pojo.entity.DeliveryEntity;
import com.seabath.pojo.entity.TestConfigurationEntity;
import com.seabath.pojo.test.configuration.GetTestConfigurationResponse;
import com.seabath.pojo.test.configuration.PostTestConfigurationRequest;
import java.util.Collections;

/**
 * Factory to build TestConfigurationEntity and response bodys related to TestConfigurationEntity
 */
public class TestConfigurationFactory {

    /**
     * Builds a DeliveryEntity from a request's body and its delivery
     *
     * @param postTestConfigurationRequest Body from a post request.
     * @param deliveryEntity               Delivery entity on which the configuration
     * @return TestConfigurationEntity
     */
    public static TestConfigurationEntity build(PostTestConfigurationRequest postTestConfigurationRequest, DeliveryEntity deliveryEntity) {
        if (postTestConfigurationRequest == null) {
            return null;
        }
        return TestConfigurationEntity.builder()
            .deliveryEntity(deliveryEntity)
            .env(postTestConfigurationRequest.env())
            .statusMini(postTestConfigurationRequest.statusMini())
            .testSuiteEntities(Collections.emptySet())
            .build();
    }

    /**
     * Builds a response from a TestConfigurationEntity.
     *
     * @param testConfigurationEntity Body from a post request.
     * @return GetTestConfigurationResponse to put in response body
     */
    public static GetTestConfigurationResponse build(TestConfigurationEntity testConfigurationEntity) {
        if (testConfigurationEntity == null) {
            return null;
        }
        return new GetTestConfigurationResponse(
            testConfigurationEntity.getId(),
            testConfigurationEntity.getStatusMini(),
            DeliveryFactory.build(testConfigurationEntity.getDeliveryEntity()),
            testConfigurationEntity.getEnv()
        );
    }
}
