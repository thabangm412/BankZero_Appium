package tests.Buy.BuyData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.buy.BuyDataPage;
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

public class NewDataAccTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(NewDataAccTests.class);
    private LoginPage loginPage;
    private AccountMenuActions accountMenuActions;
    private BuyDataPage buyDataPage;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        buyDataPage = new BuyDataPage(DriverManager.driver);

    }


    @Test(dataProvider = "getMultipleDataSet")
    public void newDataAccTest(HashMap<String, String> input) throws IOException {
        validateInput(input,
                "DataName", "provider", "productData", "recipientNo", "amount", "ref"
        );
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
        properties.load(fis);

        String appLogin = properties.getProperty("appLogin");
        String profileName = properties.getProperty("profileName");

        loginPage.loginWithRetry(profileName,appLogin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        buyDataPage.clickBuyButton();
        buyDataPage.addAccButton();
        buyDataPage.addDataItem(input.get("DataName"),input.get("provider"),input.get("productData"),input.get("recipientNo"));
        buyDataPage.clickHomeBuyButton();
        attachScreenshot(DriverManager.driver,"Data Purchase Page");
        buyDataPage.clickConfirmButton();

        try {
            String status = buyDataPage.getTransactionStatus();

            try {
                Assert.assertEquals(status, "Success");
                log.info("Transactional Status: {}",status);
                attachScreenshot(DriverManager.driver,"Data Purchase Success");
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
