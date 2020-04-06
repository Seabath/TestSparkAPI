package com.seabath.service;

import com.seabath.common.Status;
import com.seabath.dao.SimpleDAO;
import java.lang.reflect.Field;
import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import com.seabath.pojo.entity.TestRunEntity;

class TestRunServiceTest {


    private TestRunService service;
    private SimpleDAO<TestRunEntity> mockedDAO;
    private Session mockedSession;

    @BeforeEach
    public void initService() throws NoSuchFieldException, IllegalAccessException {
        mockedDAO = mock(SimpleDAO.class);
        mockedSession = mock(Session.class);
        service = spy(new TestRunService(mockedDAO));

        SessionFactory mockedSessionFactory = mock(SessionFactory.class);
        final Field sessionFactoryField = SimpleDAO.class.getDeclaredField("sessionFactory");
        sessionFactoryField.setAccessible(true);
        sessionFactoryField.set(null, mockedSessionFactory);

        when(mockedSessionFactory.openSession()).thenReturn(mockedSession);
        when(mockedDAO.openSession()).thenReturn(mockedSession);
    }

    @Test
    public void shouldTestFinish() {
        final TestRunEntity testRunEntity = TestRunEntity.builder().build();
        final Status expectedStatus = Status.INTERRUPTED;

        service.finish(testRunEntity, expectedStatus);

        assertThat(testRunEntity.getStatus())
            .isEqualTo(expectedStatus);
        assertThat(testRunEntity.getEndDate())
            .isNotNull();
        verify(service, times(1)).createOrUpdate(eq(testRunEntity));
    }

}