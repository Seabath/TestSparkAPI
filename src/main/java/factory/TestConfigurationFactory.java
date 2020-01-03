package factory;

import java.util.Collections;
import pojo.entity.DeliveryEntity;
import pojo.entity.TestConfigurationEntity;
import pojo.test.configuration.GetTestConfigurationResponse;
import pojo.test.configuration.PostTestConfigurationRequest;

public class TestConfigurationFactory {
    public static TestConfigurationEntity build(PostTestConfigurationRequest postTestConfigurationRequest, DeliveryEntity deliveryEntity) {
        return TestConfigurationEntity.builder()
            .deliveryEntity(deliveryEntity)
            .env(postTestConfigurationRequest.getEnv())
            .statusMini(postTestConfigurationRequest.getStatusMini())
            .testSuiteEntities(Collections.emptySet())
            .build();
    }

    public static GetTestConfigurationResponse build(TestConfigurationEntity testConfigurationEntity) {
        return GetTestConfigurationResponse.builder()
            .id(testConfigurationEntity.getId())
            .getDeliveryResponse(DeliveryFactory.build(testConfigurationEntity.getDeliveryEntity()))
            .env(testConfigurationEntity.getEnv())
            .statusMini(testConfigurationEntity.getStatusMini())
            .build();

    }
}
