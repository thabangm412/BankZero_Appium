package tests.PayMany;

import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.pay.QuickPayPage;
import pageObjects.app.accountsActionMenu.payMany.PayManyPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;
import utils.DriverManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class NewRecipientTests extends BaseTestsConfig {
    private static final Logger log = LoggerFactory.getLogger(NewRecipientTests.class);

    private LoginPage loginPage;
    private HomePage homePage;
    private AccountMenuActions accountMenuActions;
    private QuickPayPage quickPayPage;
    private AndroidActions androidActions;
    private PayManyPage payManyPage;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        quickPayPage = new QuickPayPage(DriverManager.driver);
        androidActions = new AndroidActions(DriverManager.driver);
        payManyPage = new PayManyPage(DriverManager.driver);

        log.debug("Page objects and androidActions initialized");
    }

    @Test(dataProvider = "getMultipleDataSet",priority = 0)
    public void addNewRecipient(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "recipientName", "group", "bank", "account", "accountNo",
                "popEmail", "popPhone"
        );

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
        attachScreenshot(DriverManager.driver,"RecipientDetails added");

        try {
            String expectedTxt =  input.get("recipientName");
            log.info("Assertion expectation: {}",expectedTxt);
            //String actualTxt = payManyPage.getRecipientName();
            Assert.assertTrue(payManyPage.getRecipientNames().contains(expectedTxt), "Recipient name does not match expected value");
            attachScreenshot(DriverManager.driver,"Recipient added successfully");

        } catch (AssertionError e) {
            log.warn("Failed to add payment recipient");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        }

    }

    @Test(dataProvider = "getMultipleDataSet",priority = 1)
    public void makePaymentToRecipient(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "recipientName",
                "amount"
        );

        payManyPage.clickNewPayment(input.get("recipientName"));
        payManyPage.clickAttachments();
        payManyPage.addAttachments();
        payManyPage.enterAmount(input.get("amount"));
        payManyPage.clickFinish();
        attachScreenshot(DriverManager.driver,"Payment details");
        payManyPage.clickConfirmButton();

        try {
            String actualTxt = payManyPage.transactionStatus();
            Assert.assertEquals(actualTxt,"Thank you");
            attachScreenshot(DriverManager.driver,"Payment successful");
        }catch (AssertionError | NoSuchElementException e)
        {
            log.warn("Failed to do payment transaction");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;
        }
        payManyPage.clickFinish();
        homePage.clickLogoutButtn();

    }

    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//payManyData.json");
        return new Object[][]{{data.getFirst()}};
    }

}
