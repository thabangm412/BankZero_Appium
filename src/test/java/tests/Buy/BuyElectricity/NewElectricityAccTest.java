package tests.Buy.BuyElectricity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
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

    @Test(dataProvider = "getMultipleDataSet")
    public void newElectricityAccTest(HashMap<String, String> input) throws InterruptedException, IOException {

        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        BuyElectricityPage buyPage = new BuyElectricityPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
        properties.load(fis);

        String name = input.get("name");
        String provider = input.get("provider");
        String tokenNo = input.get("tokenNo");
        String meterNo = input.get("meterNo");
        String amount = input.get("amount");
        String ref = input.get("ref");
        String appLogin = properties.getProperty("appLogin");
        String profileName = properties.getProperty("profileName");

        loginPage.loginWithRetry(profileName,appLogin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        buyPage.clickBuyButton();
        buyPage.addAccButton();
        buyPage.addElectricityItem(name,provider,tokenNo,meterNo);
        buyPage.buyElectricity(amount,ref);
        buyPage.clickConfrimButton();
        Thread.sleep(6000);

        /// Need to add assert here
        try {
            String status = buyPage.getTransactionStatus();

            try {
                Assert.assertEquals(status, "Success");
                log.info("Transactional Status: {}",status);
            } catch (AssertionError e) {
                log.warn("Transaction failed with status: {}", status);
                throw e;  // Let TestNG fail the test
            }
        } catch (Exception e) {
            log.error("Exception occurred during transaction handling: ", e);
            Assert.fail("Test failed due to exception: " + e.getMessage());
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
