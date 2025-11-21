package tests.Transfer;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.transfer.TransferPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;
import utils.AppiumUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class TransferMoneyTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(TransferMoneyTests.class);

    @Test(dataProvider = "getMultipleDataSet",priority = 0)
    public void transferMoney(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        TransferPage transferPage = new TransferPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        transferPage.clickTransferButton();
        transferPage.selectExistingAccount(input.get("accountName"));
        transferPage.transferMoney(input.get("amount"),input.get("ref"));

        transferPage.clickTransfer();
        transferPage.clickConfrim();

        try {
            try {
                Assert.assertEquals(transferPage.getTransferStatus(),"Transfer success");
                log.info("Transfer status: {}",transferPage.getTransferStatus());
            } catch (AssertionError e) {
                log.warn("Failed to do transfer transaction");
                throw e;  // Let TestNG fail the test
            }
        } catch (Exception e) {
            log.error("Exception occurred during adding profile: ", e);
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
        transferPage.clickFinish();
    }

//    @Test(dataProvider = "getMultipleDataSet",priority = 1)
//    public void scheduleTransferNever(HashMap<String, String> input)
//    {
//        LoginPage loginPage = new LoginPage(driver);
//        TransferPage transferPage = new TransferPage(driver);
//        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
//        AndroidActions androidActions = new AndroidActions(driver);
//
//        //androidActions.environmentChange();
//        loginPage.loginAccount(input.get("profileName"));
//        loginPage.enterLoginPin(Integer.parseInt(input.get("loginPin")));
//        loginPage.loginAccount(input.get("profileName"));
//
//        accountMenuActions.clickAccountMenuActionsButtn();
//        transferPage.clickTransferButton();
//        transferPage.selectExistingAccount(input.get("accountName"));
//
//        transferPage.clickSchedule();
//        transferPage.resetScheduleType();
//
//        try {
//            //transferPage.selectTransferSchedule("Never");
//            transferPage.transferMoney(input.get("amount"),input.get("ref"));
//            transferPage.clickTransfer();
//            transferPage.clickConfrim();
//            try {
//                Assert.assertEquals(transferPage.getTransferStatus(),"Transfer success");
//                log.info("Transfer status: {}",transferPage.getTransferStatus());
//            } catch (AssertionError e) {
//                log.warn("Failed to do transfer transaction");
//                throw e;  // Let TestNG fail the test
//            }
//        } catch (Exception e) {
//            log.error("Exception occurred during adding profile: ", e);
//            Assert.fail("Test failed due to exception: " + e.getMessage());
//        }
//        transferPage.clickBack();
//
//    }

    @Test(dataProvider = "getMultipleDataSet",priority = 1)
    public void scheduleTransferOnceOff(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        TransferPage transferPage = new TransferPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        transferPage.clickTransferButton();
        transferPage.selectExistingAccount(input.get("accountName"));

        transferPage.clickSchedule();
        //transferPage.resetScheduleType();
        //transferPage.clickSchedule();

        try {
            String scheduleType = input.get("scheduleType");  // "once-off"
            int daysToAdd = Integer.parseInt(input.get("daysToAdd"));  // 3
            double amount = Double.parseDouble(input.get("amount"));
            String formattedAmount = String.format("R%.2f", amount);
            String futureDate = AppiumUtils.getFutureDate(daysToAdd);
            String scheduleTypeLower = input.get("scheduleTypeOnceOff").toLowerCase();

            transferPage.chooseTransferSchedule(input.get("scheduleTypeOnceOff"),Integer.parseInt(input.get("daysToAdd")),input.get("ref"),input.get("amount"));

            try {
                String expectedTxt = formattedAmount + " " + scheduleTypeLower + " on " + futureDate;
                log.info("Assertion expectation: {}",expectedTxt);

                Assert.assertEquals(driver.findElement(By.id("za.co.neolabs.bankzero:id/product_type")).getText(),expectedTxt);

            } catch (AssertionError e) {
                log.warn("Failed to do schedule transfer");
                throw e;  // Let TestNG fail the test
            }
        } catch (Exception e) {
            log.error("Exception occurred during schedule transfer: ", e);
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }finally {
            transferPage.clickBack();
        }
    }

    @Test(dataProvider = "getMultipleDataSet",priority = 2)
    public void scheduleTransferWeekly(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        TransferPage transferPage = new TransferPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        transferPage.clickTransferButton();
        transferPage.selectExistingAccount(input.get("accountName"));

        transferPage.clickSchedule();

        try {
            String scheduleType = input.get("scheduleTypeWeekly");  // "once-off"
            int daysToAdd = Integer.parseInt(input.get("daysToAdd"));  // 3
            double amount = Double.parseDouble(input.get("amount"));
            String formattedAmount = String.format("R%.2f", amount);
            String toDate = AppiumUtils.getFutureDate(daysToAdd + 7);
            String scheduleTypeLower = input.get("scheduleTypeWeekly").toLowerCase();

            transferPage.chooseTransferSchedule(scheduleType,Integer.parseInt(input.get("daysToAdd")),input.get("ref"),input.get("amount"));

            try {
                String expectedTxt = formattedAmount + " " + scheduleTypeLower + " on Monday till " + toDate;
                log.info("Assertion expectation: {}",expectedTxt);

                Assert.assertEquals(driver.findElement(By.id("za.co.neolabs.bankzero:id/product_type")).getText(),expectedTxt);

            } catch (AssertionError e) {
                log.warn("Failed to do schedule transfer");
                throw e;  // Let TestNG fail the test
            }
        } catch (Exception e) {
            log.error("Exception occurred during schedule transfer: ", e);
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }finally {
            transferPage.clickBack();
        }

    }

    @Test(dataProvider = "getMultipleDataSet",priority = 3)
    public void scheduleTransferMonthly(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        TransferPage transferPage = new TransferPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        transferPage.clickTransferButton();
        transferPage.selectExistingAccount(input.get("accountName"));

        transferPage.clickSchedule();

        try {
            String scheduleType = input.get("scheduleTypeMonthly");
            int daysToAdd = Integer.parseInt(input.get("daysToAdd"));
            double amount = Double.parseDouble(input.get("amount"));
            String formattedAmount = String.format("R%.2f", amount);
            String monthlyToDate = AppiumUtils.getFutureDate(daysToAdd + 35);
            String scheduleTypeLower = scheduleType.toLowerCase();

            transferPage.chooseTransferSchedule(scheduleType,Integer.parseInt(input.get("daysToAdd")),input.get("ref"),input.get("amount"));

            try {
                String expectedTxt = formattedAmount + " " + scheduleTypeLower + " on 2nd till " + monthlyToDate;
                log.info("Assertion expectation: {}",expectedTxt);

                Assert.assertEquals(driver.findElement(By.id("za.co.neolabs.bankzero:id/product_type")).getText(),expectedTxt);

            } catch (AssertionError e) {
                log.warn("Failed to do schedule transfer");
                throw e;  // Let TestNG fail the test
            }
        } catch (Exception e) {
            log.error("Exception occurred during schedule transfer: ", e);
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }finally {
            transferPage.clickBack();
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
            HomePage homePage = new HomePage(driver);
            homePage.clickLogoutButtn();

        } catch (Exception e) {
            log.error("Cleanup failed: ", e);
        }
    }

}
