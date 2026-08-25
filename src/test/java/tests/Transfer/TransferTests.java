package tests.Transfer;

import factory.TransferDataFactory;
import models.ScheduleTransferData;
import models.TransferData;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.pay.QuickPayPage;
import pageObjects.app.accountsActionMenu.transfer.TransferPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.DriverManager;

public class TransferTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(TransferMoneySavingsAccountTests.class);
    private LoginPage loginPage;
    private QuickPayPage quickPayPage;
    private HomePage homePage;
    private AccountMenuActions accountMenuActions;

    private TransferPage transferPage;
    private TransferData data;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        transferPage = new TransferPage(DriverManager.driver);
        data = TransferDataFactory.validTransfer();


        log.debug("Page objects and androidActions initialized");
    }

    @Test(priority = 0)
    public void transferMoney() {

        loginPage.loginWithRetry(
                data.getUser().getProfileName(),
                data.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsButtn();
        transferPage.clickTransferButton();
        transferPage.selectExistingAccount(data.getAccountName());
        transferPage.transferMoney(data.getAmount(), data.getRef());

        attachScreenshot(DriverManager.driver, "TransferDetails");

        transferPage.clickTransfer();
        transferPage.clickConfrim();

        Assert.assertEquals(
                transferPage.getTransferStatus(),
                "Transfer success"
        );

        transferPage.clickFinish();
        homePage.clickLogoutButtn();
    }

    @Test(priority = 1)
    public  void onceOffTransferTest(){

        ScheduleTransferData scheduleData = TransferDataFactory.onceOffTransfer();

        loginPage.loginWithRetry(
                data.getUser().getProfileName(),
                data.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsButtn();
        transferPage.clickTransferButton();
        transferPage.selectExistingAccount(scheduleData.getAccountName());
        transferPage.clickSchedule();
        transferPage.chooseTransferSchedule(scheduleData.getScheduleType(),scheduleData.getDaysToAdd(),scheduleData.getRef(),scheduleData.getAmount());

        attachScreenshot(DriverManager.driver, "OnceOffTransferDetails");

        String expectedTxt = scheduleData.getFormattedAmount() + " " + scheduleData.getScheduleTypeLowerCase() + " on " + scheduleData.getFutureDate();

        try {
            Assert.assertEquals(
                    DriverManager.driver.findElement(By.id("za.co.neolabs.bankzero:id/product_type")).getText(),
                    expectedTxt);
                log.info("Once-off transfer details match expected values: {}", expectedTxt);
                attachScreenshot(DriverManager.driver, "OnceOffTransferDetailsVerified");
        }catch (AssertionError e) {
            log.warn("Once-off transfer details do not match expected values");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        }
        transferPage.clickBack();
        homePage.clickLogoutButtn();
    }
}
