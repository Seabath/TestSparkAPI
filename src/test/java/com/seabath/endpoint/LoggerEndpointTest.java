package com.seabath.endpoint;

import com.beerboy.ss.SparkSwagger;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoggerEndpointTest {


    @Test
    public void shouldBindLoggers() {
        final SparkSwagger mockedSparkSwagger = mock(SparkSwagger.class);
        new LoggerEndpoint(mock(Logger.class)).bind(mockedSparkSwagger);

        verify(mockedSparkSwagger, times(1)).before(any());
        verify(mockedSparkSwagger, times(1)).after(any());
    }

}