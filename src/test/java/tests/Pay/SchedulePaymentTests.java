package tests.Pay;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
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
    private LoginPage loginPage;
    private HomePage homePage;
    private AccountMenuActions accountMenuActions;
    private QuickPayPage quickPayPage;
    private AndroidActions androidActions;

    @BeforeMethod
    public void setup() {
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        accountMenuActions = new AccountMenuActions(driver);
        quickPayPage = new QuickPayPage(driver);
        androidActions = new AndroidActions(driver);
    }

    @Test(dataProvider = "getMultipleDataSet", priority = 0)
    public void scheduleOnceOffPayment(HashMap<String, String> input) throws InterruptedException {

        validateInput(input, "daysToAdd", "amount", "profileName", "loginPin", "scheduleType", "recipientName1", "ref");


        int daysToAdd = Integer.parseInt(input.get("daysToAdd"));  // 3
        double amount = Double.parseDouble(input.get("amount"));
        String formattedAmount = String.format("R%.2f", amount);
        String futureDate = AppiumUtils.getFutureDate(daysToAdd);
        String scheduleTypeLower = ("Once-off").toLowerCase();

        //androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(input.get("recipientName1"));
        quickPayPage.editProfile();
        quickPayPage.choosePaymentSchedule("Once-off", daysToAdd, input.get("ref"), input.get("amount"));

        try {
            String expectedTxt = formattedAmount + " " + scheduleTypeLower + " on " + futureDate;
            log.info("Assertion expectation: {}", expectedTxt);

            Assert.assertEquals(driver.findElement(By.id("za.co.neolabs.bankzero:id/product_type")).getText(), expectedTxt);
            attachScreenshot(driver, "Schedule Once-off Payment - " + name);

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

        validateInput(input, "daysToAdd", "amount", "profileName", "loginPin", "scheduleType", "recipientName1", "ref");

        int daysToAdd = Integer.parseInt(input.get("daysToAdd"));  // 3
        double amount = Double.parseDouble(input.get("amount"));
        String formattedAmount = String.format("R%.2f", amount);
        String futureDate = AppiumUtils.getFutureDate(daysToAdd);
        String scheduleTypeLower = ("Weekly").toLowerCase();

        //androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(input.get("recipientName1"));
        quickPayPage.editProfile();
        quickPayPage.choosePaymentSchedule("Weekly", daysToAdd, input.get("ref"), input.get("amount"));

        try {
            //String toDate = AppiumUtils.getFutureDateFormatted(daysToAdd + 7);
            String toDate = AppiumUtils.getFutureDate(daysToAdd + 7);
            String expectedTxt = formattedAmount + " " + scheduleTypeLower + " on Monday till " + toDate;
            log.info("Future date calculated for assertion: {}", toDate);
            log.info("Assertion expectation: {}",expectedTxt);

            Assert.assertEquals(driver.findElement(By.id("za.co.neolabs.bankzero:id/product_type")).getText(), expectedTxt);
            attachScreenshot(driver, "Schedule Weekly Payment - " + name);

        } catch (AssertionError | Exception e) {
            log.warn("Failed to do schedule transfer");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        } finally {
            quickPayPage.clickBack();
            homePage.clickLogoutButtn();
        }
    }

    @Test(dataProvider = "getMultipleDataSet", priority = 2)
    public void scheduleMonthlyPayment(HashMap<String, String> input) throws InterruptedException {
        validateInput(input, "daysToAdd", "amount", "profileName", "loginPin", "recipientName1", "ref");

        String scheduleType = "Monthly";
        int daysToAdd = Integer.parseInt(input.get("daysToAdd"));
        double amount = Double.parseDouble(input.get("amount"));
        String formattedAmount = String.format("R%.2f", amount);
        String monthlyToDate = AppiumUtils.getFutureDate(daysToAdd + 35);
        String scheduleTypeLower = scheduleType.toLowerCase();

        //androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.getExistingRecipient(input.get("recipientName1"));
        quickPayPage.editProfile();
        quickPayPage.choosePaymentSchedule(scheduleType, daysToAdd, input.get("ref"), input.get("amount"));

        try {
            String expectedTxt = formattedAmount + " " + scheduleTypeLower + " on 2nd till " + monthlyToDate;
            log.info("Assertion expectation: {}",expectedTxt);

            Assert.assertEquals(driver.findElement(By.id("za.co.neolabs.bankzero:id/product_type")).getText(), expectedTxt);
            attachScreenshot(driver, "Schedule Monthly Payment - " + name);

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

    private void validateInput(HashMap<String, String> input, String... required) {
        if (input == null) throw new IllegalArgumentException("Input map is null");
        StringBuilder missing = new StringBuilder();
        for (String k : required) {
            if (input.get(k) == null || input.get(k).trim().isEmpty()) {
                if (missing.length() > 0) missing.append(", ");
                missing.append(k);
            }
        }
        if (missing.length() > 0) {
            log.error("Missing required keys: {}", missing.toString());
            throw new IllegalArgumentException("Missing required keys: " + missing.toString());
        }
    }


}
