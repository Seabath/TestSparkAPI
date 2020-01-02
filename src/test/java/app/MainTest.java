package app;

import com.beerboy.ss.SparkSwagger;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class MainTest {

    @Test
    public void shouldPassMain() {
        Main.main(null);
        Main.stopServer();
    }

    @Test
    public void shouldInstanciateMain() {
        new Main();
    }

    @Test
    public void shouldGetExceptionWhileGeneratingDoc() throws IOException {
        final SparkSwagger mockedSparkSwagger = mock(SparkSwagger.class);
        doThrow(new IOException()).when(mockedSparkSwagger).generateDoc();

        Main.generateDoc(mockedSparkSwagger);
    }
}