package tests.Pay;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.pay.QuickPayPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;
import utils.DriverManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ExistingPaymentTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(ExistingPaymentTests.class);

    private LoginPage loginPage;
    private HomePage homePage;
    private QuickPayPage quickPayPage;
    private AccountMenuActions accountMenuActions;
    private AndroidActions androidActions;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        quickPayPage = new QuickPayPage(DriverManager.driver);
        androidActions = new AndroidActions(DriverManager.driver);

        log.debug("Page objects and androidActions initialized");
    }

    @Test(dataProvider = "getMultipleDataSet", priority = 0)
    public void PaymentRedoWithPoPTest(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "recipientName1"
        );

        //androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(input.get("recipientName1"));
        quickPayPage.clickRedo();
        quickPayPage.clickPay2Buttn();
        attachScreenshot(DriverManager.driver, "Payment_Redo_Confirmation");
        quickPayPage.clickConfirmButton();
        quickPayPage.possibleDuplicateCheck();

        try {
            Assert.assertTrue(quickPayPage.getPaymentStatus());
            log.info("Payment status: {}",quickPayPage.getPaymentStatus());

            attachScreenshot(DriverManager.driver, "Payment_Redo_Success");
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
    public void PaymentRedoWithoutPoPTest(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "recipientName2"
        );

        //androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(input.get("recipientName2"));
        quickPayPage.clickRedo();
        quickPayPage.clickPay2Buttn();
        attachScreenshot(DriverManager.driver, "Payment_Redo_Confirmation");
        quickPayPage.clickConfirmButton();
        quickPayPage.possibleDuplicateCheck();

        try {
            Assert.assertTrue(quickPayPage.getPaymentStatus());
            log.info("Payment status: {}",quickPayPage.getPaymentStatus());

            attachScreenshot(DriverManager.driver, "Payment_Redo_Success");
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
    public void paymentToExistingRecipientwithPoP(HashMap<String, String> input)
    {
      validateInput(input,
                "profileName", "loginPin",
                "recipientName1"
        );

        //androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(input.get("recipientName1"));
        quickPayPage.enterPaymentDetails(input.get("redoAmount"),input.get("ref"));
        quickPayPage.clickPay2Buttn();
        attachScreenshot(DriverManager.driver, "Payment_Redo_Confirmation");
        quickPayPage.clickConfirmButton();
        quickPayPage.possibleDuplicateCheck();

        try {
            Assert.assertTrue(quickPayPage.getPaymentStatus());
            log.info("Payment status: {}",quickPayPage.getPaymentStatus());

            attachScreenshot(DriverManager.driver, "Payment_ExistingRecipient_Success");
        } catch (AssertionError e) {
            log.warn("Failed to do payment transaction");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        }finally {
            quickPayPage.clickFinish();
        }
        homePage.clickLogoutButtn();
    }

    @Test(dataProvider = "getMultipleDataSet", priority = 3)
    public void paymentToExistingRecipientwithoutPoP(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "recipientName2"
        );

        //androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(input.get("recipientName2"));
        quickPayPage.enterPaymentDetails(input.get("redoAmount"),input.get("ref"));
        quickPayPage.clickPay2Buttn();
        attachScreenshot(DriverManager.driver, "Payment_Redo_Confirmation");
        quickPayPage.clickConfirmButton();
        quickPayPage.possibleDuplicateCheck();

        try {
            Assert.assertTrue(quickPayPage.getPaymentStatus());
            log.info("Payment status: {}",quickPayPage.getPaymentStatus());

            attachScreenshot(DriverManager.driver, "Payment_ExistingRecipient_Success");
        } catch (AssertionError e) {
            log.warn("Failed to do payment transaction");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        }finally {
            quickPayPage.clickFinish();
        }
        homePage.clickLogoutButtn();
    }

    @Test(dataProvider = "getMultipleDataSet", priority = 4)
    public void paymentWithAttachmentPlusPoP(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "recipientName1",
                "amount", "ref"
        );

        //androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(input.get("recipientName1"));
        quickPayPage.enterPaymentDetails(input.get("redoAmount"),input.get("ref"));
        quickPayPage.addAttachment();
        attachScreenshot(DriverManager.driver, "Attachment_Added");
        quickPayPage.clickPay2Buttn();

        SoftAssert softAssert = new SoftAssert(); // TestNG’s SoftAssert

        try {
            softAssert.assertEquals(quickPayPage.getAttachment(), "sample-pdf.pdf");
            log.info("Found attached document: {}",quickPayPage.getAttachment());
            attachScreenshot(DriverManager.driver, "Payment_With_Attachment");
            quickPayPage.clickConfirmButton();
            quickPayPage.possibleDuplicateCheck();
            softAssert.assertTrue(quickPayPage.getPaymentStatus());
            log.info("Payment status: {}",quickPayPage.getPaymentStatus());
            attachScreenshot(DriverManager.driver, "Payment_With_Attachment_Success");

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
    public void paymentWithAttachmentMinusPoP(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "recipientName2",
                "amount", "ref"
        );

        //androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(input.get("recipientName2"));
        quickPayPage.enterPaymentDetails(input.get("redoAmount"),input.get("ref"));
        quickPayPage.addAttachment();
        attachScreenshot(DriverManager.driver, "Attachment_Added");
        quickPayPage.clickPay2Buttn();

        SoftAssert softAssert = new SoftAssert(); // TestNG’s SoftAssert

        try {
            softAssert.assertEquals(quickPayPage.getAttachment(), "sample-pdf.pdf");
            log.info("Found attached document: {}",quickPayPage.getAttachment());
            attachScreenshot(DriverManager.driver, "Payment_With_Attachment");
            quickPayPage.clickConfirmButton();
            quickPayPage.possibleDuplicateCheck();
            softAssert.assertTrue(quickPayPage.getPaymentStatus());
            log.info("Payment status: {}",quickPayPage.getPaymentStatus());
            attachScreenshot(DriverManager.driver, "Payment_With_Attachment_Success");

        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            softAssert.fail("Test crashed: " + e.getMessage());
        } finally {
            quickPayPage.clickFinish();
            softAssert.assertAll();
        }
        homePage.clickLogoutButtn();
    }

    @Test(dataProvider = "getMultipleDataSet", priority = 6)
    public void addAlreadyExistingRecipientTest(HashMap<String, String> input)
    {

        validateInput(input,
                "profileName", "loginPin",
                "recipientName1", "group", "bank", "account", "accountNo1",
                "popEmail", "popPhone"
        );

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
            String toastMessage = DriverManager.driver.findElement(
                    By.id("za.co.neolabs.bankzero:id/snackbar_text")
            ).getText();
            //softVerifyEquals(toastMessage,"[79] We're sorry, you cannot add  as it already exists","Existing recipient error");
            Assert.assertEquals(toastMessage, "[79] We're sorry, you cannot add this recipient as it already exists");
            log.info("Recipient added: {}", toastMessage);
            attachScreenshot(DriverManager.driver, "Add_Existing_Recipient_Passed");
        } catch (NoSuchElementException| AssertionError e) {
            log.warn("Test failed to validate existing recipient addition");
            attachScreenshot(DriverManager.driver, "Add_Existing_Recipient_Failed");
            Assert.fail("Element not found: " + e.getMessage());
        }finally {
            DriverManager.driver.navigate().back();
            quickPayPage.clickBack();
            homePage.clickLogoutButtn();

//            if (!softFailures.isEmpty()) {
//                Assert.fail("Soft assertions failed:\n" + String.join("\n", softFailures));
//            }

        }
    }


    @Test(dataProvider = "getMultipleDataSet", priority = 7)
    public void updateExistingRecipient(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "recipientName2",
                "updateRecipientName", "updateGroup", "updateBank", "updateAcc"
        );

        //androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(input.get("recipientName2"));
        quickPayPage.editProfile();
        quickPayPage.updateRecipientDetails(input.get("updateRecipientName"),input.get("updateGroup"),input.get("updateBank"),input.get("updateAcc"),input.get("updateAccNo"));

        try {
            String actualAccNo =  quickPayPage.getAccNo();
            log.info("Assertion expectation: {}",actualAccNo);

            Assert.assertEquals(actualAccNo, "Account"+ " " + input.get("updateAccNo"));
            attachScreenshot(DriverManager.driver, "Recipient_Updated_Success");

        } catch (AssertionError e) {
            log.warn("Failed to add payment recipient");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        }
        DriverManager.driver.navigate().back();
//        quickPayPage.clickBack();
        homePage.clickLogoutButtn();
    }

    @Test(dataProvider = "getMultipleDataSet",dependsOnMethods = "updateExistingRecipient", priority = 8)
    public void deleteExistingRecipientTest(HashMap<String, String> input) throws InterruptedException {

        validateInput(input,
                "profileName", "loginPin",
                "updateRecipientName", "updateGroup", "updateBank", "updateAcc"
        );

        //androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(input.get("updateRecipientName"));
        quickPayPage.editProfile();
        Thread.sleep(2000);
        quickPayPage.clickDelete();


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
        quickPayPage.clickBack();
        homePage.clickLogoutButtn();
    }

    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//payData.json");
        return new Object[][]{{data.get(0)}};
    }


}
