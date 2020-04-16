package com.seabath.filter;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import spark.Request;
import spark.Response;

class AfterAfterRouteFilterTest {

    @Test
    public void shouldLog() {
        final Logger mock = mock(Logger.class);
        final AfterAfterRouteFilter afterAfterRouteFilter = new AfterAfterRouteFilter(mock);
        afterAfterRouteFilter.handle(mock(Request.class), mock(Response.class));

        verify(mock, atLeastOnce()).info(anyString());
    }

}