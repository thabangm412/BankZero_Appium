package testConfig;

import com.jcraft.jsch.JSchException;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import utils.AppiumUtils;
import utils.DriverManager;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

public class BaseTestsConfig extends AppiumUtils {

//    public static AndroidDriver driver;
    public static AppiumDriverLocalService service;

    @BeforeSuite
    public void ConfigureAppium() throws IOException {
        // Start Appium server
        service = new AppiumServiceBuilder()
                .withAppiumJS(new File("C:\\Users\\ThabangMonoane\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"))
                .withIPAddress("127.0.0.1")
                .usingPort(4725) // Match this port with driver URL below
                .withTimeout(Duration.ofSeconds(30))
                .build();
        service.start();

        // Set desired capabilities
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("Samsung SM-A566B");
        options.setUdid("R5CY60MB6KK");

        options.setPlatformName("Android");
        options.setAppPackage("za.co.neolabs.bankzero");
        options.setAppActivity("za.co.neolabs.bankzero.SplashActivity");
        options.setAutoGrantPermissions(true);

        // Preserve app state
        options.setCapability("noReset", true);
        options.setCapability("dontStopAppOnReset", true);


        DriverManager.driver = new AndroidDriver(new URL(URI.create("http://127.0.0.1:4725").toString()), options);
        System.out.println("BASE DRIVER HASH: " + DriverManager.driver.hashCode());
        DriverManager.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        DriverManager.driver.activateApp("za.co.neolabs.bankzero");

       //AndroidActions.copyAttachmentsToDevice(driver);
    }

    @AfterSuite
    public void tearDown() throws JSchException {
        if (DriverManager.driver != null) {
            DriverManager.driver.quit();
        }
        if (DriverManager.driver != null && service.isRunning()) {
            service.stop();
        }
    }
}