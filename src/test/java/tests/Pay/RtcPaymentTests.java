package tests.Pay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.pay.QuickPayPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class RtcPaymentTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(RtcPaymentTests.class);

    @Test(dataProvider = "getMultipleDataSet",priority = 0)
    public void AddRtcRecipientTestWithPoP(HashMap<String, String> input) throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        QuickPayPage quickPayPage = new QuickPayPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.clickAddRecipientButton();

        quickPayPage.addRecipientDetails(input.get("recipientName"),input.get("group"),input.get("bank"),input.get("account"),input.get("accountNo"));
        quickPayPage.addPoP(input.get("popEmail"),input.get("popPhone"));
        quickPayPage.clickAddButton();
        try {
            String expectedTxt =  quickPayPage.getAccName();
            log.info("Assertion expectation: {}",expectedTxt);

            Assert.assertEquals(expectedTxt,input.get("recipientName"));

        } catch (AssertionError e) {
            log.warn("Failed to add payment recipient");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        }
    }

    @Test(dataProvider = "getMultipleDataSet", priority = 1)
    public void RtCPaymentTest(HashMap<String, String> input)
    {
        QuickPayPage quickPayPage = new QuickPayPage(driver);
        HomePage homePage = new HomePage(driver);

        quickPayPage.enterPaymentDetails(input.get("amount"),input.get("ref"));
        quickPayPage.clickPayImmediatelyButtn();
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

    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//payData.json");
        return new Object[][]{{data.get(1)}};
    }
}
