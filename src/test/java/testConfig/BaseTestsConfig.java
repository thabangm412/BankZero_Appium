package testConfig;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import utils.AndroidActions;
import utils.AppiumUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

public class BaseTestsConfig extends AppiumUtils {

    public AndroidDriver driver;
    public AppiumDriverLocalService service;

    @BeforeClass
    public void ConfigureAppium() throws IOException {
        // Start Appium server
        service = new AppiumServiceBuilder()
                .withAppiumJS(new File("C:\\Users\\ThabangMonoane\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"))
                .withIPAddress("127.0.0.1")
                .usingPort(4723) // Match this port with driver URL below
                .withTimeout(Duration.ofSeconds(30))
                .build();
        service.start();

        // Set desired capabilities
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("Samsung SM-A566B");
        options.setUdid("R5CY60MB6KK");
        options.setApp("C:\\Users\\ThabangMonoane\\IdeaProjects\\BzAppiumAutomation\\src\\test\\java\\resources\\apps\\app-dev-0.9.9.52-rc02.apk");
        options.setAutoGrantPermissions(true);
        options.setPlatformName("Android");
        options.setAppPackage("za.co.neolabs.bankzero");
        options.setAppActivity("za.co.neolabs.bankzero.SplashActivity");

        // Preserve app state

        options.setCapability("dontStopAppOnReset", true);

//        options.setCapability("unicodeKeyboard", true);
//        options.setCapability("resetKeyboard", true);
        options.setCapability("noReset", true);

        // Start driver
        //driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        driver = new AndroidDriver(new URL(URI.create("http://127.0.0.1:4723").toString()), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

       //AndroidActions.copyAttachmentsToDevice(driver);
    }

//    @BeforeClass
//    @Parameters({"deviceName", "portNumber", "udid"})
//    public void ConfigureAppium(String deviceName, String portNumber, String udid) throws IOException {
//        int port = Integer.parseInt(portNumber);
//
//        // Start Appium server
//        service = new AppiumServiceBuilder()
//                .withAppiumJS(new File("C:\\Users\\ThabangMonoane\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"))
//                .withIPAddress("127.0.0.1")
//                .usingPort(port) // Match this port with driver URL
//                .withTimeout(Duration.ofSeconds(30))
//                .build();
//        service.start();
//
//        // Set desired capabilities
//        UiAutomator2Options options = new UiAutomator2Options();
//        options.setDeviceName(deviceName);
//        options.setUdid(udid);
//        options.setApp("C:\\Users\\ThabangMonoane\\IdeaProjects\\BzAppiumAutomation\\src\\test\\java\\resources\\apps\\app-dev-0.9.9.42-rc15.apk");
//        options.setAutoGrantPermissions(true);
//        options.setPlatformName("Android");
//        options.setAppPackage("za.co.neolabs.bankzero");
//        options.setAppActivity("za.co.neolabs.bankzero.SplashActivity");
//
//        // Preserve app state
//        options.setCapability("noReset", true);
//        options.setCapability("dontStopAppOnReset", true);
//
//        // Keyboard settings
//        options.setCapability("unicodeKeyboard", true);
//        options.setCapability("resetKeyboard", true);
//
//        // Start Android driver with matching Appium port
//        URL gridUrl = new URL("http://localhost:4444");
//        driver = new AndroidDriver(gridUrl, options);
////        URL appiumUrl = new URL("http://127.0.0.1:" + port);
////        driver = new AndroidDriver(appiumUrl, options);
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//
//        // Optional utility method for attachment setup
//        // AndroidActions.copyAttachmentsToDevice(driver);
//    }



    @AfterClass
    public void tearDown() {

        AndroidActions androidActions = new AndroidActions(driver);
        //androidActions.closeApp();
        if (driver != null) {
            driver.quit();
        }
        if (service != null && service.isRunning()) {
            service.stop();
        }
    }
}