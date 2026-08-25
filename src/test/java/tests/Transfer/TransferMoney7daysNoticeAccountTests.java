package tests.Transfer;

import factory.TransferDataFactory;
import models.TransferData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.pay.QuickPayPage;
import pageObjects.app.accountsActionMenu.sendMoney.SendMoneyPage;
import pageObjects.app.accountsActionMenu.transfer.TransferPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;
import utils.DriverManager;

import java.util.HashMap;

public class TransferMoney7daysNoticeAccountTests extends BaseTestsConfig {
    private static final Logger log = LoggerFactory.getLogger(TransferMoney7daysNoticeAccountTests.class);
    private LoginPage loginPage;
    private QuickPayPage quickPayPage;
    private HomePage homePage;
    private AccountMenuActions accountMenuActions;
    private AndroidActions androidActions;
    private SendMoneyPage sendMoneyPage;
    private TransferPage transferPage;
    private TransferData transferData;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        sendMoneyPage = new SendMoneyPage(DriverManager.driver);
        androidActions = new AndroidActions(DriverManager.driver);
        transferPage = new TransferPage(DriverManager.driver);
        transferData = TransferDataFactory.valid7daysNoticeTransfer();

        log.debug("Page objects and androidActions initialized");
    }

    @Test(priority = 0)
    public void transferMoneyTo7DaysNoticeAccount()
    {
        loginPage.loginWithRetry(
                transferData.getUser().getProfileName(),
                transferData.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsButtn();
        transferPage.clickTransferButton();
        transferPage.selectExistingAccount(transferData.getAccountName());
        transferPage.transferMoney(transferData.getAmount(), transferData.getRef());
        attachScreenshot(DriverManager.driver,"TransferDetails");

        transferPage.clickTransfer();
        transferPage.clickConfrim();

        try {
            try {
                Assert.assertEquals(transferPage.getTransferStatus(),"Transfer success");
                log.info("Transfer status: {}",transferPage.getTransferStatus());
                attachScreenshot(DriverManager.driver,"TransferSuccess");
            } catch (AssertionError e) {
                log.warn("Failed to do transfer transaction");
                throw e;
            }
        } catch (Exception e) {
            log.error("Exception occurred during adding profile: ", e);
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
        transferPage.clickFinish();
    }


}
