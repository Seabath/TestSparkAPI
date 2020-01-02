package endpoint;

import com.beerboy.ss.SparkSwagger;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import spark.Service;
import static spark.Service.ignite;

public abstract class AbstractEndPointT {

    private static final int TEST_DEFAULT_PORT = Service.SPARK_DEFAULT_PORT;
    private static Service sparkService;
    protected static SparkSwagger sparkSwagger;

    @BeforeAll
    public static void initSpark() {
        sparkService = ignite();
        sparkService.port(TEST_DEFAULT_PORT);

        sparkSwagger = SparkSwagger.of(sparkService);
        sparkSwagger
            .endpoint(new ExceptionEndpoint());
    }

    @AfterAll
    public static void tearDownSpark() {
        sparkService.stop();
        sparkService.awaitStop();
    }


    protected HttpResponse<String> testPost(String endpointPath, Object requestObject) throws UnirestException {
        String body = new Gson().toJson(requestObject);
        return Unirest.post("http://localhost:" + TEST_DEFAULT_PORT + endpointPath)
            .body(body)
            .asString();
    }

    protected HttpResponse<String> testGet(String endpointPath) throws UnirestException {
        return Unirest.get("http://localhost:" + TEST_DEFAULT_PORT + endpointPath)
            .asString();
    }
}
