package tests.Buy.BuySMS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.buy.BuyAirtimePage;
import pageObjects.app.accountsActionMenu.buy.BuySMSPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.DriverManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class ExistingSMSTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(ExistingSMSTests.class);

    private LoginPage loginPage;
    private AccountMenuActions accountMenuActions;
    private BuySMSPage buySMSPage;
        private BuyAirtimePage buyAirtimePage;


    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        buySMSPage = new BuySMSPage(DriverManager.driver);
        buyAirtimePage = new BuyAirtimePage(DriverManager.driver);


    }

    @Test(dataProvider = "getMultipleDataSet", priority = 0)
    public void existingSMSAccTest(HashMap<String, String> input) throws IOException {
        validateInput(input,
                "SMSName", "amount", "ref"
        );
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
        properties.load(fis);

        String appLogin = properties.getProperty("appLogin");
        String profileName = properties.getProperty("profileName");

        loginPage.loginWithRetry(profileName,appLogin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        buySMSPage.clickBuyButton();
        buySMSPage.getExistingProfile(input.get("SMSName"));
        attachScreenshot(DriverManager.driver,"Existing SMS Profile Retrieved");
        buySMSPage.clickRedo();
        buySMSPage.clickHomeBuyButton();
        attachScreenshot(DriverManager.driver,"SMS Purchase Page");
        buySMSPage.clickConfrimButton();

        try {
            String status = buySMSPage.getTransactionStatus();

            try {
                Assert.assertEquals(status, "Success");
                log.info("Transactional Status: {}",status);
                attachScreenshot(DriverManager.driver,"SMS Purchase Success");
            } catch (AssertionError e) {
                log.warn("Transaction failed with status: {}", status);
                throw e;  // Let TestNG fail the test
            }
        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
            log.error("Exception occurred during transaction handling: ", e);
        }
        buySMSPage.clickFinishButton();
    }

    @Test(dataProvider = "getMultipleDataSet", priority = 1)
    public void deleteSmsExistingRecipient(HashMap<String, String> input)  throws InterruptedException, IOException{

        validateInput(input,
                "SMSName", "amount", "ref"
        );
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
        properties.load(fis);

        String appLogin = properties.getProperty("appLogin");
        String profileName = properties.getProperty("profileName");

        loginPage.loginWithRetry(profileName,appLogin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        buySMSPage.clickBuyButton();
        buySMSPage.getExistingProfile(input.get("SMSName"));
        attachScreenshot(DriverManager.driver,"Existing SMS Profile Retrieved for Deletion");
        buySMSPage.clickEditButton();
        buySMSPage.clickDeleteButton();

        try {
            Assert.assertTrue(buySMSPage.isRecipientDeleted(input.get("SMSName")));
            log.info("Recipient deletion confirmed: {}",input.get("SMSName"));
            attachScreenshot(DriverManager.driver,"SMSName Recipient Deleted");
        } catch (Exception| AssertionError e) {
            Assert.fail("Test failed");
            log.warn("Test failed due to: {}",e);
            throw new RuntimeException(e);
        }
        DriverManager.driver.navigate().back();
        buyAirtimePage.clickBack();
    }

    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//data+sms+bundle_BuyData.json");
        return new Object[][]{{data.get(0)},{data.get(1)},{data.get(2)},{data.get(3)}};
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
