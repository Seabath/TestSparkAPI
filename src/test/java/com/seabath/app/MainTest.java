package com.seabath.app;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import spark.Service;

class MainTest {

    public static final String TEST_URL = "http://localhost:" + Service.SPARK_DEFAULT_PORT;

    @Test
    public void shouldPassMain() throws UnirestException, InterruptedException {
        Main.main(null);

        HttpResponse<String> response = Unirest.get(TEST_URL)
            .asString();

        assertThat(response.getStatus())
            .isEqualTo(200);

        Main.stopServer();
        Thread.sleep(10000);

        Assertions.assertThrows(
            UnirestException.class,
            () -> Unirest.get(TEST_URL)
                .asString()
        );
    }

    @Test
    public void shouldInstanciateMain() {
        new Main();
    }
}