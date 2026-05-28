package tests.Buy.BuyAirtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.buy.BuyAirtimePage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.DriverManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class ExistingAirtimeAccTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(ExistingAirtimeAccTests.class);

    private LoginPage loginPage;
    private AccountMenuActions accountMenuActions;
    private BuyAirtimePage buyAirtimePage;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        buyAirtimePage = new BuyAirtimePage(DriverManager.driver);

    }

    @Test(dataProvider = "getMultipleDataSet", priority = 0)
    public void existingAirtimeAccTest(HashMap<String, String> input) throws IOException {
        validateInput(input,
                    "AirtimeName", "amount", "ref"
        );

        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
        properties.load(fis);

        String appLogin = properties.getProperty("appLogin");
        String profileName = properties.getProperty("profileName");

        loginPage.loginWithRetry(profileName,appLogin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        buyAirtimePage.clickBuyButton();
        buyAirtimePage.getExistingProfile(input.get("AirtimeName"));
        attachScreenshot(DriverManager.driver,"Existing Airtime Profile Retrieved");
        buyAirtimePage.clickRedo();
        buyAirtimePage.clickHomeBuyButton();
        attachScreenshot(DriverManager.driver,"Airtime Purchase Page");
        buyAirtimePage.clickConfirmButton();

        try {
            String status = buyAirtimePage.getTransactionStatus();

            try {
                Assert.assertEquals(status, "Success");
                log.info("Transactional Status: {}",status);
                attachScreenshot(DriverManager.driver,"Airtime Purchase Success");
            } catch (AssertionError e) {
                log.warn("Transaction failed with status: {}", status);
                throw e;  // Let TestNG fail the test
            }
        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
            log.error("Exception occurred during transaction handling: ", e);
        }
        buyAirtimePage.clickFinishButton();
    }

    @Test(dataProvider = "getMultipleDataSet", priority = 1)
    public void deleteExistingRecipient(HashMap<String, String> input)  throws InterruptedException, IOException{

       validateInput(input,
                "AirtimeName", "amount", "ref"
        );
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
        properties.load(fis);

        String appLogin = properties.getProperty("appLogin");
        String profileName = properties.getProperty("profileName");

        loginPage.loginWithRetry(profileName,appLogin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        buyAirtimePage.clickBuyButton();
        buyAirtimePage.getExistingProfile(input.get("AirtimeName"));
        attachScreenshot(DriverManager.driver,"Existing Airtime Profile Retrieved for Deletion");
        buyAirtimePage.clickEditButton();
        buyAirtimePage.clickDeleteButton();

        try {
            Assert.assertTrue(buyAirtimePage.isRecipientDeleted(input.get("AirtimeName")));
            log.info("Recipient deletion confirmed: {}",input.get("AirtimeName"));
            attachScreenshot(DriverManager.driver,"Airtime Recipient Deleted");
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
