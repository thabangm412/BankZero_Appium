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
import pageObjects.app.accountsActionMenu.buy.BuyElectricityPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.DriverManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class NewAirtimeAccTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(NewAirtimeAccTests.class);

    private LoginPage loginPage;
    private AccountMenuActions accountMenuActions;
    private BuyAirtimePage buyAirtimePage;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        buyAirtimePage = new BuyAirtimePage(DriverManager.driver);

    }


    @Test(dataProvider = "getMultipleDataSet")
    public void newAirtimePurchase(HashMap<String, String> input) throws IOException, InterruptedException {

         validateInput(input,
                 "AirtimeName", "provider", "recipientNo", "amount", "ref"
          );

        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
        properties.load(fis);

        String appLogin = properties.getProperty("appLogin");
        String profileName = properties.getProperty("profileName");

        loginPage.loginWithRetry(profileName,appLogin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        buyAirtimePage.clickBuyButton();
        buyAirtimePage.addAccButton();
        buyAirtimePage.addAirtimeItem(input.get("AirtimeName"),input.get("provider"),input.get("recipientNo"));
        buyAirtimePage.buyAirtime(input.get("amount"),input.get("ref"));
        attachScreenshot(DriverManager.driver, "AirtimePurchaseDetails");
        buyAirtimePage.clickConfirmButton();


        try {
            String status = buyAirtimePage.getTransactionStatus();

            try {
                Assert.assertEquals(status, "Success");
                attachScreenshot(DriverManager.driver, "AirtimePurchaseSuccess");
                log.info("Transactional Status: {}",status);
            } catch (AssertionError e) {
                log.warn("Transaction failed with status: {}", status);
                throw e;  // Let TestNG fail the test
            }
        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
            log.error("Exception occurred during transaction handling: ", e);
        }

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
            BuyElectricityPage buyPage = new BuyElectricityPage(DriverManager.driver);

            buyPage.clickFinishButton();
            homePage.clickLogoutButtn();
        } catch (Exception e) {
            log.error("Cleanup failed: ", e);
        }
    }
}
