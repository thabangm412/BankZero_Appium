package tests.NewAccounts.SavingsAccount;

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
import pageObjects.app.accountsActionMenu.transfer.TransferPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AppiumUtils;
import utils.DriverManager;

public class WithdrawalFromSavingsTests extends BaseTestsConfig {
    private static final Logger log = LoggerFactory.getLogger(WithdrawalFromSavingsTests.class);
    private LoginPage loginPage;
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
    public void transferMoneyFromSavings() {

        loginPage.loginWithRetry(
                data.getUser().getProfileName(),
                data.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Savings Test");
        transferPage.clickTransferButton();
        //transferPage.selectExistingAccount(data.getAccountName());
        transferPage.transferMoney(data.getAmount(), data.getRef());

        attachScreenshot(DriverManager.driver, "TransferDetails");

        transferPage.clickTransfer();
        transferPage.clickConfrim();

        Assert.assertEquals(
                transferPage.getTransferStatus(),
                "Transfer success"
        );
        attachScreenshot(DriverManager.driver, "TransferSuccess");

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

        accountMenuActions.clickAccountMenuActionsOption("Savings Test");
        transferPage.clickTransferButton();
        //transferPage.selectExistingAccount(scheduleData.getAccountName());
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
            throw e;  // Let TestNG fail the test
        }
        transferPage.clickBack();
        homePage.clickLogoutButtn();
    }

    @Test(priority = 2)
    public void scheduleTransferWeekly()
    {
        ScheduleTransferData scheduleData = TransferDataFactory.weeklyTransfer();

        loginPage.loginWithRetry(
                data.getUser().getProfileName(),
                data.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Savings Test");
        transferPage.clickTransferButton();
        //transferPage.selectExistingAccount(scheduleData.getAccountName());
        transferPage.clickSchedule();
        transferPage.chooseTransferSchedule(scheduleData.getScheduleType(),scheduleData.getDaysToAdd(),scheduleData.getRef(),scheduleData.getAmount());

        attachScreenshot(DriverManager.driver, "WeeklyTransferDetails");

        String toDate = AppiumUtils.getFutureDate(scheduleData.getDaysToAdd() + 7);
        log.info("Future date calculated for assertion: {}", toDate);
        String expectedTxt = scheduleData.getFormattedAmount() + " " + scheduleData.getScheduleTypeLowerCase() + " on Monday till " + toDate;
        try {

            Assert.assertEquals(DriverManager.driver.findElement(By.id("za.co.neolabs.bankzero:id/product_type"))
                    .getText(), expectedTxt);
            log.info("Assertion passed for scheduled transfer: {}", expectedTxt);
            attachScreenshot(DriverManager.driver, "WeeklyTransferDetailsVerified");
        } catch (AssertionError | Exception e) {
            log.warn("Failed to do schedule transfer");
            throw e;  // Let TestNG fail the test
        } finally {
            transferPage.clickBack();
            homePage.clickLogoutButtn();
        }
    }

    @Test(priority = 3)
    public void scheduleTransferMonthly()
    {
        ScheduleTransferData scheduleData = TransferDataFactory.monthlyTransfer();

        loginPage.loginWithRetry(
                data.getUser().getProfileName(),
                data.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Savings Test");
        transferPage.clickTransferButton();
        //transferPage.selectExistingAccount(scheduleData.getAccountName());
        transferPage.clickSchedule();
        transferPage.chooseTransferSchedule(scheduleData.getScheduleType(),scheduleData.getDaysToAdd(),scheduleData.getRef(),scheduleData.getAmount());

        attachScreenshot(DriverManager.driver, "WeeklyTransferDetails");

        String toDate = AppiumUtils.getFutureDate(scheduleData.getDaysToAdd() + 35);
        log.info("Future date calculated for assertion: {}", toDate);
        String expectedTxt = scheduleData.getFormattedAmount() + " " + scheduleData.getScheduleTypeLowerCase() + " on 2nd till " + toDate;
        try {

            Assert.assertEquals(DriverManager.driver.findElement(By.id("za.co.neolabs.bankzero:id/product_type"))
                    .getText(), expectedTxt);
            log.info("Assertion passed for scheduled transfer: {}", expectedTxt);
            attachScreenshot(DriverManager.driver, "WeeklyTransferDetailsVerified");
        } catch (AssertionError | Exception e) {
            log.warn("Failed to do schedule transfer");
            throw e;  // Let TestNG fail the test
        } finally {
            transferPage.clickBack();
            homePage.clickLogoutButtn();
        }
    }
}
