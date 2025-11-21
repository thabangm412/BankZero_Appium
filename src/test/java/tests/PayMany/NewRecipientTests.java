package tests.PayMany;

import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.pay.QuickPayPage;
import pageObjects.app.accountsActionMenu.payMany.PayManyPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class NewRecipientTests extends BaseTestsConfig {
    private static final Logger log = LoggerFactory.getLogger(NewRecipientTests.class);

    @Test(dataProvider = "getMultipleDataSet",priority = 0)
    public void addNewRecipient(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        QuickPayPage quickPayPage = new QuickPayPage(driver);
        PayManyPage payManyPage = new PayManyPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

       // androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        payManyPage.clickPayManyButton();
        payManyPage.clickAddRecipient();
        payManyPage.addRecipientDetails(input.get("recipientName"),input.get("group"),input.get("bank"),input.get("account"),input.get("accountNo"));
        payManyPage.enterPOPDetails(input.get("popEmail"),input.get("popPhone"));
        payManyPage.clickAddButton();
        payManyPage.getGroups(input.get("group"));

        try {
            String expectedTxt =  input.get("recipientName");
            log.info("Assertion expectation: {}",expectedTxt);
            String actualTxt = payManyPage.getRecipientName();
            Assert.assertEquals(actualTxt,expectedTxt);

        } catch (AssertionError e) {
            log.warn("Failed to add payment recipient");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        }

    }

    @Test(dataProvider = "getMultipleDataSet",priority = 1)
    public void makePaymentToRecipient(HashMap<String, String> input)
    {
        PayManyPage payManyPage = new PayManyPage(driver);
        HomePage homePage = new HomePage(driver);
        payManyPage.clickNewPayment(input.get("recipientName"));
        payManyPage.clickAttachments();
        payManyPage.addAttachments();
        payManyPage.enterAmount(input.get("amount"));
        payManyPage.clickPayButton();
        payManyPage.clickConfirmButton();

        try {
            String actualTxt = payManyPage.transactionStatus();
            Assert.assertEquals(actualTxt,"Thank you");
        }catch (AssertionError | NoSuchElementException e)
        {
            log.warn("Failed to do payment transaction");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;
        }finally {
            payManyPage.clickFinish();
        }
        homePage.clickLogoutButtn();

    }

    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//payManyData.json");
        return new Object[][]{{data.getFirst()}};
    }
}
