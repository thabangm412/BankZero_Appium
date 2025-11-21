import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleTest {
    private static final Logger logger = LoggerFactory.getLogger(SampleTest.class);

    public static void main(String[] args) {
        logger.debug("Debug message");
        logger.info("Info message");
        logger.error("Error message");
        logger.trace("Trace message");
    }
}
