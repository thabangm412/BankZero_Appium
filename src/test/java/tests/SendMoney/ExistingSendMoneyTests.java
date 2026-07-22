package tests.SendMoney;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.pay.QuickPayPage;
import pageObjects.app.accountsActionMenu.sendMoney.SendMoneyPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;
import utils.AppiumUtils;
import utils.DriverManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ExistingSendMoneyTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(ExistingSendMoneyTests.class);

    private LoginPage loginPage;
    private QuickPayPage quickPayPage;
    private HomePage homePage;
    private AccountMenuActions accountMenuActions;
    private AndroidActions androidActions;

    private SendMoneyPage sendMoneyPage;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        sendMoneyPage = new SendMoneyPage(DriverManager.driver);
        quickPayPage = new QuickPayPage(DriverManager.driver);
        androidActions = new AndroidActions(DriverManager.driver);

        log.debug("Page objects and androidActions initialized");
    }

    @Test(dataProvider = "getMultipleDataSet",priority = 0)
    public void sendMoneyToExistingProfile(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "recipientName", "amount", "ref"
        );

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        sendMoneyPage.clickSendMoneyButton();
        sendMoneyPage.getExistingProfile(input.get("recipientName"));
        sendMoneyPage.sendMoney(input.get("amount"),input.get("ref"));
        attachScreenshot(DriverManager.driver, "SendMoneyDetails");
        sendMoneyPage.clickSend();
        sendMoneyPage.clickConfirm();
        quickPayPage.possibleDuplicateCheck();


        try {
            try {
                Assert.assertEquals(sendMoneyPage.getStatus(), "Thank you");
                log.info("Transactional successful");
                attachScreenshot(DriverManager.driver, "SendMoney_Success");
            } catch (AssertionError e) {
                log.warn("Transaction failed with status: {}", sendMoneyPage.getStatus());
                throw e;
            }
        } catch (Exception e) {
            log.error("Exception occurred during transaction handling: ", e);
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }finally {
            sendMoneyPage.clickFinish();
        }

    }

    @Test(dataProvider = "getMultipleDataSet",priority = 1)
    public void sendMoneyWithRedo(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "recipientName"
        );

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        sendMoneyPage.clickSendMoneyButton();
        sendMoneyPage.getExistingProfile(input.get("recipientName"));
        sendMoneyPage.clickRedo();
        attachScreenshot(DriverManager.driver, "Redo_Screen");
        sendMoneyPage.clickSend();
        sendMoneyPage.clickConfirm();
        quickPayPage.possibleDuplicateCheck();

        try {
            try {
                Assert.assertEquals(sendMoneyPage.getStatus(), "Thank you");
                attachScreenshot(DriverManager.driver, "SendMoney_Redo_Success");
                log.info("Transactional successful");
            } catch (AssertionError e) {
                log.warn("Transaction failed with status: {}", sendMoneyPage.getStatus());
                throw e;
            }
        } catch (Exception e) {
            log.error("Exception occurred during transaction handling: ", e);
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }finally {
            sendMoneyPage.clickFinish();
        }
    }

    @Test(dataProvider = "getMultipleDataSet", priority = 2)
    public void sendMoneyWithAttachment(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "recipientName", "amount", "ref"
        );

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        sendMoneyPage.clickSendMoneyButton();
        sendMoneyPage.getExistingProfile(input.get("recipientName"));
        sendMoneyPage.sendMoney(input.get("amount"),input.get("ref"));
        sendMoneyPage.addAttachment();
        AppiumUtils.waitForElement(By.id("za.co.neolabs.bankzero:id/fileImage"),DriverManager.driver);
        attachScreenshot(DriverManager.driver, "Attachment_Added");
        sendMoneyPage.clickSend();
        sendMoneyPage.clickConfirm();

        SoftAssert softAssert = new SoftAssert(); // TestNG’s SoftAssert

        try {
            softAssert.assertTrue(sendMoneyPage.getAttachment(), "Attachment missing");
            softAssert.assertEquals(sendMoneyPage.getStatus(), "Thank you", "Wrong status");
            attachScreenshot(DriverManager.driver, "SendMoney_Attachment_Success");

            if (sendMoneyPage.getAttachment()) {
                log.info("Attachment check passed");
            }
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            softAssert.fail("Test crashed: " + e.getMessage());
        } finally {
            sendMoneyPage.clickFinish();
            softAssert.assertAll();
        }
    }

    @Test(dataProvider = "getMultipleDataSet",priority = 3)
    public void addAlreadyExistingRecipient(HashMap<String, String> input)
    {

        validateInput(input,
                "profileName", "loginPin",
                "recipientName", "recipientPhone"
        );

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        sendMoneyPage.clickSendMoneyButton();
        sendMoneyPage.clickAddRecipientButton();
        sendMoneyPage.addRecipient(input.get("recipientName"),input.get("recipientPhone"));

        try {
            String toastMessage = DriverManager.driver.findElement(
                    By.id("za.co.neolabs.bankzero:id/snackbar_text")
            ).getText();
            Assert.assertEquals(toastMessage, "[79] We're sorry, you cannot add this recipient as it already exists");
            log.info("Failed to add recipient, error message: {}", toastMessage);
            androidActions.attachScreenshot(DriverManager.driver, "Add_Existing_Recipient_passed");
        } catch (NoSuchElementException| AssertionError e) {
            log.warn("Test failed to validate existing recipient addition");
            androidActions.attachScreenshot(DriverManager.driver,"Add_Existing_Recipient_Failed");
            Assert.fail("Element not found: " + e.getMessage());
        }finally {
            DriverManager.driver.navigate().back();
            sendMoneyPage.clickBack();

        }

    }

    @Test(dataProvider = "getMultipleDataSet", priority = 4)
    public void updateExistingProfile(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "recipientName", "updateRecipientName", "updateRecipientPhone"
        );

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        sendMoneyPage.clickSendMoneyButton();
        sendMoneyPage.getExistingProfile(input.get("recipientName"));
        attachScreenshot(DriverManager.driver, "Profile_Before_Update");
        sendMoneyPage.editProfile();
        sendMoneyPage.updateRecipient(input.get("updateRecipientName"),input.get("updateRecipientPhone"));
        try {
            String addedPhone = sendMoneyPage.getAddedProfile();
            Assert.assertEquals(addedPhone, input.get("updateRecipientPhone"));
            attachScreenshot(DriverManager.driver, "Profile_After_Update");
            log.info("Updated phone added: {}",input.get("updateRecipientPhone"));
        } catch (NoSuchElementException e) {
            log.warn("Test failed to update profile");
            Assert.fail("Element not found: " + e.getMessage());
        } finally {
            sendMoneyPage.clickBack();
        }
    }

    @Test(dataProvider = "getMultipleDataSet",dependsOnMethods = "updateExistingProfile", priority = 4)
    public void deleteExistingRecipient(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "updateRecipientName"
        );


        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        sendMoneyPage.clickSendMoneyButton();
        sendMoneyPage.getExistingProfile(input.get("updateRecipientName"));
        attachScreenshot(DriverManager.driver, "Profile_Before_Deletion");

        sendMoneyPage.editProfile();
        sendMoneyPage.clickDeleteProfile();

        try {
            Assert.assertTrue(quickPayPage.isRecipientDeleted(input.get("updateRecipientName")));
            log.info("Recipient deletion confirmed: {}", input.get("updateRecipientName"));
            attachScreenshot(DriverManager.driver, "Recipient_Deleted_Success");
        } catch (AssertionError e) {
            log.warn("Failed to delete payment recipient");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            log.info("Recipient still exists: {}", input.get("updateRecipientName"));
            throw e;  // Let TestNG fail the test
        }

        DriverManager.driver.navigate().back();
        sendMoneyPage.clickBack();
    }

    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//sendMoneyData.json");
        return new Object[][]{{data.getFirst()}};
    }

    @AfterMethod
    public void cleanUp() {
        try {
            HomePage homePage = new HomePage(DriverManager.driver);
            homePage.clickLogoutButtn();

        } catch (Exception e) {
            log.error("Cleanup failed: ", e);
        }
    }
}
