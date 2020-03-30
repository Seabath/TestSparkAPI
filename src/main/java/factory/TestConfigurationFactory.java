package factory;

import java.util.Collections;
import pojo.entity.DeliveryEntity;
import pojo.entity.TestConfigurationEntity;
import pojo.test.configuration.GetTestConfigurationResponse;
import pojo.test.configuration.PostTestConfigurationRequest;

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
            .env(postTestConfigurationRequest.getEnv())
            .statusMini(postTestConfigurationRequest.getStatusMini())
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
        return GetTestConfigurationResponse.builder()
            .id(testConfigurationEntity.getId())
            .getDeliveryResponse(DeliveryFactory.build(testConfigurationEntity.getDeliveryEntity()))
            .env(testConfigurationEntity.getEnv())
            .statusMini(testConfigurationEntity.getStatusMini())
            .build();

    }
}
