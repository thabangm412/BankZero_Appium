package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;

import static utils.ExtentReporterNG.getReportObject;

public class Listeners extends AppiumUtils implements ITestListener {


    private static final Logger log = LoggerFactory.getLogger(Listeners.class);
    private static ExtentReports extent;
    public static final ThreadLocal<ExtentTest> TEST_THREAD = new ThreadLocal<>();

    private ExtentTest getTest() {
        return TEST_THREAD.get();
    }

    @Override
    public void onTestStart(ITestResult result) {
        TEST_THREAD.set(extent.createTest(result.getMethod().getMethodName()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = getTest();
        if (test != null) {
            test.pass("Test Passed");
        }
//        attachScreenshot(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = getTest();
        Throwable cause = result.getThrowable();

        if (test != null) {
            if (cause instanceof AssertionError) {
                log.error("Assertion failed in [{}]: {}", result.getName(), cause.getMessage());
                test.fail("Assertion failed: " + cause.getMessage());
            } else {
                log.error("Exception in [{}]: {}", result.getName(), cause.getMessage());
                test.fail("Test failed due to exception: " + cause.getMessage());
            }
            test.fail(cause); // attaches full stack trace to Extent report
        }

        attachScreenshot(result);
        log.info("Screenshot attached for failed test: [{}]", result.getName());
    }

    private void attachScreenshot(ITestResult result) {
        try {
            Optional<AppiumDriver> optDriver = getDriverFromResult(result);
            if (optDriver.isPresent()) {
                AppiumDriver driver = optDriver.get();
                String base64Screenshot = getBase64Screenshot(driver);
                ExtentTest test = getTest();
                if (test != null) {
                    test.addScreenCaptureFromBase64String(base64Screenshot, result.getMethod().getMethodName());
                }
            } else {
                log.warn("Driver instance not found for test: {}", result.getMethod().getMethodName());
            }
        } catch (Exception e) {
            log.warn("Could not capture screenshot: {}", e.getMessage());
            ExtentTest test = getTest();
            if (test != null) {
                test.warning("Could not capture screenshot: " + e.getMessage());
            }
        }
    }

    private Optional<AppiumDriver> getDriverFromResult(ITestResult result) {
        Object testInstance = result.getInstance();
        Class<?> clazz = testInstance.getClass();
        while (clazz != null && clazz != Object.class) {
            try {
                Field field = clazz.getDeclaredField("driver");
                field.setAccessible(true);
                Object value = field.get(testInstance);
                if (value instanceof AppiumDriver) {
                    return Optional.of((AppiumDriver) value);
                } else {
                    return Optional.empty();
                }
            } catch (NoSuchFieldException nsf) {
                clazz = clazz.getSuperclass();
            } catch (IllegalAccessException iae) {
                log.warn("Unable to access driver field: {}", iae.getMessage());
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    @Override
    public void onTestSkipped(ITestResult result) {}

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

    @Override
    public void onStart(ITestContext context) {
        extent = ExtentReporterNG.getReportObject((AndroidDriver) context.getAttribute("driver"));
    }

    @Override
    public void onFinish(ITestContext context) {
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        extent.setSystemInfo("Execution End Time", endTime);
        extent.flush();
        TEST_THREAD.remove();
    }

}

