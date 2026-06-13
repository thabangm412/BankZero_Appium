package tests.Business.Pay;

import factory.BusinessDataFactory;
import factory.TransferDataFactory;
import models.BusinessData;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.pay.QuickPayPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import tests.Pay.PaymentTests;
import utils.DriverManager;

import java.util.HashMap;

public class NewBusinessRecipientTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(NewBusinessRecipientTests.class);
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
    public void AddNewRecipientForBusiness()
    {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        quickPayPage.clickPayButtn();
        quickPayPage.clickAddRecipientButton();
        quickPayPage.addRecipientDetails(businessPayData.getRecipientName1(),businessPayData.getGroup(),businessPayData.getBank(),businessPayData.getAccount(),businessPayData.getAccountNo1());
        quickPayPage.addPoP(businessPayData.getPopEmail(),businessPayData.getPopPhone());
        quickPayPage.clickAddButton();
        Assert.assertEquals(quickPayPage.getAccName(),businessPayData.getRecipientName1());
        attachScreenshot(DriverManager.driver, "Recipient_Added_Success");

    }

    @Test(priority = 1)
    public void PaymentToAddedRecipientWithPoPTest() throws InterruptedException {

        quickPayPage.enterPaymentDetails(businessPayData.getAmount(),businessPayData.getRef());
        quickPayPage.clickPay2Buttn();
        attachScreenshot(DriverManager.driver, "Payment_Confirmation");
        quickPayPage.clickConfirmButton();

        Assert.assertTrue(quickPayPage.getPaymentStatus());
        log.info("Payment status: {}",quickPayPage.getPaymentStatus());
        attachScreenshot(DriverManager.driver, "Payment_Success");
        Thread.sleep(3000); // Wait for 3 seconds before clicking finish
        quickPayPage.clickFinish();
        homePage.clickLogoutButtn();

    }

}
