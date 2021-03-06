package com.seabath.app;

import com.beerboy.ss.SparkSwagger;
import com.beerboy.ss.rest.Endpoint;
import com.seabath.dao.SimpleDAO;
import com.seabath.dao.TestDAO;
import com.seabath.endpoint.*;
import com.seabath.pojo.entity.DeliveryEntity;
import com.seabath.pojo.entity.TestConfigurationEntity;
import com.seabath.pojo.entity.TestRunEntity;
import com.seabath.pojo.entity.TestSuiteEntity;
import com.seabath.service.SimpleService;
import com.seabath.service.TestRunService;
import com.seabath.service.TestService;
import com.seabath.service.TestSuiteService;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Service;
import static spark.Service.ignite;

/**
 * Entry point of the service.
 */
public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static Service sparkService;

    public static void main(String[] args) {
        logger.trace("Starting spark service");
        sparkService = ignite();
        sparkService.port(Service.SPARK_DEFAULT_PORT);


        // DAO declarations
        final SimpleDAO<DeliveryEntity> deliveryDAO = new SimpleDAO<>(DeliveryEntity.class);
        final SimpleDAO<TestConfigurationEntity> testConfigurationDAO =
            new SimpleDAO<>(TestConfigurationEntity.class);
        final SimpleDAO<TestSuiteEntity> testSuiteDAO = new SimpleDAO<>(TestSuiteEntity.class);
        final TestDAO testEntityDAO = new TestDAO();
        final SimpleDAO<TestRunEntity> testRunEntityDAO = new SimpleDAO<>(TestRunEntity.class);

        // Service declarations
        final SimpleService<DeliveryEntity> deliveryService =
            new SimpleService<>(deliveryDAO);
        final SimpleService<TestConfigurationEntity> testConfigurationService =
            new SimpleService<>(testConfigurationDAO);
        final TestRunService testRunService = new TestRunService(testRunEntityDAO);
        final TestService testService = new TestService(testEntityDAO, testRunService);
        final TestSuiteService testSuiteService = new TestSuiteService(testSuiteDAO, testService);

        // Route mapping
        final List<Endpoint> endpoints = Arrays.asList(
            new LoggerEndpoint(logger),
            new ExceptionEndpoint(),
            new DeliveryEndpoint(deliveryService),
            new TestConfigurationEndpoint(testConfigurationService, deliveryService),
            new TestSuiteEndpoint(testSuiteService, testConfigurationService),
            new TestEndpoint(testSuiteService, testService)
        );


        SparkSwagger.of(sparkService, SparkSwagger.CONF_FILE_NAME)
            .endpoints(() -> endpoints);


        logger.trace("Spark service started");
    }

    static void stopServer() {
        sparkService.stop();
    }
}
