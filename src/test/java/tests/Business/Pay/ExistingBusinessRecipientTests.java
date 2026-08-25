package tests.Business.Pay;

import factory.BusinessDataFactory;
import factory.TransferDataFactory;
import models.BusinessData;
import models.User;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.pay.QuickPayPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import tests.Pay.PaymentTests;
import utils.DriverManager;

import java.util.HashMap;

public class ExistingBusinessRecipientTests extends BaseTestsConfig {
    private static final Logger log = LoggerFactory.getLogger(ExistingBusinessRecipientTests.class);
    private LoginPage loginPage;
    private HomePage homePage;
    private AccountMenuActions accountMenuActions;
    private QuickPayPage quickPayPage;
    private User appUser;
    private BusinessData businessPayData;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        quickPayPage = new QuickPayPage(DriverManager.driver);
        appUser = TransferDataFactory.validAppUser();
        businessPayData = BusinessDataFactory.validPayBusinessData();

        log.debug("Page objects and androidActions initialized");
    }

    @Test(priority = 0)
    public void RedoPaymentToExistingRecipientForBusiness()
    {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(businessPayData.getRecipientName1());
        quickPayPage.clickRedo();
        quickPayPage.clickPay2Buttn();
        attachScreenshot(DriverManager.driver, "Payment_Confirmation");
        quickPayPage.clickConfirmButton();
        quickPayPage.possibleDuplicateCheck();
        Assert.assertTrue(quickPayPage.getPaymentStatus());
        log.info("Payment status: {}",quickPayPage.getPaymentStatus());
        attachScreenshot(DriverManager.driver, "Payment_Redo_Success");
        quickPayPage.clickFinish();
        homePage.clickLogoutButtn();
    }

    @Test(priority = 1)
    public void paymentWithAttachmentPlusPoP()
    {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(businessPayData.getRecipientName1());
        quickPayPage.clickRedo();
        quickPayPage.addAttachment();
        attachScreenshot(DriverManager.driver, "Attachment_Added");
        quickPayPage.clickPay2Buttn();

        SoftAssert softAssert = new SoftAssert();
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

    @Test(priority = 2)
    public void addAlreadyExistingBusinessRecipientTest()
    {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        quickPayPage.clickPayButtn();
        quickPayPage.clickAddRecipientButton();
        quickPayPage.addRecipientDetails(businessPayData.getRecipientName1(), businessPayData.getGroup(), businessPayData.getBank(), businessPayData.getAccount(), businessPayData.getAccountNo1());
        quickPayPage.addPoP(businessPayData.getPopEmail(), businessPayData.getPopPhone());
        quickPayPage.clickAddButton();

        String toastMessage = DriverManager.driver.findElement(
                By.id("za.co.neolabs.bankzero:id/snackbar_text")
        ).getText();
        Assert.assertEquals(toastMessage, "[79] We're sorry, you cannot add this recipient as it already exists");
        log.warn("Failed to add recipient, error message: {}", toastMessage);
        attachScreenshot(DriverManager.driver, "Add_Existing_Recipient_Failed");
        DriverManager.driver.navigate().back();
        quickPayPage.clickBack();
        homePage.clickLogoutButtn();
    }

    @Test(priority = 3)
    public void updateExistingRecipient()
    {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(businessPayData.getRecipientName1());
        quickPayPage.editProfile();
        quickPayPage.updateRecipientDetails(businessPayData.getUpdateRecipientName(),businessPayData.getUpdateGroup(),businessPayData.getUpdateBank(),businessPayData.getUpdateAcc(),businessPayData.getUpdateAccNo());

        try {
            String actualAccNo =  quickPayPage.getAccNo();
            log.info("Assertion expectation: {}",actualAccNo);

            Assert.assertEquals(actualAccNo, "Account"+ " " + businessPayData.getUpdateAccNo());
            attachScreenshot(DriverManager.driver, "Recipient_Updated_Success");

        } catch (AssertionError e) {
            log.warn("Failed to add payment recipient");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        }
//        DriverManager.driver.navigate().back();
////        quickPayPage.clickBack();
//        homePage.clickLogoutButtn();
    }

    @Test(priority = 4)
    public void PaymentToUpdatedRecipientWithPoPTest() throws InterruptedException {

        quickPayPage.enterPaymentDetails(businessPayData.getAmount(),businessPayData.getRef());
        quickPayPage.clickPay2Buttn();
        attachScreenshot(DriverManager.driver, "Payment_Confirmation");
        quickPayPage.clickConfirmButton();

        Assert.assertTrue(quickPayPage.getPaymentStatus());
        log.info("Payment status: {}",quickPayPage.getPaymentStatus());
        attachScreenshot(DriverManager.driver, "Payment_Success");
        Thread.sleep(2000);
        quickPayPage.clickFinish();
        homePage.clickLogoutButtn();

    }

    @Test(priority = 5)
    public void deleteExistingRecipientForBusiness() throws InterruptedException {

        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(businessPayData.getUpdateRecipientName());
        quickPayPage.editProfile();
        Thread.sleep(2000);
        quickPayPage.clickDelete();
        Assert.assertTrue(quickPayPage.isRecipientDeleted(businessPayData.getUpdateRecipientName()));
        log.info("Recipient deletion confirmed: {}", businessPayData.getUpdateRecipientName());
        attachScreenshot(DriverManager.driver, "Recipient_Deleted_Success");
        DriverManager.driver.navigate().back();
        quickPayPage.clickBack();
        homePage.clickLogoutButtn();

    }
}
