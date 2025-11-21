package tests.Pay;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.pay.QuickPayPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;
import utils.AppiumUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SchedulePaymentTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(SchedulePaymentTests.class);

    @Test(dataProvider = "getMultipleDataSet", priority = 0)
    public void scheduleOnceOffPayment(HashMap<String, String> input) throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        QuickPayPage quickPayPage = new QuickPayPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        int daysToAdd = Integer.parseInt(input.get("daysToAdd"));  // 3
        double amount = Double.parseDouble(input.get("amount"));
        String profileName = input.get("profileName");
        String appLogin = input.get("loginPin");
        String formattedAmount = String.format("R%.2f", amount);
        String futureDate = AppiumUtils.getFutureDate(daysToAdd);
        String scheduleTypeLower = input.get("scheduleType").toLowerCase();

        //androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("appLogin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(input.get("recipientName"));
        quickPayPage.editProfile();
        quickPayPage.choosePaymentSchedule("Once-off", daysToAdd, input.get("ref"), input.get("amount"));

        try {
            String expectedTxt = formattedAmount + " " + scheduleTypeLower + " on " + futureDate;
            log.info("Assertion expectation: {}", expectedTxt);

            Assert.assertEquals(driver.findElement(By.id("za.co.neolabs.bankzero:id/product_type")).getText(), expectedTxt);

        } catch (AssertionError | Exception e) {
            log.warn("Failed to do schedule transfer");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        } finally {
            quickPayPage.clickBack();
            homePage.clickLogoutButtn();
        }
    }

    @Test(dataProvider = "getMultipleDataSet", priority = 1)
    public void scheduleWeeklyPayment(HashMap<String, String> input) throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        QuickPayPage quickPayPage = new QuickPayPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        int daysToAdd = Integer.parseInt(input.get("daysToAdd"));  // 3
        double amount = Double.parseDouble(input.get("amount"));
        String profileName = input.get("profileName");
        String appLogin = input.get("loginPin");
        String formattedAmount = String.format("R%.2f", amount);
        String futureDate = AppiumUtils.getFutureDate(daysToAdd);
        String scheduleTypeLower = input.get("scheduleType").toLowerCase();

        //androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("appLogin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(input.get("recipientName"));
        quickPayPage.editProfile();
        quickPayPage.choosePaymentSchedule("Weekly", daysToAdd, input.get("ref"), input.get("amount"));

        try {
            String toDate = AppiumUtils.getFutureDateFormatted(daysToAdd + 7);
            String expectedTxt = formattedAmount + " " + scheduleTypeLower + " on Monday till " + toDate;
            log.info("Assertion expectation: {}",expectedTxt);

            Assert.assertEquals(driver.findElement(By.id("za.co.neolabs.bankzero:id/product_type")).getText(), expectedTxt);

        } catch (AssertionError | Exception e) {
            log.warn("Failed to do schedule transfer");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        } finally {
            quickPayPage.clickBack();
            homePage.clickLogoutButtn();
        }
    }

    @Test(dataProvider = "getMultipleDataSet", priority = 4)
    public void scheduleMonthlyPayment(HashMap<String, String> input) throws InterruptedException {

        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        QuickPayPage quickPayPage = new QuickPayPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        String scheduleType = "Monthly";
        int daysToAdd = Integer.parseInt(input.get("daysToAdd"));
        double amount = Double.parseDouble(input.get("amount"));
        String profileName = input.get("profileName");
        String appLogin = input.get("loginPin");
        String formattedAmount = String.format("R%.2f", amount);
        String monthlyToDate = AppiumUtils.getFutureDate(daysToAdd + 35);
        String scheduleTypeLower = scheduleType.toLowerCase();

        //androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("appLogin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(input.get("recipientName"));
        quickPayPage.editProfile();
        quickPayPage.choosePaymentSchedule(scheduleType, daysToAdd, input.get("ref"), input.get("amount"));

        try {

            String expectedTxt = formattedAmount + " " + scheduleTypeLower + " on 2nd till " + monthlyToDate;
            log.info("Assertion expectation: {}",expectedTxt);

            Assert.assertEquals(driver.findElement(By.id("za.co.neolabs.bankzero:id/product_type")).getText(), expectedTxt);

        } catch (AssertionError | Exception e) {
            log.warn("Failed to do schedule transfer");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        } finally {
            quickPayPage.clickBack();
            homePage.clickLogoutButtn();
        }
    }

    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//payData.json");
        return new Object[][]{{data.getFirst()}};
    }


}
