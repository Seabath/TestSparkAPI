package endpoint;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import javassist.NotFoundException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

class ExceptionEndpointTest extends AbstractEndPointTest {


    public static final String EXCEPTION_ROUTE = "/exception";
    public static final String NOT_FOUND_EXCEPTION_ROUTE = "/not_found_exception";

    @BeforeAll
    public static void initService() {
        ExceptionEndpoint endpoint = new ExceptionEndpoint();

        sparkSwagger.endpoint(endpoint);
        sparkSwagger.getSpark().get(EXCEPTION_ROUTE, ExceptionEndpointTest::throwException);
        sparkSwagger.getSpark().get(NOT_FOUND_EXCEPTION_ROUTE, ExceptionEndpointTest::throwNotFoundException);
    }

    @Test
    public void should_catch_500() throws UnirestException {
        final HttpResponse<String> stringHttpResponse = testGet(EXCEPTION_ROUTE);
        assertThat(stringHttpResponse.getStatus())
            .isEqualTo(500);
    }

    @Test
    public void should_catch_404() throws UnirestException {
        final HttpResponse<String> stringHttpResponse = testGet(NOT_FOUND_EXCEPTION_ROUTE);
        assertThat(stringHttpResponse.getStatus())
            .isEqualTo(404);
    }

    private static Object throwException(Request request, Response response) throws Exception {
        throw new Exception();
    }

    private static Object throwNotFoundException(Request request, Response response) throws Exception {
        throw new NotFoundException("Blo");
    }

}