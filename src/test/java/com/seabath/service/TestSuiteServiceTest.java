package com.seabath.service;

import com.seabath.common.Status;
import com.seabath.dao.SimpleDAO;
import com.seabath.pojo.entity.TestEntity;
import com.seabath.pojo.entity.TestSuiteEntity;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
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

class TestSuiteServiceTest {


    private TestSuiteService service;
    private SimpleDAO<TestSuiteEntity> mockedSuiteDAO;
    private TestService mockedTestService;
    private Session mockedSession;

    @BeforeEach
    public void initService() throws NoSuchFieldException, IllegalAccessException {
        mockedSuiteDAO = mock(SimpleDAO.class);
        mockedTestService = mock(TestService.class);
        mockedSession = mock(Session.class);
        service = new TestSuiteService(mockedSuiteDAO, mockedTestService);

        SessionFactory mockedSessionFactory = mock(SessionFactory.class);
        final Field sessionFactoryField = SimpleDAO.class.getDeclaredField("sessionFactory");
        sessionFactoryField.setAccessible(true);
        sessionFactoryField.set(null, mockedSessionFactory);

        when(mockedSessionFactory.openSession()).thenReturn(mockedSession);
        when(mockedSuiteDAO.openSession()).thenReturn(mockedSession);
    }

    @ParameterizedTest
    @MethodSource("updateStatusSource")
    public void shouldUpdateStatus(TestSuiteEntity suiteEntity, Status expectedStatus,
                                   VerificationMode verificationMode) {
        when(mockedSuiteDAO.saveOrUpdate(eq(mockedSession), eq(suiteEntity))).thenReturn(suiteEntity);

        service.updateStatus(suiteEntity);


        assertThat(suiteEntity.getEndDate())
            .isNotNull();
        assertThat(suiteEntity.getStatus())
            .isEqualTo(expectedStatus);
        verify(mockedTestService, verificationMode).interrupt(any());
    }

    private static Stream<Arguments> updateStatusSource() {
        final TestSuiteEntity emptyTestsSuite = TestSuiteEntity.builder()
            .testEntities(Collections.emptySet())
            .build();

        final HashSet<TestEntity> failedTestEntities = new HashSet<>();
        failedTestEntities.add(
            TestEntity.builder()
                .status(Status.FAIL)
                .build()
        );
        failedTestEntities.add(
            TestEntity.builder()
                .status(Status.SUCCESS)
                .build()
        );
        TestSuiteEntity failedSuite = TestSuiteEntity.builder()
            .testEntities(failedTestEntities)
            .build();

        final HashSet<TestEntity> flakyTestEntities = new HashSet<>();
        flakyTestEntities.add(
            TestEntity.builder()
                .status(Status.FLAKY)
                .build()
        );
        flakyTestEntities.add(
            TestEntity.builder()
                .status(Status.SUCCESS)
                .build()
        );
        TestSuiteEntity flakySuite = TestSuiteEntity.builder()
            .testEntities(flakyTestEntities)
            .build();

        final HashSet<TestEntity> interruptedTestEntities = new HashSet<>();
        interruptedTestEntities.add(
            TestEntity.builder()
                .status(Status.IN_PROGRESS)
                .build()
        );
        TestSuiteEntity interruptedSuite = TestSuiteEntity.builder()
            .testEntities(interruptedTestEntities)
            .build();

        return Stream.of(
            Arguments.of(emptyTestsSuite, Status.SUCCESS, never()),
            Arguments.of(failedSuite, Status.FAIL, never()),
            Arguments.of(flakySuite, Status.FLAKY, never()),
            Arguments.of(interruptedSuite, Status.INTERRUPTED, only())
        );
    }

}