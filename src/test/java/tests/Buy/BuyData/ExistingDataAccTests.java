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
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.DriverManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class ExistingDataAccTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(ExistingDataAccTests.class);
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
    public void existingDataAccTest(HashMap<String, String> input) throws IOException {
        validateInput(input,
                "DataName", "amount", "ref"
        );
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
        properties.load(fis);

        String appLogin = properties.getProperty("appLogin");
        String profileName = properties.getProperty("profileName");

        loginPage.loginWithRetry(profileName,appLogin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        buyDataPage.clickBuyButton();
        buyDataPage.getExistingProfile(input.get("DataName"));
        attachScreenshot(DriverManager.driver,"Existing Data Profile Retrieved");
        buyDataPage.clickRedo();
        buyDataPage.clickHomeBuyButton();
        attachScreenshot(DriverManager.driver,"Data Purchase Page");
        buyDataPage.clickConfirmButton();

        try {
            String status = buyDataPage.getTransactionStatus();

            try {
                Assert.assertEquals(status, "Success");
                log.info("Transactional Status: {}",status);
                attachScreenshot(DriverManager.driver,"Existing Data Profile Transaction Status");
            } catch (AssertionError e) {
                log.warn("Transaction failed with status: {}", status);
                throw e;  // Let TestNG fail the test
            }
        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
            log.error("Exception occurred during transaction handling: ", e);
        }
        buyDataPage.clickFinishButton();
    }

    @Test(dataProvider = "getMultipleDataSet")
    public void deleteExistingDataRecipient(HashMap<String, String> input)  throws InterruptedException, IOException{

        validateInput(input,
                "DataName", "amount", "ref"
        );
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
        properties.load(fis);

        String appLogin = properties.getProperty("appLogin");
        String profileName = properties.getProperty("profileName");

        loginPage.loginWithRetry(profileName,appLogin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        buyDataPage.clickBuyButton();
        buyDataPage.getExistingProfile(input.get("DataName"));
        attachScreenshot(DriverManager.driver,"Existing Data Profile Retrieved for Deletion");
        buyDataPage.clickEditButton();
        buyDataPage.clickDeleteButton();

        try {
            Assert.assertTrue(buyDataPage.isRecipientDeleted(input.get("DataName")));
            log.info("Recipient deletion confirmed: {}",input.get("DataName"));
//            Assert.assertEquals(actualTxt,"Item deleted!");
//            log.info("Recipient deleted");
            attachScreenshot(DriverManager.driver,"Data Recipient Deleted");
        } catch (Exception| AssertionError e) {
            Assert.fail("Test failed");
            log.warn("Test failed due to: {}",e);
            throw new RuntimeException(e);
        }
        DriverManager.driver.navigate().back();
        buyDataPage.clickBack();
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

            homePage.clickLogoutButtn();
        } catch (Exception e) {
            log.error("Cleanup failed: ", e);
        }
    }

}
