package tests.Buy.BuyElectricity;

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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class NewElectricityAccTest extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(NewElectricityAccTest.class);
        private LoginPage loginPage;
        private AccountMenuActions accountMenuActions;
        private BuyElectricityPage buyPage;

        @BeforeMethod
        public void preSetUp() {
            loginPage = new LoginPage(driver);
            accountMenuActions = new AccountMenuActions(driver);
            buyPage = new BuyElectricityPage(driver);

        }

    @Test(dataProvider = "getMultipleDataSet")
    public void newElectricityAccTest(HashMap<String, String> input) throws InterruptedException, IOException {

        validateInput(input,
                    "name", "provider", "tokenNo", "meterNo", "amount", "ref"
        );

        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
        properties.load(fis);

        String amount = input.get("amount");
        String ref = input.get("ref");
        String appLogin = properties.getProperty("appLogin");
        String profileName = properties.getProperty("profileName");

        loginPage.loginWithRetry(profileName,appLogin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        buyPage.clickBuyButton();
        buyPage.addAccButton();
        buyPage.addElectricityItem(input.get("name"),input.get("provider"),input.get("tokenNo"),input.get("meterNo"));
        buyPage.buyElectricity(input.get("amount"),input.get("ref"));
        buyPage.clickConfrimButton();
        Thread.sleep(6000);

        try {
            String status = buyPage.getTransactionStatus();

            try {
                Assert.assertEquals(status, "Success");
                log.info("Transactional Status: {}",status);
                attachScreenshot(driver,"Electricity Purchase Success");
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

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//buyElectricityData.json");
        return new Object[][]{{data.get(0)},{data.get(1)}};
    }

    @AfterMethod
    public void cleanUp() {
        try {
            HomePage homePage = new HomePage(driver);
            BuyElectricityPage buyPage = new BuyElectricityPage(driver);

            buyPage.clickFinishButton();
            homePage.clickLogoutButtn();
        } catch (Exception e) {
            log.error("Cleanup failed: ", e);
        }
    }
}
