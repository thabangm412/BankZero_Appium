package tests.Pay;

import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.pay.QuickPayPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AppiumUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import static utils.Listeners.TEST_THREAD;

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
                "recipientName1", "group", "bank", "account", "accountNo1",
                "popEmail", "popPhone"
        );

        //androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.clickAddRecipientButton();

        quickPayPage.addRecipientDetails(input.get("recipientName1"),input.get("group"),input.get("bank"),input.get("account"),input.get("accountNo1"));
        quickPayPage.addPoP(input.get("popEmail"),input.get("popPhone"));
        quickPayPage.clickAddButton();
        try {
            String expectedTxt =  quickPayPage.getAccName();
            log.info("Assertion expectation: {}",expectedTxt);

            Assert.assertEquals(expectedTxt,input.get("recipientName1"));

            attachScreenshot(driver, "Recipient_Added_Success");

        } catch (AssertionError e) {
            log.warn("Failed to add payment recipient");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        }
    }

    @Test(dataProvider = "getMultipleDataSet", priority = 1)
    public void PaymentToAddedRecipientWithPoPTest(HashMap<String, String> input) throws InterruptedException {
        validateInput(input,
                "profileName", "loginPin",
                "amount", "ref"
        );
        quickPayPage.enterPaymentDetails(input.get("amount"),input.get("ref"));
        quickPayPage.clickPay2Buttn();
        attachScreenshot(driver, "Payment_Confirmation");
        quickPayPage.clickConfirmButton();

        try {
            Assert.assertTrue(quickPayPage.getPaymentStatus());
            log.info("Payment status: {}",quickPayPage.getPaymentStatus());
            // ✅ Screenshot at EXACT success moment
            attachScreenshot(driver, "Payment_Success");


        } catch (AssertionError e) {
            log.warn("Failed to do payment transaction");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        }finally {
            Thread.sleep(3000); // Wait for 3 seconds before clicking finish
            quickPayPage.clickFinish();
        }
        homePage.clickLogoutButtn();
    }

    @Test(dataProvider = "getMultipleDataSet",priority = 2)
    public void AddRecipientTestWithoutPoP(HashMap<String, String> input) throws InterruptedException {

        validateInput(input,
                "profileName", "loginPin",
                "recipientName2", "group", "bank", "account", "accountNo2",
                "popEmail", "popPhone"
        );

        //androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.clickAddRecipientButton();

        quickPayPage.addRecipientDetails(input.get("recipientName2"),input.get("group"),input.get("bank"),input.get("account"),input.get("accountNo2"));
        //quickPayPage.addPoP(input.get("popEmail"),input.get("popPhone"));
        quickPayPage.clickAddButton();
        try {
            String expectedTxt =  quickPayPage.getAccName();
            log.info("Assertion expectation: {}",expectedTxt);

            Assert.assertEquals(expectedTxt,input.get("recipientName2"));

            attachScreenshot(driver, "Recipient_Added_Success");

        } catch (AssertionError e) {
            log.warn("Failed to add payment recipient");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        }
    }

    @Test(dataProvider = "getMultipleDataSet", priority = 3)
    public void PaymentToAddedRecipientWithoutPoPTest(HashMap<String, String> input) throws InterruptedException {
        validateInput(input,
                "profileName", "loginPin",
                "amount", "ref"
        );
        quickPayPage.enterPaymentDetails(input.get("amount"),input.get("ref"));
        quickPayPage.clickPay2Buttn();
        attachScreenshot(driver, "Payment_Confirmation");
        quickPayPage.clickConfirmButton();

        try {
            Assert.assertTrue(quickPayPage.getPaymentStatus());
            log.info("Payment status: {}",quickPayPage.getPaymentStatus());
            // ✅ Screenshot at EXACT success moment
            attachScreenshot(driver, "Payment_Success");


        } catch (AssertionError e) {
            log.warn("Failed to do payment transaction");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        }finally {
            Thread.sleep(3000); // Wait for 3 seconds before clicking finish
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

//    public void attachScreenshot(AppiumDriver driver, String screenshotName) {
//        try {
//            ExtentTest test = getTest();
//            if (test != null && driver != null) {
//                String base64Screenshot = getBase64Screenshot(driver);
//                test.addScreenCaptureFromBase64String(base64Screenshot, screenshotName);
//            }
//        } catch (Exception e) {
//            log.warn("Could not capture screenshot: {0}", e.getMessage());
//            ExtentTest test = getTest();
//            if (test != null) {
//                test.warning("Could not capture screenshot: " + e.getMessage());
//            }
//        }
//    }
//
//     private ExtentTest getTest() {
//        return TEST_THREAD.get();
//    }


}


