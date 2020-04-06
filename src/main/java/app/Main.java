package app;

import com.beerboy.ss.SparkSwagger;
import com.beerboy.ss.rest.Endpoint;
import dao.SimpleDAO;
import dao.TestDAO;
import endpoint.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojo.entity.DeliveryEntity;
import pojo.entity.TestConfigurationEntity;
import pojo.entity.TestRunEntity;
import pojo.entity.TestSuiteEntity;
import service.SimpleService;
import service.TestRunService;
import service.TestService;
import service.TestSuiteService;
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


        final SparkSwagger sparkSwagger = SparkSwagger.of(sparkService, SparkSwagger.CONF_FILE_NAME)
            .endpoints(() -> endpoints);

        generateDoc(sparkSwagger);


        logger.trace("Spark service started");
    }


    static void generateDoc(SparkSwagger sparkSwagger) {
        try {
            sparkSwagger.generateDoc();
        } catch (IOException e) {
            logger.error("Error while generating swagger.", e);
        }
    }

    static void stopServer() {
        sparkService.stop();
    }
}
