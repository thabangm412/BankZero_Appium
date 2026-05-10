package tests.Buy.BuySMS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.buy.BuySMSPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class NewSMSAccTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(NewSMSAccTests.class);

    private LoginPage loginPage;
    private AccountMenuActions accountMenuActions;
    private BuySMSPage buySMSPage;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(driver);
        accountMenuActions = new AccountMenuActions(driver);
        buySMSPage = new BuySMSPage(driver);

    }

    @Test(dataProvider = "getMultipleDataSet")
    public void newSMSAccTest(HashMap<String, String> input) throws IOException {
        validateInput(input,
                "SMSName", "provider", "productSMS", "recipientNo", "amount", "ref"
        );
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
        properties.load(fis);

     ;
        String appLogin = properties.getProperty("appLogin");
        String profileName = properties.getProperty("profileName");

        loginPage.loginWithRetry(profileName,appLogin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        buySMSPage.clickBuyButton();
        buySMSPage.addAccButton();
        buySMSPage.addSMSItem(input.get("SMSName"),input.get("provider"),input.get("productSMS"),input.get("recipientNo"));
        buySMSPage.clickHomeBuyButton();
        attachScreenshot(driver,"SMS Purchase Page");
        buySMSPage.clickConfirmButton();

        try {
            String status = buySMSPage.getTransactionStatus();

            try {
                Assert.assertEquals(status, "Success");
                log.info("Transactional Status: {}",status);
                attachScreenshot(driver,"SMS Purchase Success");
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
            HomePage homePage = new HomePage(driver);
            BuySMSPage buySMSPage = new BuySMSPage(driver);

            buySMSPage.clickFinishButton();
            homePage.clickLogoutButtn();
        } catch (Exception e) {
            log.error("Cleanup failed: ", e);
        }
    }
}
