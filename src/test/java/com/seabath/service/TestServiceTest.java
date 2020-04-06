package com.seabath.service;

import com.seabath.common.Status;
import com.seabath.dao.SimpleDAO;
import com.seabath.dao.TestDAO;
import com.seabath.pojo.entity.TestEntity;
import com.seabath.pojo.entity.TestRunEntity;
import com.seabath.pojo.entity.TestSuiteEntity;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.mockito.Mockito.*;
import org.mockito.verification.VerificationMode;

class TestServiceTest {


    private TestService service;
    private TestDAO mockedDAO;
    private TestRunService mockedTestRunService;
    private Session mockedSession;

    @BeforeEach
    public void initService() throws NoSuchFieldException, IllegalAccessException {
        mockedDAO = mock(TestDAO.class);
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
    @MethodSource("shouldInterruptTestsSource")
    public void shouldInterruptTests(TestEntity input, VerificationMode verificationMode) {
        service.interrupt(input);

        assertThat(input.getStatus())
            .isEqualTo(Status.INTERRUPTED);
        verify(mockedTestRunService, verificationMode).finish(any(), eq(Status.INTERRUPTED));
    }

    private static Stream<Arguments> shouldInterruptTestsSource() {
        final TestEntity emptyRuns = TestEntity.builder()
            .testRunEntities(Collections.emptySet())
            .build();
        final TestEntity oneInProgress = TestEntity.builder()
            .testRunEntities(Collections.singleton(
                TestRunEntity.builder()
                    .status(Status.IN_PROGRESS)
                    .build()
            ))
            .build();

        final TestEntity noneInProgress = TestEntity.builder()
            .testRunEntities(Collections.singleton(
                TestRunEntity.builder()
                    .status(Status.FAIL)
                    .build()
            ))
            .build();

        return Stream.of(
            Arguments.of(emptyRuns, never()), // technicaly shouldn't happen, but just in case
            Arguments.of(noneInProgress, never()),
            Arguments.of(oneInProgress, only())
        );
    }

    @Test
    public void shouldStartTestNotExistingTest() {
        final String testName = "testname";
        final String packageName = "packagename";
        final TestSuiteEntity testSuiteEntity = TestSuiteEntity.builder()
            .id(42L)
            .testEntities(new HashSet<>())
            .build();

        final TestEntity testEntity = service.startTest(testSuiteEntity, testName, packageName);


        assertThat(testSuiteEntity.getTestEntities())
            .contains(testEntity);

        assertThat(testEntity.getStatus())
            .isEqualTo(Status.IN_PROGRESS);
        assertThat(testEntity.getTestName())
            .isEqualTo(testName);
        assertThat(testEntity.getPackageName())
            .isEqualTo(packageName);
        assertThat(testEntity.getTestSuiteEntity())
            .isEqualTo(testSuiteEntity);

        final Set<TestRunEntity> testRunEntities = testEntity.getTestRunEntities();
        final List<TestRunEntity> collect = testRunEntities.parallelStream()
            .collect(Collectors.toList());
        assertThat(collect)
            .hasSize(1);

        final TestRunEntity testRunEntity = collect.get(0);
        assertThat(testRunEntity.getStatus())
            .isEqualTo(Status.IN_PROGRESS);
        assertThat(testRunEntity.getStartDate())
            .isNotNull();

    }

    @Test
    public void shouldStartExistingTest() {
        final String testName = "testname";
        final String packageName = "packagename";
        final long isSuite = 42L;
        final TestEntity expected = TestEntity.builder()
            .packageName(packageName)
            .testName(testName)
            .status(Status.FAIL)
            .testRunEntities(new HashSet<>())
            .build();
        final TestSuiteEntity testSuiteEntity = TestSuiteEntity.builder()
            .id(isSuite)
            .testEntities(Collections.singleton(expected))
            .build();
        expected.setTestSuiteEntity(testSuiteEntity);

        when(mockedDAO.find(eq(mockedSession), eq(isSuite), eq(testName), eq(packageName)))
            .thenReturn(expected);
        final TestEntity result = service.startTest(testSuiteEntity, testName, packageName);


        assertThat(testSuiteEntity.getTestEntities())
            .contains(result);

        assertThat(result)
            .isEqualTo(expected);
        assertThat(result.getStatus())
            .isEqualTo(Status.IN_PROGRESS);
        assertThat(result.getTestName())
            .isEqualTo(testName);
        assertThat(result.getPackageName())
            .isEqualTo(packageName);
        assertThat(result.getTestSuiteEntity())
            .isEqualTo(testSuiteEntity);

        final Set<TestRunEntity> testRunEntities = result.getTestRunEntities();
        final List<TestRunEntity> collect = testRunEntities.parallelStream()
            .collect(Collectors.toList());
        assertThat(collect)
            .hasSize(1);

        final TestRunEntity testRunEntity = collect.get(0);
        assertThat(testRunEntity.getStatus())
            .isEqualTo(Status.IN_PROGRESS);
        assertThat(testRunEntity.getStartDate())
            .isNotNull();
    }

    @Test
    public void shouldGetAnExceptionStart() {
        Assertions.assertThrows(NullPointerException.class, () ->
            service.startTest(null, "", ""));
    }

    @Test
    public void shouldGetAnExceptionSucceed() {
        Assertions.assertThrows(NullPointerException.class, () ->
            service.succeed(null));
    }

    @Test
    public void shouldSucceedTestEntity() {
        final TestRunEntity testRunEntity = TestRunEntity.builder()
            .status(Status.IN_PROGRESS)
            .build();
        final TestEntity testEntity = TestEntity.builder()
            .status(Status.FAIL)
            .testRunEntities(Collections.singleton(testRunEntity))
            .build();

        service.succeed(testEntity);

        assertThat(testEntity.getStatus())
            .isEqualTo(Status.FLAKY);
        verify(mockedTestRunService, only()).finish(eq(testRunEntity), eq(Status.SUCCESS));
    }

    @Test
    public void shouldSucceedTestEntityNoRunInProgress() {
        final TestEntity testEntity = TestEntity.builder()
            .status(Status.FAIL)
            .testRunEntities(Collections.singleton(
                TestRunEntity.builder()
                    .status(Status.FAIL)
                    .build()
            ))
            .build();

        service.succeed(testEntity);

        assertThat(testEntity.getStatus())
            .isEqualTo(Status.FLAKY);
        verify(mockedTestRunService, never()).finish(any(), eq(Status.SUCCESS));
    }
}