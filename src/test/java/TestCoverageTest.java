import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TestCoverageTest {

    @Test
    public void sampleTest() {
        assertThat(new TestCoverage().multipleByTwo(4), is(8));
    }
}