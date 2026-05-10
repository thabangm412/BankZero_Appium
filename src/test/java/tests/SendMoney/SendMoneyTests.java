package tests.SendMoney;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.pay.QuickPayPage;
import pageObjects.app.accountsActionMenu.sedMoney.SendMoneyPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SendMoneyTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(SendMoneyTests.class);

    private LoginPage loginPage;
    private QuickPayPage quickPayPage;
    private HomePage homePage;
    private AccountMenuActions accountMenuActions;
    private AndroidActions androidActions;

    private SendMoneyPage sendMoneyPage;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        accountMenuActions = new AccountMenuActions(driver);
        sendMoneyPage = new SendMoneyPage(driver);
        androidActions = new AndroidActions(driver);

        log.debug("Page objects and androidActions initialized");
    }

    @Test(dataProvider = "getMultipleDataSet", priority = 0)
    public void addNewRecipient(HashMap<String, String> input)
    {
        validateInput(input,
                "profileName", "loginPin",
                "recipientName", "recipientPhone"
        );

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        sendMoneyPage.clickSendMoneyButton();
        sendMoneyPage.clickAddRecipientButton();
        sendMoneyPage.addRecipient(input.get("recipientName"),input.get("recipientPhone"));

        try {
            String addedPhone = sendMoneyPage.getAddedProfile();

            try {
                Assert.assertEquals(addedPhone, input.get("recipientPhone"));
                attachScreenshot(driver,"ProfilePhoneAdded.png");
                log.info("Profile phone added: {}",input.get("recipientPhone"));
            } catch (AssertionError e) {
                log.warn("Failed to add profile phone: {}", input.get("recipientPhone"));
                throw e;  // Let TestNG fail the test
            }
        } catch (Exception e) {
            log.error("Exception occurred during adding profile: ", e);
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }

    }

    @Test(dataProvider = "getMultipleDataSet", priority = 1)
    public void sendMoneyToNewRecipient(HashMap<String, String> input)
    {
        validateInput(input,
                "amount", "ref"
        );


        sendMoneyPage.sendMoney(input.get("amount"),input.get("ref"));
        attachScreenshot(driver,"SendMoneyDetails.png");
        sendMoneyPage.clickSend();
        sendMoneyPage.clickConfirm();
        try {
            try {
                Assert.assertEquals(sendMoneyPage.getStatus(), "Thank you");
                attachScreenshot(driver,"TransactionSuccessful.png");
                log.info("Transactional successful");
            } catch (AssertionError e) {
                log.warn("Transaction failed with status: {}", sendMoneyPage.getStatus());
                throw e;
            }
        } catch (Exception e) {
            log.error("Exception occurred during transaction handling: ", e);
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }finally {

            sendMoneyPage.clickFinish();
            homePage.clickLogoutButtn();
        }
    }

    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//sendMoneyData.json");
        return new Object[][]{{data.getFirst()}};
    }
}
