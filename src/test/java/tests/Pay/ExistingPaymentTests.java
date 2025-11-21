package tests.Pay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.pay.QuickPayPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ExistingPaymentTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(ExistingPaymentTests.class);

    @Test(dataProvider = "getMultipleDataSet", priority = 0)
    public void PaymentRedoTest(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        QuickPayPage quickPayPage = new QuickPayPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(input.get("recipientName"));
        quickPayPage.clickRedo();
        quickPayPage.clickPay2Buttn();
        quickPayPage.clickConfirmButton();

        try {
            Assert.assertTrue(quickPayPage.getPaymentStatus());
            log.info("Payment status: {}",quickPayPage.getPaymentStatus());
        } catch (AssertionError e) {
            log.warn("Failed to do payment transaction");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        }finally {
            quickPayPage.clickFinish();
        }
        homePage.clickLogoutButtn();
    }

    @Test(dataProvider = "getMultipleDataSet", priority = 1)
    public void paymentToExistingRecipient(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        QuickPayPage quickPayPage = new QuickPayPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(input.get("recipientName"));
        quickPayPage.enterPaymentDetails(input.get("redoAmount"),input.get("ref"));
        quickPayPage.clickPay2Buttn();
        quickPayPage.clickConfirmButton();

        try {
            Assert.assertTrue(quickPayPage.getPaymentStatus());
            log.info("Payment status: {}",quickPayPage.getPaymentStatus());
        } catch (AssertionError e) {
            log.warn("Failed to do payment transaction");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        }finally {
            quickPayPage.clickFinish();
        }
        homePage.clickLogoutButtn();
    }

    @Test(dataProvider = "getMultipleDataSet", priority = 2)
    public void paymentWithAttachment(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        QuickPayPage quickPayPage = new QuickPayPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(input.get("recipientName"));
        quickPayPage.enterPaymentDetails(input.get("amount"),input.get("ref"));
        quickPayPage.addAttachment();
        quickPayPage.clickPay2Buttn();

        SoftAssert softAssert = new SoftAssert(); // TestNG’s SoftAssert

        try {
            softAssert.assertEquals(quickPayPage.getAttachment(), "sample-pdf.pdf");
            log.info("Found attached document: {}",quickPayPage.getAttachment());
            quickPayPage.clickConfirmButton();
            softAssert.assertTrue(quickPayPage.getPaymentStatus());
            log.info("Payment status: {}",quickPayPage.getPaymentStatus());

        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            softAssert.fail("Test crashed: " + e.getMessage());
        } finally {
            quickPayPage.clickFinish();
            softAssert.assertAll();
        }
        homePage.clickLogoutButtn();
    }


    @Test(dataProvider = "getMultipleDataSet", priority = 5)
    public void updateExistingRecipient(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        QuickPayPage quickPayPage = new QuickPayPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(input.get("recipientName"));
        quickPayPage.editProfile();
        quickPayPage.updateRecipientDetails(input.get("updateRecipientName"),input.get("updateGroup"),input.get("updateBank"),input.get("updateAcc"),input.get("updateAcc"));

        try {
            String expectedAccNo =  quickPayPage.getAccNo();
            log.info("Assertion expectation: {}",expectedAccNo);

            Assert.assertEquals(expectedAccNo, "Account"+ " " + input.get("updateAcc"));

        } catch (AssertionError e) {
            log.warn("Failed to add payment recipient");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        }
    }

    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//payData.json");
        return new Object[][]{{data.get(1)}};
    }

}
