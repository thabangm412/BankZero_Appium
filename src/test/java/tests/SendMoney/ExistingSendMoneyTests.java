package tests.SendMoney;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.sedMoney.SendMoneyPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;
import utils.AppiumUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ExistingSendMoneyTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(ExistingSendMoneyTests.class);

    @Test(dataProvider = "getMultipleDataSet",priority = 0)
    public void sendMoneyToExistingProfile(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        SendMoneyPage sendMoneyPage = new SendMoneyPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        sendMoneyPage.clickSendMoneyButton();
        sendMoneyPage.getExistingProfile(input.get("recipientName"));
        sendMoneyPage.sendMoney(input.get("redoAmount"),input.get("ref"));
        sendMoneyPage.clickSend();
        sendMoneyPage.clickConfirm();

        try {
            try {
                Assert.assertEquals(sendMoneyPage.getStatus(), "Thank you");
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

    @Test(dataProvider = "getMultipleDataSet", priority = 1)
    public void sendMoneyWithAttachment(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        SendMoneyPage sendMoneyPage = new SendMoneyPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        sendMoneyPage.clickSendMoneyButton();
        sendMoneyPage.getExistingProfile(input.get("recipientName"));
        sendMoneyPage.sendMoney(input.get("amount"),input.get("ref"));
        sendMoneyPage.addAttachment();
        AppiumUtils.waitForElement(By.id("za.co.neolabs.bankzero:id/fileImage"),driver);
        sendMoneyPage.clickSend();
        sendMoneyPage.clickConfirm();

        SoftAssert softAssert = new SoftAssert(); // TestNG’s SoftAssert

        try {
            softAssert.assertTrue(sendMoneyPage.getAttachment(), "Attachment missing");
            softAssert.assertEquals(sendMoneyPage.getStatus(), "Thank you", "Wrong status");

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

    @Test(dataProvider = "getMultipleDataSet",priority = 2)
    public void sendMoneyWithRedo(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        SendMoneyPage sendMoneyPage = new SendMoneyPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        sendMoneyPage.clickSendMoneyButton();
        sendMoneyPage.getExistingProfile(input.get("recipientName"));
        sendMoneyPage.clickRedo();
        sendMoneyPage.clickSend();
        sendMoneyPage.clickConfirm();

        try {
            try {
                Assert.assertEquals(sendMoneyPage.getStatus(), "Thank you");
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

    @Test(dataProvider = "getMultipleDataSet", priority = 3)
    public void updateExistingProfile(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        SendMoneyPage sendMoneyPage = new SendMoneyPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        sendMoneyPage.clickSendMoneyButton();
        sendMoneyPage.getExistingProfile(input.get("recipientName"));
        sendMoneyPage.editProfile();
        sendMoneyPage.updateRecipient(input.get("updateRecipientName"),input.get("updateRecipientPhone"));
        try {
            String addedPhone = sendMoneyPage.getAddedProfile();
            Assert.assertEquals(addedPhone, input.get("updateRecipientPhone"));
            log.info("Updated phone added: {}",input.get("updateRecipientPhone"));
        } catch (NoSuchElementException e) {
            log.warn("Test failed to update profile");
            Assert.fail("Element not found: " + e.getMessage());
        } finally {
            sendMoneyPage.clickBack();
        }
    }

    @Test(dataProvider = "getMultipleDataSet",priority = 4)
    public void deleteExistingRecipient(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        SendMoneyPage sendMoneyPage = new SendMoneyPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        sendMoneyPage.clickSendMoneyButton();
        sendMoneyPage.getExistingProfile(input.get("updateRecipientName"));

        sendMoneyPage.editProfile();
        sendMoneyPage.clickDeleteProfile();
        sendMoneyPage.clickUpdate();

        try {
            boolean isDisplayed = driver.findElement(By.id("za.co.neolabs.bankzero:id/tile_to_mask")).isDisplayed();
            Assert.assertTrue(isDisplayed);
            log.info("Profile deleted, send money page displayed");
        } catch (NoSuchElementException e) {

            log.warn("Test failed to delete profile");
            Assert.fail("Element not found: " + e.getMessage());
        } finally {
            sendMoneyPage.clickBack();
        }
    }

    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//sendMoneyData.json");
        return new Object[][]{{data.getFirst()}};
    }

    @AfterMethod
    public void cleanUp() {
        try {
            HomePage homePage = new HomePage(driver);
            homePage.clickLogoutButtn();

        } catch (Exception e) {
            log.error("Cleanup failed: ", e);
        }
    }
}
