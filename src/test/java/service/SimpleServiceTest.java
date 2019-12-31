package service;

import dao.SimpleDAO;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class SimpleServiceTest {

    private SimpleService<String> simpleService;

    private SimpleDAO<String> mockedDAO;
    private Session mockedSession;
    private SessionFactory mockedSessionFactory;
    private Transaction mockedTransaction;

    @BeforeEach
    public void setUp() throws IllegalAccessException, NoSuchFieldException {
        mockedDAO = mock(SimpleDAO.class);
        mockedSession = mock(Session.class);
        mockedSessionFactory = mock(SessionFactory.class);
        mockedTransaction = mock(Transaction.class);

        final Field sessionFactoryField = SimpleDAO.class.getDeclaredField("sessionFactory");
        sessionFactoryField.setAccessible(true);
        sessionFactoryField.set(null, mockedSessionFactory);

        when(mockedSessionFactory.openSession()).thenReturn(mockedSession);
        when(mockedDAO.openSession()).thenReturn(mockedSession);

        simpleService = new SimpleService<>(mockedDAO);
    }

    @Test
    public void should_get_a_entity() {
        final long id = 0L;
        final String expected = "42L";
        when(mockedDAO.find(any(), eq(id))).thenReturn(expected);
        final String result = simpleService.get(id);

        assertThat(result)
            .isEqualTo(expected);
    }

    @Test
    public void should_get_all_entities() {
        final List<String> expected = Collections.singletonList("42L");
        when(mockedDAO.findAll(any())).thenReturn(expected);
        final List<String> result = simpleService.getAll();

        assertThat(result)
            .isEqualTo(expected);
    }

    @Test
    public void should_create_entity() {
        final String expected = "42L";
        when(mockedSession.beginTransaction()).thenReturn(mockedTransaction);
        when(mockedDAO.save(any(), eq(expected))).thenReturn(expected);
        final String result = simpleService.create(expected);

        assertThat(result)
            .isEqualTo(expected);
    }

    @Test
    public void should_create_or_update_entity() {
        final String expected = "42L";
        when(mockedSession.beginTransaction()).thenReturn(mockedTransaction);
        when(mockedDAO.saveOrUodate(any(), eq(expected))).thenReturn(expected);
        final String result = simpleService.createOrUpdate(expected);

        assertThat(result)
            .isEqualTo(expected);
    }

    @Test
    public void should_delete_entity() {
        final String expected = "42L";
        when(mockedSession.beginTransaction()).thenReturn(mockedTransaction);
        when(mockedDAO.delete(any(), eq(expected))).thenReturn(expected);
        final String result = simpleService.delete(expected);

        assertThat(result)
            .isEqualTo(expected);
    }

    @Test
    public void should_delete_entity_id() {
        Long id = 42L;
        final String expected = "42L";
        when(mockedSession.beginTransaction()).thenReturn(mockedTransaction);
        when(mockedDAO.delete(any(), eq(expected))).thenReturn(expected);
        when(mockedDAO.find(any(), eq(id))).thenReturn(expected);
        final String result = simpleService.delete(id);

        assertThat(result)
            .isEqualTo(expected);
    }

}