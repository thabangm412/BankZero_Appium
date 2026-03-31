package tests.Pay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.card.MyCardPage;
import pageObjects.app.accountsActionMenu.pay.QuickPayPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class NegativePaymentTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(PaymentTests.class);
    private LoginPage loginPage;
    private HomePage homePage;
    private AccountMenuActions accountMenuActions;
    private QuickPayPage quickPayPage;
    private MyCardPage myCardPage;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        accountMenuActions = new AccountMenuActions(driver);
        quickPayPage = new QuickPayPage(driver);
        myCardPage = new MyCardPage(driver);

        log.debug("Page objects and androidActions initialized");
    }

    @Test(dataProvider = "getMultipleDataSet",priority = 0)
    public void AddRecipientTestWithPoP(HashMap<String, String> input) throws InterruptedException {

        validateInput(input,
                "profileName", "loginPin",
                "recipientName3", "group", "bank", "account", "CBV_Rejected_Acc",
                "popEmail", "popPhone"
        );

        //androidActions.environmentChange();
        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        quickPayPage.clickPayButtn();
        quickPayPage.clickAddRecipientButton();

        quickPayPage.addRecipientDetails(input.get("recipientName3"),input.get("group"),input.get("bank"),input.get("account"),input.get("CBV_Rejected_Acc"));
        //quickPayPage.addPoP(input.get("popEmail"),input.get("popPhone"));
        quickPayPage.clickAddButton();
        try {

            String expectedTxt =  quickPayPage.getErrorMessage();
            log.info("Assertion expectation: {}",expectedTxt);
            Assert.assertEquals(expectedTxt,"Account number and branch combination could not be validated. Please check and rectify.");
            attachScreenshot(driver, "Recipient_Add_Failed");

        } catch (AssertionError e) {
            log.warn("Failed to add payment recipient");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;  // Let TestNG fail the test
        }finally {
            quickPayPage.clickBack();
            quickPayPage.clickBack();
            homePage.clickLogoutButtn();
        }
    }
    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//payData.json");
        return new Object[][]{{data.get(0)}};
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
