package tests.Buy.BuyElectricity;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
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

public class ExistingElectricityAccTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(ExistingElectricityAccTests.class);
    private LoginPage loginPage;
    private AccountMenuActions accountMenuActions;
    private BuyElectricityPage buyPage;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        buyPage = new BuyElectricityPage(DriverManager.driver);

    }

    @Test(dataProvider = "getMultipleDataSet", priority = 0)
    public void existingElectricityAccTest(HashMap<String, String> input) throws InterruptedException, IOException {

        validateInput(input,
                "name", "amount", "ref"
        );
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
        properties.load(fis);

        String appLogin = properties.getProperty("appLogin");
        String profileName = properties.getProperty("profileName");

        loginPage.loginWithRetry(profileName,appLogin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        buyPage.clickBuyButton();
        buyPage.buyElectricityAgain(input.get("name"),input.get("amount"),input.get("ref"));
        attachScreenshot(DriverManager.driver,"Confirm Electricity Purchase");
        buyPage.clickConfrimButton();
        Thread.sleep(5000);

        try {
            String status = buyPage.getTransactionStatus();

            try {
                Assert.assertEquals(status, "Success");
                log.info("Transactional Status: {}",status);
                attachScreenshot(DriverManager.driver,"Electricity Purchase Success");
            } catch (AssertionError e) {
                log.warn("Transaction failed with status: {}", status);
                throw e;  // Let TestNG fail the test
            }
        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
            log.error("Exception occurred during transaction handling: ", e);
        }finally {
            buyPage.clickFinishButton();
        }

    }

    @Test(dataProvider = "getMultipleDataSet", priority = 1)
    public void deleteExistingElectricityRecipient(HashMap<String, String> input) throws InterruptedException, IOException {

        validateInput(input, "name", "amount", "ref");

        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "//src//main//java//resources//data.properties");
        properties.load(fis);

        String appLogin = properties.getProperty("appLogin");
        String profileName = properties.getProperty("profileName");

        loginPage.loginWithRetry(profileName, appLogin, 2);
        accountMenuActions.clickAccountMenuActionsButtn();
        buyPage.clickBuyButton();
        buyPage.getExistingRecipient(input.get("name"));
        attachScreenshot(DriverManager.driver, "Existing Electricity Recipient Retrieved");
        buyPage.clickEditButton();
        buyPage.clickDeleteButton();

        try {
            Assert.assertTrue(buyPage.isRecipientDeleted(input.get("name")));
            log.info("Recipient deleted successfully");
            attachScreenshot(DriverManager.driver,"Electricity Recipient Deletion Success");
        } catch (Exception| AssertionError e) {
            log.warn("Test failed due to: {}",e);
            Assert.fail("Test failed");
            throw new RuntimeException(e);
        }
        DriverManager.driver.navigate().back();
        buyPage.clickBack();
    }


    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//buyElectricityData.json");
        return new Object[][]{{data.get(0)},{data.get(1)}};
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
