package tests.Business.Registration;

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
import utils.AndroidActions;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessPtyTest extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(BusinessPtyTest.class);
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
    public void PtyRegistration(HashMap<String, String> input)
    {
        log.info("Starting PtyRegistration test");

        validateInputKeys(input, "profileName", "loginPin", "businessType",
                "tradingName", "registeredName", "registrationNo",
                "sicGroup", "sicIndustry", "notifyEmail","city","street","successMsg","cardPin");

        String profileName = input.get("profileName");
        String loginPin = input.get("loginPin");
        log.info("Logging in with profile: {}", profileName);
        // do not log sensitive values such as PIN
        loginPage.loginWithRetry(profileName, loginPin, 2);
        log.debug("Login attempt finished for profile: {}", profileName);

        log.info("Starting add account flow");
        addAccountPage.clickAddAccButtn();
        addAccountPage.newBusinessButtn();
        log.debug("Selected new business account");

        log.info("Choosing business type: {}", input.get("businessType"));
        businessPage.chooseBusiness(input.get("businessType"));

        log.info("Entering PTY business details (trading/registration names and number)");
        businessPage.ptyBusinessDetails(input.get("tradingName"), input.get("registeredName"), input.get("registrationNo"));

        log.info("Entering SIC details: group={}, industry={}", input.get("sicGroup"), input.get("sicIndustry"));
        businessPage.sicDetails(input.get("sicGroup"), input.get("sicIndustry"));

        log.info("Selecting source of funds & wealth");
        businessPage.chooseSourceOfFundsAndWealth();

        log.info("Entering notification email");
        businessPage.enterNotifyEmail(input.get("notifyEmail"));

        log.info("Selecting from account and proceeding to next step");
        businessPage.selectFromAccount(input.get("profileName"));
        businessPage.clickNextButton();

        log.info("Entering registered address details");
        businessPage.enterRegisteredAddress(input.get("street"), input.get("city"));
        businessPage.clickNextButton();

        log.info("Selecting card option for business account");
        businessPage.selectCardOptions(input.get("tradingName"));
        businessPage.clickNextButton();

        log.info("Adding card delivery and security details");
        businessPage.enterCardPin(input.get("cardPin"));
        businessPage.clickNextButton();

        log.info("Adding owners and officials");
        businessPage.selectOwnersAndOfficials();
        businessPage.clickNextButton();

        log.info("Finalizing business account registration");
        businessPage.verifyTermsAndConditions();
        businessPage.clickNextButton();

        assertTextPresentExact(input.get("successMsg"));

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
