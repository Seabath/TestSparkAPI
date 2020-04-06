package factory;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import pojo.test.run.GetTestRunResponse;

class TestRunFactoryTest {


    @Test
    public void should() {
        new TestRunFactory();
    }

    @Test
    public void shouldReturnNullResponse() {
        final GetTestRunResponse response = TestRunFactory.build(null);
        assertThat(response)
            .isNull();
    }

}