package tests.AddAccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.addAccount.AddAccountPage;
import pageObjects.app.addAccount.NewAccPage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ThirtyTwoDayNoticeAccountTest extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(ThirtyTwoDayNoticeAccountTest.class);

    @Test(dataProvider = "getMultipleDataSet")
    public void newThirtyTwoDayAccountRegistration(HashMap<String, String> input) throws IOException {
        LoginPage loginPage = new LoginPage(driver);
        AndroidActions androidActions = new AndroidActions(driver);
        HomePage homePage = new HomePage(driver);
        AddAccountPage addAccountPage = new AddAccountPage(driver);
        NewAccPage newAccPage = new NewAccPage(driver);
//        Properties properties = new Properties();
//        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
//        properties.load(fis);

        String accName = input.get("accName");
        String amount = input.get("amount");
        String goalAmount = input.get("goalAmount");
        String accType = input.get("accType");
        String appLogin = input.get("appLogin");
        String profileName = input.get("profileName");

        loginPage.loginWithRetry(profileName,appLogin,2);

        addAccountPage.clickAddAccButtn();
        addAccountPage.clickNewAccount();

        newAccPage.selectAccountType(accType);
        newAccPage.clickAdd();

        newAccPage.enterSavingsDetails(accName,goalAmount,amount);
        newAccPage.clickCheckbox();

        try {
            String status = newAccPage.getAccStatus();

            try {
                Assert.assertEquals(status, "Account successfully added.  You can access this account from your canvas.");
                log.info("Account registration Status: {}",status);
            } catch (AssertionError e) {
                log.warn("Account registration failed with status: {}", status);
                throw e;  // Let TestNG fail the test
            }
        } catch (Exception e) {
            log.error("Exception occurred during transaction handling: ", e);
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }

    }

    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//newAccData.json");
        return new Object[][]{{data.get(2)} };
    }

    @AfterMethod
    public void cleanUp() {
        try {
            HomePage homePage = new HomePage(driver);
            NewAccPage newAccPage = new NewAccPage(driver);

            newAccPage.clickFinish();
            homePage.clickLogoutButtn();
        } catch (Exception e) {
            log.error("Cleanup failed: ", e);
        }
    }
}
