package tests.PayMany;

import factory.PaymentDataFactory;
import factory.TransferDataFactory;
import models.PayManyData;
import models.User;
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

    private PayManyPage payManyPage;
    private User appUser;
    private PayManyData payManyData;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        payManyPage = new PayManyPage(DriverManager.driver);
        appUser = TransferDataFactory.validAppUser();
        payManyData = PaymentDataFactory.validPayManyData();

        log.debug("Page objects and androidActions initialized");
    }

    @Test(priority = 0)
    public void addNewRecipient() throws InterruptedException {

        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsButtn();
        payManyPage.clickPayManyButton();
        payManyPage.clickAddRecipient();
        payManyPage.addRecipientDetails(payManyData.getRecipientName(),payManyData.getGroup(),payManyData.getBank(),payManyData.getAccount(), payManyData.getAccountNo());
        payManyPage.enterPOPDetails(payManyData.getPopEmail(),payManyData.getPopPhone());
        payManyPage.clickAddButton();
        Thread.sleep(3000);
        payManyPage.getGroups(payManyData.getGroup());
        attachScreenshot(DriverManager.driver,"RecipientDetails added");
        Assert.assertTrue(payManyPage.getRecipientNames().contains(payManyData.getRecipientName()), "Recipient name does not match expected value");
        attachScreenshot(DriverManager.driver,"Recipient added successfully");

    }

    @Test(priority = 1)
    public void makePaymentToRecipient()
    {

        payManyPage.clickNewPayment(payManyData.getRecipientName());
        payManyPage.clickAttachments();
        payManyPage.addAttachments();
        payManyPage.enterAmount(payManyData.getAmount());
        payManyPage.clickFinish();
        attachScreenshot(DriverManager.driver,"Payment details");
        payManyPage.clickConfirmButton();

        String actualTxt = payManyPage.transactionStatus();
        log.info("Payment transaction status: {}", actualTxt);
        Assert.assertEquals(actualTxt,"Thank you");
        attachScreenshot(DriverManager.driver,"Payment successful");
        payManyPage.clickFinish();
        homePage.clickLogoutButtn();

    }

}
