package endpoint;

import com.beerboy.ss.SparkSwagger;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.BeforeAll;
import spark.Service;
import static spark.Service.ignite;

public abstract class AbstractEndPointTest {

    private static final int TEST_DEFAULT_PORT = Service.SPARK_DEFAULT_PORT;
    private static Service sparkService;
    protected static SparkSwagger sparkSwagger;

    private static boolean isStarted;

    @BeforeAll
    public static void initSpark() {
        if (!isStarted) {
            sparkService = ignite();
            sparkService.port(TEST_DEFAULT_PORT);

            sparkSwagger = SparkSwagger.of(sparkService);
            sparkSwagger.endpoint(new ExceptionEndpoint());

            sparkService.awaitInitialization();
            isStarted = true;
        }
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
