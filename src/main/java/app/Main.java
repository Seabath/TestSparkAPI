package app;

import com.beerboy.ss.SparkSwagger;
import com.beerboy.ss.rest.Endpoint;
import endpoint.DeliveryEndpoint;
import endpoint.ExceptionEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Service;
import static spark.Service.ignite;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static Service sparkService;

    public static void main(String[] args) {
        logger.trace("Starting spark service");
        sparkService = ignite();
        sparkService.port(Service.SPARK_DEFAULT_PORT);


        final List<Endpoint> endpoints = Arrays.asList(
            new ExceptionEndpoint(),
            new DeliveryEndpoint()
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
