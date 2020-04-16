package com.seabath.filter;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import spark.Request;
import spark.Response;

class BeforeRouteFilterTest {

    @Test
    public void shouldLog() {
        final Logger mock = mock(Logger.class);
        final BeforeRouteFilter beforeRouteFilter = new BeforeRouteFilter(mock);
        beforeRouteFilter.handle(mock(Request.class), mock(Response.class));

        verify(mock, atLeastOnce()).info(anyString());
    }

}