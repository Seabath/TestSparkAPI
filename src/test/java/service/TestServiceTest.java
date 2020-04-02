package service;

import common.Status;
import dao.SimpleDAO;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.mockito.Mockito.*;
import org.mockito.verification.VerificationMode;
import pojo.entity.TestEntity;
import pojo.entity.TestRunEntity;

class TestServiceTest {


    private TestService service;
    private SimpleDAO<TestEntity> mockedDAO;
    private TestRunService mockedTestRunService;
    private Session mockedSession;

    @BeforeEach
    public void initService() throws NoSuchFieldException, IllegalAccessException {
        mockedDAO = mock(SimpleDAO.class);
        mockedTestRunService = mock(TestRunService.class);
        mockedSession = mock(Session.class);
        service = new TestService(mockedDAO, mockedTestRunService);

        SessionFactory mockedSessionFactory = mock(SessionFactory.class);
        final Field sessionFactoryField = SimpleDAO.class.getDeclaredField("sessionFactory");
        sessionFactoryField.setAccessible(true);
        sessionFactoryField.set(null, mockedSessionFactory);

        when(mockedSessionFactory.openSession()).thenReturn(mockedSession);
        when(mockedDAO.openSession()).thenReturn(mockedSession);
    }


    @ParameterizedTest
    @MethodSource("shouldInterrputTestsSource")
    public void shouldInterruptTests(TestEntity input, VerificationMode verificationMode) {

        service.interrupt(input);

        assertThat(input.getStatus())
            .isEqualTo(Status.INTERRUPTED);
        verify(mockedTestRunService, verificationMode).interrupt(any());
    }

    private static Stream<Arguments> shouldInterrputTestsSource() {
        final TestEntity emptyRuns = TestEntity.builder()
            .testRunEntities(Collections.emptySet())
            .build();

        Set<TestRunEntity> testRunEntities = new HashSet<>();
        testRunEntities.add(
            TestRunEntity.builder()
                .status(Status.IN_PROGRESS)
                .build()
        );
        testRunEntities.add(
            TestRunEntity.builder()
                .status(Status.FAIL)
                .build()
        );
        final TestEntity oneInProgress = TestEntity.builder()
            .testRunEntities(testRunEntities)
            .build();

        return Stream.of(
            Arguments.of(emptyRuns, never()), // technicaly shouldn't happen, but just in case
            Arguments.of(oneInProgress, only())
        );
    }

}