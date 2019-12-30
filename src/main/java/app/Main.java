package app;

import com.beerboy.ss.SparkSwagger;
import com.beerboy.ss.rest.Endpoint;
import endpoint.DeliveryEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Service;
import static spark.Service.ignite;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.trace("Starting service");


        Service sparkService = ignite();
        sparkService.port(Service.SPARK_DEFAULT_PORT);


        final List<Endpoint> endpoints = Arrays.asList(
            new DeliveryEndpoint()
        );


        try {
            SparkSwagger.of(sparkService, SparkSwagger.CONF_FILE_NAME)
                .endpoints(() -> endpoints)
                .generateDoc();
        } catch (IOException e) {
            logger.error("Error while generating swagger.", e);
        }


        logger.trace("Service started");
    }
}
