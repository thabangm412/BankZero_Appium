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

public class ExistingElectricityAccTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(ExistingElectricityAccTests.class);

    @Test(dataProvider = "getSingleDataSet")
    public void existingElectricityAccTest(HashMap<String, String> input) throws InterruptedException, IOException {

        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        BuyElectricityPage buyPage = new BuyElectricityPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
        properties.load(fis);

        String name = input.get("name");
        String amount = input.get("amount");
        String ref = input.get("ref");
        String appLogin = properties.getProperty("appLogin");
        String profileName = properties.getProperty("profileName");

        loginPage.loginWithRetry(profileName,appLogin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        buyPage.clickBuyButton();
        buyPage.buyElectricityAgain(name,amount,ref);
        buyPage.clickConfrimButton();
        Thread.sleep(5000);

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

        buyPage.clickFinishButton();
    }

    @Test(dataProvider = "getSingleDataSet")
    public void deleteExistingRecipient(HashMap<String, String> input)  throws InterruptedException, IOException{

        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        BuyElectricityPage buyPage = new BuyElectricityPage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
        properties.load(fis);

        String name = input.get("name");
        String amount = input.get("amount");
        String ref = input.get("ref");
        String appLogin = properties.getProperty("appLogin");
        String profileName = properties.getProperty("profileName");

        loginPage.loginWithRetry(profileName,appLogin,2);
        accountMenuActions.clickAccountMenuActionsButtn();
        buyPage.clickBuyButton();
        buyPage.getExistingRecipient(input.get("name"));
        buyPage.clickEditButton();
        buyPage.clickDeleteButton();
        String actualTxt = buyPage.getDeleteToastMsg();
        try {
            Assert.assertEquals(actualTxt,"Item deleted!");
            log.info("Recipient deleted");
        } catch (Exception| AssertionError e) {
            log.warn("Test failed due to: {}",e);
            Assert.fail("Test failed");
            throw new RuntimeException(e);
        }
        buyPage.clickBack();
    }

    @DataProvider
    public Object[] [] getSingleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//buyElectricityData.json");
        return new Object[][]{{data.get(0)}};
    }

    @AfterMethod
    public void cleanUp() {
        try {
            HomePage homePage = new HomePage(driver);
            homePage.clickLogoutButtn();
        } catch (Exception e) {
            log.error("Cleanup failed: ", e);
        }
    }
}
