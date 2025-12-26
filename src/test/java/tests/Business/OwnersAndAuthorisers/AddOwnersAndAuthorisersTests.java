package tests.Business.OwnersAndAuthorisers;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.addAccount.AddAccountPage;
import pageObjects.app.business.BusinessPage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import tests.Business.Registration.BusinessPtyTest;
import utils.AndroidActions;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddOwnersAndAuthorisersTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(AddOwnersAndAuthorisersTests.class);

    private LoginPage loginPage;
    private AddAccountPage addAccountPage;
    private BusinessPage businessPage;
    private AndroidActions androidActions;


    @BeforeMethod
    public void setUpPages() {
        // initialize page objects once per test method
        log.debug("Initializing page objects for test.");
        loginPage = new LoginPage(driver);
        addAccountPage = new AddAccountPage(driver);
        businessPage = new BusinessPage(driver);
        androidActions = new AndroidActions(driver);
    }

    @Test(dataProvider = "getMultipleDataSet")
    public void addingOwnersAndAuthorisers(HashMap<String, String> input)
    {
        log.info("Starting addingOwnersAndAuthorisers test");

        validateInputKeys(input, "profileName", "loginPin", "ownerName", "cellPhone","role","nationality");

        String profileName = input.get("profileName");
        String loginPin = input.get("loginPin");
        log.info("Logging in with profile: {}", profileName);
        // do not log sensitive values such as PIN
        loginPage.loginWithRetry(profileName, loginPin, 2);

        businessPage.clickBusinessMenuActionButtn();
        businessPage.clickOwnersAndAuthorisers();
        businessPage.addOwnersAndOfficials(input.get("role"),input.get("nationality"),input.get("cellPhone"),input.get("ownerName"));
    }

    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        String path =
                System.getProperty("user.dir") + File.separator + "src" + File.separator
                        + "test" + File.separator + "java" + File.separator + "testData" + File.separator + "businessData.json";
        log.debug("Loading test data from: {}", path);

        List<HashMap<String, String>> data = getJsonData(path);
        if (data == null || data.isEmpty()) {
            log.error("No test data found at: {}", path);
            throw new IllegalStateException("Test data is empty: " + path);
        }

        log.info("Providing {} data set(s) to test", data.size());
        return new Object[][]{{data.getFirst()}};
    }

    @AfterMethod
    public void cleanUp() {
        try {
            HomePage homePage = new HomePage(driver);
            homePage.clickLogoutButtn();
            log.info("Logged out successfully during cleanup.");
        } catch (Exception e) {
            log.error("Cleanup failed: ", e);
        }
    }

    private void validateInputKeys(Map<String, String> input, String... requiredKeys) {
        if (input == null) {
            log.error("Input map is null");
            throw new IllegalArgumentException("Input data cannot be null");
        }
        StringBuilder missing = new StringBuilder();
        Arrays.stream(requiredKeys).forEach(k -> {
            if (input.get(k) == null || input.get(k).trim().isEmpty()) {
                if (missing.length() > 0) missing.append(", ");
                missing.append(k);
            }
        });
        if (missing.length() > 0) {
            log.error("Missing required test input keys: {}", missing);
            throw new IllegalArgumentException("Missing required test input keys: " + missing);
        }
        log.debug("All required keys present in input");
    }


    private void assertTextPresentExact(String exactText) {
        By xpath = By.xpath("//android.widget.TextView[@text=\"" + exactText + "\"]");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(xpath));
            Assert.assertTrue(el.isDisplayed(), "Expected text not visible: " + exactText);
            log.info("Found expected text: {}", maskForLog(exactText));
        } catch (TimeoutException e) {
            log.error("Expected text not found within timeout: {}", exactText);
            Assert.fail("Expected text not found: " + exactText);
        }
    }

    private String maskForLog(String s) {
        if (s == null) return "";
        if (s.length() <= 20) return s;
        return s.substring(0, 20) + "...";
    }
}
