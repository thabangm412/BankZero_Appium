package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.ExtentReporterNG.getReportObject;

public class Listeners extends AppiumUtils implements ITestListener {


    private static final Logger LOGGER = Logger.getLogger(Listeners.class.getName());
    private static final ExtentReports extent = getReportObject();
    private static final ThreadLocal<ExtentTest> TEST_THREAD = new ThreadLocal<>();

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
            test.log(Status.PASS, "Test Passed");
        }
        attachScreenshot(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = getTest();
        if (test != null) {
            test.fail(result.getThrowable());
        }
        attachScreenshot(result);
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
                LOGGER.log(Level.WARNING, "Driver instance not found for test: {0}", result.getMethod().getMethodName());
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Could not capture screenshot: {0}", e.getMessage());
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
                LOGGER.log(Level.WARNING, "Unable to access driver field: {0}", iae.getMessage());
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
    public void onStart(ITestContext context) {}

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        TEST_THREAD.remove();
    }

//    ExtentTest test;
//    static ExtentReports extent = getReportObject();
//    AppiumDriver driver;
//
//    @Override
//    public void onTestStart(ITestResult result) {
//        test = extent.createTest(result.getMethod().getMethodName());
//    }
//
//    @Override
//    public void onTestSuccess(ITestResult result) {
//        test.log(Status.PASS, "Test Passed");
//        attachScreenshot(result);
//    }
//
//    @Override
//    public void onTestFailure(ITestResult result) {
//        test.fail(result.getThrowable());
//        attachScreenshot(result);
//    }
//
//    private void attachScreenshot(ITestResult result) {
//        try {
//            driver = (AppiumDriver) result.getTestClass().getRealClass().getField("driver")
//                    .get(result.getInstance());
//            String base64Screenshot = getBase64Screenshot(driver);
//            test.addScreenCaptureFromBase64String(base64Screenshot, result.getMethod().getMethodName());
//        } catch (Exception e) {
//            e.printStackTrace();
//            test.warning("Could not capture screenshot: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public void onTestSkipped(ITestResult result) {}
//
//    @Override
//    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
//
//    @Override
//    public void onStart(ITestContext context) {}
//
//    @Override
//    public void onFinish(ITestContext context) {
//        extent.flush();
//    }
}

