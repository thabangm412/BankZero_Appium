package tests.Transfer;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.pay.QuickPayPage;
import pageObjects.app.accountsActionMenu.sendMoney.SendMoneyPage;
import pageObjects.app.accountsActionMenu.transfer.TransferPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;
import utils.AppiumUtils;
import utils.DriverManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class TransferMoneyTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(TransferMoneyTests.class);
    private LoginPage loginPage;
    private QuickPayPage quickPayPage;
    private HomePage homePage;
    private AccountMenuActions accountMenuActions;
    private AndroidActions androidActions;
    private SendMoneyPage sendMoneyPage;
    private TransferPage transferPage;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        sendMoneyPage = new SendMoneyPage(DriverManager.driver);
        androidActions = new AndroidActions(DriverManager.driver);
        transferPage = new TransferPage(DriverManager.driver);

        log.debug("Page objects and androidActions initialized");
    }

    @Test(dataProvider = "getMultipleDataSet",priority = 0)
    public void transferMoney(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "accountName", "amount", "ref"
        );

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        transferPage.clickTransferButton();
        transferPage.selectExistingAccount(input.get("accountName"));
        transferPage.transferMoney(input.get("amount"),input.get("ref"));
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


    @Test(dataProvider = "getMultipleDataSet",priority = 1)
    public void scheduleTransferOnceOff(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "accountName", "amount", "ref",
                "scheduleTypeOnceOff", "daysToAdd"
        );

        int daysToAdd = Integer.parseInt(input.get("daysToAdd"));  // 3
        double amount = Double.parseDouble(input.get("amount"));
        String formattedAmount = String.format("R%.2f", amount);
        String futureDate = AppiumUtils.getFutureDate(daysToAdd);
        String scheduleTypeLower = input.get("scheduleTypeOnceOff").toLowerCase();

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        transferPage.clickTransferButton();
        transferPage.selectExistingAccount(input.get("accountName"));

        transferPage.clickSchedule();
        transferPage.chooseTransferSchedule(input.get("scheduleTypeOnceOff"),Integer.parseInt(input.get("daysToAdd")),input.get("ref"),input.get("amount"));

        try {
            String expectedTxt = formattedAmount + " " + scheduleTypeLower + " on " + futureDate;
            log.info("Assertion expectation: {}", expectedTxt);

            Assert.assertEquals(DriverManager.driver.findElement(By.id("za.co.neolabs.bankzero:id/product_type")).getText(), expectedTxt);
            log.info("Assertion passed for scheduled transfer: {}", expectedTxt);
            attachScreenshot(DriverManager.driver, "Schedule Once-off Payment - " + name);


        } catch (AssertionError | Exception e) {
            log.warn("Failed to do schedule transfer");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        } finally {
            transferPage.clickBack();

        }
    }

    @Test(dataProvider = "getMultipleDataSet",priority = 2)
    public void scheduleTransferWeekly(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "accountName", "amount", "ref",
                "scheduleTypeWeekly", "daysToAdd"
        );

        String scheduleType = input.get("scheduleTypeWeekly");
        int daysToAdd = Integer.parseInt(input.get("daysToAdd"));
        double amount = Double.parseDouble(input.get("amount"));
        String formattedAmount = String.format("R%.2f", amount);
        String toDate = AppiumUtils.getFutureDate(daysToAdd + 7);
        String scheduleTypeLower = input.get("scheduleTypeWeekly").toLowerCase();

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        transferPage.clickTransferButton();
        transferPage.selectExistingAccount(input.get("accountName"));
        transferPage.clickSchedule();
        transferPage.chooseTransferSchedule(scheduleType,Integer.parseInt(input.get("daysToAdd")),input.get("ref"),input.get("amount"));

        try {
            String expectedTxt = formattedAmount + " " + scheduleTypeLower + " on Monday till " + toDate;
            log.info("Future date calculated for assertion: {}", toDate);
            Assert.assertEquals(DriverManager.driver.findElement(By.id("za.co.neolabs.bankzero:id/product_type")).getText(), expectedTxt);
            log.info("Assertion passed for scheduled transfer: {}", expectedTxt);
            attachScreenshot(DriverManager.driver, "Schedule Weekly Payment - " + name);

        } catch (AssertionError | Exception e) {
            log.warn("Failed to do schedule transfer");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        } finally {
            transferPage.clickBack();
            homePage.clickLogoutButtn();
        }

    }

    @Test(dataProvider = "getMultipleDataSet",priority = 3)
    public void scheduleTransferMonthly(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "accountName", "amount", "ref",
                "scheduleTypeMonthly", "daysToAdd"
        );

        String scheduleType = input.get("scheduleTypeMonthly");
        int daysToAdd = Integer.parseInt(input.get("daysToAdd"));
        double amount = Double.parseDouble(input.get("amount"));
        String formattedAmount = String.format("R%.2f", amount);
        String monthlyToDate = AppiumUtils.getFutureDate(daysToAdd + 35);
        String scheduleTypeLower = scheduleType.toLowerCase();

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        transferPage.clickTransferButton();
        transferPage.selectExistingAccount(input.get("accountName"));
        transferPage.clickSchedule();
        transferPage.chooseTransferSchedule(scheduleType,Integer.parseInt(input.get("daysToAdd")),input.get("ref"),input.get("amount"));

        try {
            String expectedTxt = formattedAmount + " " + scheduleTypeLower + " on 2nd till " + monthlyToDate;
            log.info("Assertion expectation: {}",expectedTxt);

            Assert.assertEquals(DriverManager.driver.findElement(By.id("za.co.neolabs.bankzero:id/product_type")).getText(), expectedTxt);
            attachScreenshot(DriverManager.driver, "Schedule Monthly Payment - " + name);
            log.info("Assertion passed for scheduled transfer: {}", expectedTxt);

        } catch (AssertionError | Exception e) {
            log.warn("Failed to do schedule transfer");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        } finally {
            transferPage.clickBack();
            homePage.clickLogoutButtn();
        }

    }

    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//transferMoneyData.json");
        return new Object[][]{{data.getFirst()}};
    }

    @AfterMethod
    public void cleanUp() {
        try {
            HomePage homePage = new HomePage(DriverManager.driver);
            homePage.clickLogoutButtn();

        } catch (Exception e) {
            log.error("Cleanup failed: ", e);
        }
    }

}
