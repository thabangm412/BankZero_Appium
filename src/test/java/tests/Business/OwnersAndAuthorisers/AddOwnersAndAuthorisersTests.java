package tests.Business.OwnersAndAuthorisers;

import factory.BusinessDataFactory;
import models.OwnersAndOfficials;
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
import org.testng.asserts.SoftAssert;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.business.BusinessAuthChainPage;
import pageObjects.app.business.BusinessPage;
import pageObjects.app.business.OwnersAndOfficialsPage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;
import utils.DriverManager;

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

    private AccountMenuActions accountMenuActions;
    private BusinessAuthChainPage businessAuthChainPage;
    private HomePage homePage;
    private SoftAssert softAssert;
    private OwnersAndOfficials owners;
    private OwnersAndOfficialsPage ownersAndOfficialsPage;
    private OwnersAndOfficials businessOwnersAndOfficials;

    @BeforeMethod
    public void setUpPages() {
        // initialize page objects once per test method
        log.debug("Initializing page objects for test.");
        log.debug("Initializing page objects for test.");
        loginPage = new LoginPage(DriverManager.driver);
        businessPage = new BusinessPage(DriverManager.driver);
        androidActions = new AndroidActions(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        businessAuthChainPage = new BusinessAuthChainPage(DriverManager.driver);
        ownersAndOfficialsPage = new OwnersAndOfficialsPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        softAssert = new SoftAssert();
        owners = BusinessDataFactory.ownersAndOfficials();
        businessOwnersAndOfficials = BusinessDataFactory.businessOwnersAndOfficials();
    }

    @Test(priority = 0, description = "View Owners and Officials of business account")
    public void viewOwnersAndOfficials()
    {
        loginPage.loginWithRetry(
                owners.getUser().getProfileName(),
                owners.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        ownersAndOfficialsPage.clickOwnersAndOfficialsButton();
        Assert.assertTrue(ownersAndOfficialsPage.getOwnersAndOfficialsPage());
        androidActions.attachScreenshot(DriverManager.driver,"Owners and Officials Page");
        DriverManager.driver.navigate().back();
        homePage.clickLogoutButtn();

    }


    @Test(dataProvider = "getMultipleDataSet",description = "Adding Owners and Authorisers of indivisual account",priority = 1)
    public void addingOwnersAndAuthorisers(HashMap<String, String> input) throws InterruptedException {

        loginPage.loginWithRetry(
                owners.getUser().getProfileName(),
                owners.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        ownersAndOfficialsPage.clickOwnersAndOfficialsButton();
        businessPage.addNewOwnersAndAuthorisersButtn();
        ownersAndOfficialsPage.addOwnersAndOfficials(input.get("role"),input.get("nationality"),input.get("cellNumber"),input.get("ownerName"));
        businessPage.saveChanges();
        Assert.assertTrue(ownersAndOfficialsPage.getAddedOwnersAndOfficials().contains(input.get("ownerName")));
        ownersAndOfficialsPage.clickConfirmButton();
        ownersAndOfficialsPage.clickFinishButton();
        homePage.clickLogoutButtn();

    }

    @Test(dataProvider = "getMultipleDataSet",description = "Adding Owners and Authorisers of business account",priority = 2)
    public void addingBusinessOwnersAndAuthorisers(HashMap<String, String> input) throws InterruptedException {

        loginPage.loginWithRetry(
                owners.getUser().getProfileName(),
                owners.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        ownersAndOfficialsPage.clickOwnersAndOfficialsButton();
        businessPage.addNewOwnersAndAuthorisersButtn();
        ownersAndOfficialsPage.addBusinessOwnersAndOfficials(businessOwnersAndOfficials.getRole5(),businessOwnersAndOfficials.getNationality(),businessOwnersAndOfficials.getOwnerName(),businessOwnersAndOfficials.getRegistrationNo());
        businessPage.saveChanges();
        Assert.assertTrue(ownersAndOfficialsPage.getAddedOwnersAndOfficials().contains(input.get("ownerName")));
        ownersAndOfficialsPage.clickConfirmButton();
        ownersAndOfficialsPage.clickFinishButton();
        homePage.clickLogoutButtn();

    }

    @Test(dataProvider = "getMultipleDataSet",priority = 1)
    public void duplicateOwnersAndAuthorisersErrorTest(HashMap<String, String> input) throws InterruptedException {
        loginPage.loginWithRetry(
                owners.getUser().getProfileName(),
                owners.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        ownersAndOfficialsPage.clickOwnersAndOfficialsButton();
        businessPage.addNewOwnersAndAuthorisersButtn();
        ownersAndOfficialsPage.addOwnersAndOfficials(input.get("role"),input.get("nationality"),input.get("cellNumber"),input.get("ownerName"));
        assertOwnerAlreadyExistsMessagePresent(input.get("ownerName"), input.get("cellNumber"));
        businessPage.confirmDuplication();
        DriverManager.driver.navigate().back();
        homePage.clickLogoutButtn();
    }

    @Test(dataProvider = "getMultipleDataSet",priority = 1)
    public void duplicateBusinessOwnersAndAuthorisersErrorTest(HashMap<String, String> input) throws InterruptedException {
        loginPage.loginWithRetry(
                owners.getUser().getProfileName(),
                owners.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        ownersAndOfficialsPage.clickOwnersAndOfficialsButton();
        businessPage.addNewOwnersAndAuthorisersButtn();
        ownersAndOfficialsPage.addBusinessOwnersAndOfficials(businessOwnersAndOfficials.getRole5(),businessOwnersAndOfficials.getNationality(),businessOwnersAndOfficials.getOwnerName(),businessOwnersAndOfficials.getRegistrationNo());
        assertBusinessOwnerAlreadyExistsMessagePresent(businessOwnersAndOfficials.getOwnerName(), businessOwnersAndOfficials.getOwnerName());
        businessPage.confirmDuplication();
        DriverManager.driver.navigate().back();
        homePage.clickLogoutButtn();
    }



    @Test(dataProvider = "getMultipleDataSet",priority = 2)
    public void editOwnersAndAuthorisers(HashMap<String, String> input) throws InterruptedException {
        log.info("Starting editOwnersAndAuthorisers test");

        androidActions.validateInputKeys(input, "profileName", "loginPin", "ownerName", "updateownerName", "updateRole","nationality","updateCellNumber");

        String profileName = input.get("profileName");
        String loginPin = input.get("loginPin");
        log.info("Logging in with profile: {}", profileName);
        // do not log sensitive values such as PIN
        loginPage.loginWithRetry(profileName, loginPin, 2);

        businessPage.clickBusinessMenuActionButtn();
        businessPage.clickOwnersAndAuthorisers();
        Thread.sleep(3000);
        businessPage.editOwnersAndAuthorisers(input.get("ownerName"),input.get("updateownerName"),input.get("updateRole"),input.get("updateCellNumber"),input.get("nationality"));
        businessPage.saveChanges();
        Thread.sleep(300);
        businessPage.clickFinish();
        businessPage.clickBusinessMenuActionButtn();
        businessPage.clickOwnersAndAuthorisers();
        Thread.sleep(3000);
        androidActions.assertTextPresentExact(input.get("updateownerName"));
        driver.navigate().back();
        log.info("Owner update test completed successfully.");

    }

    @Test(dataProvider = "getMultipleDataSet",priority = 3)
    public void deleteOwnersAndAuthorisers(HashMap<String, String> input) throws InterruptedException {
        loginPage.loginWithRetry(
                owners.getUser().getProfileName(),
                owners.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        ownersAndOfficialsPage.clickOwnersAndOfficialsButton();
        businessPage.deleteOwnersAndAuthorisers(input.get("ownerName"));
        businessPage.confirmRemove();
        businessPage.saveChanges();
        businessPage.clickFinish();
        businessPage.clickFinish();
        accountMenuActions.clickAccountMenuActionsOption("Business");
        ownersAndOfficialsPage.clickOwnersAndOfficialsButton();
        androidActions.assertTextAbscentExtract(input.get("ownerName"));
        driver.navigate().back();
        homePage.clickLogoutButtn();

    }

    @Test(dataProvider = "getMultipleDataSet",priority = 3)
    public void deleteBusinessOwnersAndAuthorisers(HashMap<String, String> input) throws InterruptedException {
        loginPage.loginWithRetry(
                owners.getUser().getProfileName(),
                owners.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        ownersAndOfficialsPage.clickOwnersAndOfficialsButton();
        businessPage.deleteOwnersAndAuthorisers(businessOwnersAndOfficials.getOwnerName());
        businessPage.confirmRemove();
        businessPage.saveChanges();
        businessPage.clickFinish();
        businessPage.clickFinish();
        accountMenuActions.clickAccountMenuActionsOption("Business");
        ownersAndOfficialsPage.clickOwnersAndOfficialsButton();
        androidActions.assertTextAbscentExtract(businessOwnersAndOfficials.getOwnerName());
        DriverManager.driver.navigate().back();
        homePage.clickLogoutButtn();

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
        WebDriverWait wait = new WebDriverWait(DriverManager.driver, Duration.ofSeconds(15));

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

    private void assertBusinessOwnerAlreadyExistsMessagePresent(String name, String ownerName) {

        String expectedMessage =
                "Owner/Official " + name + " with name " + ownerName + " already exist.";

        By messageXpath = By.xpath("//android.widget.TextView[@resource-id='android:id/message']");
        WebDriverWait wait = new WebDriverWait(DriverManager.driver, Duration.ofSeconds(15));

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
