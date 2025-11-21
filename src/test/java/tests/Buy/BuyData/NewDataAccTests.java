package tests.Buy.BuyData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.buy.BuyDataPage;
import pageObjects.app.accountsActionMenu.buy.BuyElectricityPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class NewDataAccTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(NewDataAccTests.class);

    @Test(dataProvider = "getMultipleDataSet")
    public void newDataAccTest(HashMap<String, String> input) throws IOException {
        LoginPage loginPage = new LoginPage(driver);
        BuyDataPage buyDataPage = new BuyDataPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
        properties.load(fis);

        String name = input.get("DataName");
        String provider = input.get("provider");
        String dataProduct = input.get("productData");
        String recipient = input.get("recipientNo");
        String amount = input.get("amount");
        String ref = input.get("ref");
        String appLogin = properties.getProperty("appLogin");
        String profileName = properties.getProperty("profileName");

        loginPage.loginWithRetry(profileName,appLogin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        buyDataPage.clickBuyButton();
        buyDataPage.addAccButton();
        buyDataPage.addDataItem(name,provider,dataProduct,recipient);
        buyDataPage.clickHomeBuyButton();
        buyDataPage.clickConfirmButton();

        /// Need to add assert here
        try {
            String status = buyDataPage.getTransactionStatus();

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

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//data+sms+bundle_BuyData.json");
        return new Object[][]{{data.get(3)}};
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
