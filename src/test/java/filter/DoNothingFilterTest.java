package filter;

import org.junit.jupiter.api.Test;

class DoNothingFilterTest {

    @Test
    public void shouldNotDoAnything() {
        final DoNothingFilter doNothingFilter = new DoNothingFilter();
        doNothingFilter.handle(null, null);
    }

}