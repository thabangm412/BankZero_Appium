package tests.Buy.BuyBundles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.buy.BuyBundlesPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class ExistingBundlesAccTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(ExistingBundlesAccTests.class);


    @Test(dataProvider = "getSingleDataSet")
    public void existingBundlesAccTest(HashMap<String, String> input) throws IOException {
        LoginPage loginPage = new LoginPage(driver);
        BuyBundlesPage buyBundlesPage = new BuyBundlesPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
        properties.load(fis);

        String name = input.get("name");
        String amount = input.get("amount");
        String ref = input.get("ref");
        String appLogin = properties.getProperty("appLogin");
        String profileName = properties.getProperty("profileName");

        loginPage.loginAccount(profileName);
        loginPage.enterLoginPin(Integer.parseInt(appLogin));
        loginPage.loginAccount(profileName);

        accountMenuActions.clickAccountMenuActionsButtn();
        buyBundlesPage.clickBuyButton();
        buyBundlesPage.buyBundlesAgain(name,amount,ref);
        buyBundlesPage.clickConfrimButton();

        try {
            String status = buyBundlesPage.getTransactionStatus();

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
    public Object[] [] getSingleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//data+sms+bundle_BuyData.json");
        return new Object[][]{{data.get(0)},{data.get(1)}};
    }
    @AfterMethod
    public void cleanUp() {
        try {
            HomePage homePage = new HomePage(driver);
            BuyBundlesPage buyBundlesPage = new BuyBundlesPage(driver);

            buyBundlesPage.clickFinishButton();
            homePage.clickLogoutButtn();
        } catch (Exception e) {
            log.error("Cleanup failed: ", e);
        }
    }
}
