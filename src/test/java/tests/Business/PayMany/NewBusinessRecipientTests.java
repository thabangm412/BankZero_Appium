package tests.Business.PayMany;

import factory.BusinessDataFactory;
import factory.PaymentDataFactory;
import factory.TransferDataFactory;
import models.BusinessData;
import models.PayManyData;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.payMany.PayManyPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import tests.PayMany.NewRecipientTests;
import utils.DriverManager;

public class NewBusinessRecipientTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(NewBusinessRecipientTests.class);

    private LoginPage loginPage;
    private HomePage homePage;
    private AccountMenuActions accountMenuActions;

    private PayManyPage payManyPage;
    private User appUser;
    private BusinessData payManyData;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        payManyPage = new PayManyPage(DriverManager.driver);
        appUser = TransferDataFactory.validAppUser();
        payManyData = BusinessDataFactory.validPayBusinessData();

        log.debug("Page objects and androidActions initialized");
    }

    @Test(priority = 0)
    public void addNewRecipientForBusiness() throws InterruptedException {

        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        payManyPage.clickPayManyButton();
        payManyPage.clickAddRecipient();
        payManyPage.addRecipientDetails(payManyData.getRecipientName1(),payManyData.getGroup(),payManyData.getBank(),payManyData.getAccount(), payManyData.getAccountNo1());
        payManyPage.enterPOPDetails(payManyData.getPopEmail(),payManyData.getPopPhone());
        payManyPage.clickAddButton();
        DriverManager.driver.navigate().back();
        accountMenuActions.clickAccountMenuActionsOption("Business");
        payManyPage.clickPayManyButton();
        Thread.sleep(2000);
        payManyPage.getGroups(payManyData.getGroup());
        attachScreenshot(DriverManager.driver,"RecipientDetails added");
        Assert.assertTrue(payManyPage.getRecipientNames().contains(payManyData.getRecipientName1()), "Recipient name does not match expected value");
        attachScreenshot(DriverManager.driver,"Recipient added successfully");

    }

    @Test(priority = 1)
    public void makePaymentToRecipientForBusiness() throws InterruptedException {

        DriverManager.driver.navigate().back();
        accountMenuActions.clickAccountMenuActionsOption("Business");
        payManyPage.clickPayManyButton();
        payManyPage.getGroups(payManyData.getGroup());
        payManyPage.clickNewPayment(payManyData.getRecipientName1());
        payManyPage.clickAttachments();
        payManyPage.addAttachments();
        payManyPage.enterAmount(payManyData.getAmount());
        payManyPage.clickFinish();
        attachScreenshot(DriverManager.driver,"Payment details");
        payManyPage.clickConfirmButton();
        payManyPage.duplicatePaymentCheck();
        try {
            Assert.assertEquals(payManyPage.transactionStatus(),"Thank you");
            attachScreenshot(DriverManager.driver,"Payment successful");
        } catch (AssertionError e) {
            log.error("Payment failed: {}", e.getMessage());
            throw e; // Rethrow the exception to fail the test
        }finally {
            payManyPage.clickFinish();
            homePage.clickLogoutButtn();
        }

    }

}
