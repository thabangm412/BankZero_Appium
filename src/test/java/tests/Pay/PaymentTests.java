package tests.Pay;

import com.beust.ah.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.Registration.RegisterOTP;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.pay.QuickPayPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.addAccount.AddAccountPage;
import pageObjects.app.login.LoginPage;
import pageObjects.app.login.PairOnDevicePage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class PaymentTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(PaymentTests.class);
    private LoginPage loginPage;
    private HomePage homePage;
    private AccountMenuActions accountMenuActions;
    private QuickPayPage quickPayPage;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        accountMenuActions = new AccountMenuActions(driver);
        quickPayPage = new QuickPayPage(driver);

        log.debug("Page objects and androidActions initialized");
    }

    @Test(dataProvider = "getMultipleDataSet",priority = 0)
    public void AddRecipientTestWithPoP(HashMap<String, String> input) throws InterruptedException {

        validateInput(input,
                "profileName", "loginPin",
                "recipientName", "group", "bank", "account", "accountNo",
                "popEmail", "popPhone"
        );

        //androidActions.environmentChange();
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
    public void PaymentTest(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "amount", "ref"
        );
        quickPayPage.enterPaymentDetails(input.get("amount"),input.get("ref"));
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
        return new Object[][]{{data.get(0)}};
    }

    private void validateInput(HashMap<String, String> input, String... required) {
        if (input == null) throw new IllegalArgumentException("Input map is null");
        StringBuilder missing = new StringBuilder();
        for (String k : required) {
            if (input.get(k) == null || input.get(k).trim().isEmpty()) {
                if (missing.length() > 0) missing.append(", ");
                missing.append(k);
            }
        }
        if (missing.length() > 0) {
            log.error("Missing required keys: {}", missing.toString());
            throw new IllegalArgumentException("Missing required keys: " + missing.toString());
        }
    }
}


