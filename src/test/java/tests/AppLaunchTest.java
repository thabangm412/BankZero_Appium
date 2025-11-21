package tests;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AppLaunchTest {

    private AndroidDriver driver;
    private AppiumDriverLocalService service;

    @BeforeMethod
    public void ConfigureAppium() throws IOException, MalformedURLException {
        // Start Appium server
        service = new AppiumServiceBuilder()
                .withAppiumJS(new File("C:\\Users\\ThabangMonoane\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"))
                .withIPAddress("127.0.0.1")
                .usingPort(4723)
                .withTimeout(Duration.ofSeconds(30))
                .build();

        service.start();

        // Set desired capabilities
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("Pixel 6");
        options.setApp("C:\\Users\\ThabangMonoane\\IdeaProjects\\BzAppiumAutomation\\src\\test\\java\\resources\\apps\\app-dev-0.9.9.42-rc12.apk");
        options.setAutoGrantPermissions(true);
        options.setPlatformName("Android");
        options.setAppPackage("za.co.neolabs.bankzero");
        options.setAppActivity("za.co.neolabs.bankzero.SplashActivity");
        options.setCapability("noReset", true);
        options.setCapability("dontStopAppOnReset", true);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testAppLaunchesSuccessfully() {
        // Adjust locator below based on the app's splash screen or main screen UI element
        // This example checks for a known element ID or text
//        try {
//            WebElement splashText = driver.findElement(By.id("za.co.neolabs.bankzero:id/splash_text")); // Replace with real element
//            //Assertions.assertTrue(splashText.isDisplayed(), "Splash screen is visible, app launched successfully.");
//        } catch (Exception e) {
//            Assertions.fail("App did not launch correctly or expected element not found.");
//        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        if (service != null) {
            service.stop();
        }
    }
}