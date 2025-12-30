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
import pageObjects.app.business.BusinessPage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

public class AddOwnersAndAuthorisersTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(AddOwnersAndAuthorisersTests.class);

    private LoginPage loginPage;
    private BusinessPage businessPage;

    private  AndroidActions androidActions;

    @BeforeMethod
    public void setUpPages() {
        // initialize page objects once per test method
        log.debug("Initializing page objects for test.");
        loginPage = new LoginPage(driver);
        businessPage = new BusinessPage(driver);
        androidActions = new AndroidActions(driver);
    }

    @Test(dataProvider = "getMultipleDataSet")
    public void addingOwnersAndAuthorisers(HashMap<String, String> input) throws InterruptedException {
        log.info("Starting addingOwnersAndAuthorisers test");


        androidActions.validateInputKeys(input, "profileName", "loginPin", "ownerName", "cellNumber","role","nationality");

        String profileName = input.get("profileName");
        String loginPin = input.get("loginPin");
        log.info("Logging in with profile: {}", profileName);
        // do not log sensitive values such as PIN
        loginPage.loginWithRetry(profileName, loginPin, 2);

        businessPage.clickBusinessMenuActionButtn();
        businessPage.clickOwnersAndAuthorisers();
        businessPage.addNewOwnersAndAuthorisersButtn();
        //businessPage.clickAddOrRemoveButtn();
        businessPage.addOwnersAndOfficials(input.get("role"),input.get("nationality"),input.get("cellNumber"),input.get("ownerName"));
        businessPage.saveChanges();
        Thread.sleep(300);
        businessPage.clickFinish();
        businessPage.clickBusinessMenuActionButtn();
        businessPage.clickOwnersAndAuthorisers();
        Thread.sleep(3000);
        androidActions.assertTextPresentExact(input.get("ownerName"));
        driver.navigate().back();
        log.info("addingOwnersAndAuthorisers test completed successfully.");
    }

    @Test(dataProvider = "getMultipleDataSet",priority = 1)
    public void duplicateOwnersAndAuthorisersErrorTest(HashMap<String, String> input) throws InterruptedException {
        log.info("Starting duplicateOwnersAndAuthorisersValidation test");
        androidActions.validateInputKeys(input, "profileName", "loginPin", "ownerName", "cellNumber","role","nationality");

        String profileName = input.get("profileName");
        String loginPin = input.get("loginPin");
        log.info("Logging in with profile: {}", profileName);
        // do not log sensitive values such as PIN
        loginPage.loginWithRetry(profileName, loginPin, 2);

        businessPage.clickBusinessMenuActionButtn();
        businessPage.clickOwnersAndAuthorisers();
        Thread.sleep(3000);
        businessPage.addNewOwnersAndAuthorisersButtn();
        businessPage.addOwnersAndOfficials(input.get("role"),input.get("nationality"),input.get("cellNumber"),input.get("ownerName"));
        assertOwnerAlreadyExistsMessagePresent(input.get("ownerName"), input.get("cellNumber"));
        businessPage.confirmDuplication();
        driver.navigate().back();
        log.info("duplicateOwnersAndAuthorisersValidation test completed successfully.");
    }

    @Test(dataProvider = "getMultipleDataSet",priority = 2)
    public void deleteOwnersAndAuthorisers(HashMap<String, String> input) throws InterruptedException {
        log.info("Starting deleteOwnersAndAuthorisers test");

        androidActions.validateInputKeys(input, "profileName", "loginPin", "ownerName");

        String profileName = input.get("profileName");
        String loginPin = input.get("loginPin");
        log.info("Logging in with profile: {}", profileName);
        // do not log sensitive values such as PIN
        loginPage.loginWithRetry(profileName, loginPin, 2);

        businessPage.clickBusinessMenuActionButtn();
        businessPage.clickOwnersAndAuthorisers();
        Thread.sleep(3000);
        businessPage.deleteOwnersAndAuthorisers(input.get("ownerName"));
        businessPage.confirmRemove();
        //click update/submit
        businessPage.saveChanges();
        Thread.sleep(300);
        businessPage.clickFinish();
        businessPage.clickBusinessMenuActionButtn();
        businessPage.clickOwnersAndAuthorisers();
        Thread.sleep(3000);
        androidActions.assertTextAbscentExtract(input.get("ownerName"));
        driver.navigate().back();
        log.info("deleteOwnersAndAuthorisers test completed successfully.");

    }

    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        String path =
                System.getProperty("user.dir") + File.separator + "src" + File.separator
                        + "test" + File.separator + "java" + File.separator + "testData" + File.separator + "ownersAndAuthorisers.json";
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

    private void assertOwnerAlreadyExistsMessagePresent(String name, String cellphone) {

        String expectedMessage =
                "Owner/Official " + name + " with number " + cellphone + " already exist.";

        By messageXpath = By.xpath("//android.widget.TextView[@resource-id='android:id/message']");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            WebElement messageElement =
                    wait.until(ExpectedConditions.visibilityOfElementLocated(messageXpath));

            String actualMessage = messageElement.getText().trim();

            Assert.assertEquals(
                    actualMessage,
                    expectedMessage,
                    "Owner already exists message does not match"
            );

            log.info("Confirmed expected message is present: {}", androidActions.maskForLog(expectedMessage));

        } catch (TimeoutException e) {
            log.error("Expected message was not displayed: {}", expectedMessage);
            Assert.fail("Expected message was not displayed: " + expectedMessage);
        }
    }



}
