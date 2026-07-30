package tests.DevicePair;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.Registration.RegisterOTP;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.addAccount.AddAccountPage;
import pageObjects.app.login.LoginPage;
import pageObjects.app.login.PairOnDevicePage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;
import utils.AppiumUtils;
import utils.DriverManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class DevicePairTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(DevicePairTests.class);

    private AndroidActions androidActions;
    private PairOnDevicePage pairOnDevicePage;
    private RegisterOTP registerOTP;
    private LoginPage loginPage;
    private HomePage homePage;

    private AddAccountPage addAccountPage;

    @BeforeMethod
    public void preSetUp() {
        androidActions  = new AndroidActions(DriverManager.driver);
        //androidActions.environmentChange();

        // initialize page objects once per test
        pairOnDevicePage = new PairOnDevicePage(DriverManager.driver);
        registerOTP = new RegisterOTP(DriverManager.driver);
        loginPage = new LoginPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        addAccountPage = new AddAccountPage(DriverManager.driver);

        log.debug("Page objects and androidActions initialized");
    }

    @Test(dataProvider = "getSingleDataSet", priority = 0)
    public void DevicePairTest(HashMap<String, String> input) throws InterruptedException {

        validateInput(input,
                "cellNumber", "idNumber", "prefName", "loginPin",
                "alNumber");

        // Environment check
        androidActions.environmentChange();

        // Enter user inputs into the app
        pairOnDevicePage.addProfile();
        pairOnDevicePage.partialRegistrationCheck();
        pairOnDevicePage.enterCellNumber(input.get("cellNumber"));
        pairOnDevicePage.enterIdNumber(input.get("idNumber"));
        pairOnDevicePage.enterPreferredName(input.get("prefName"));
        attachScreenshot(DriverManager.driver, "Device_Pairing_Input");
        pairOnDevicePage.clickSubmit();

        pairOnDevicePage.enterLoginPin(Integer.parseInt(input.get("loginPin")));
        pairOnDevicePage.enterCardPin(Integer.parseInt(input.get("loginPin")));
        pairOnDevicePage.setAcceptTermsAndConditions();

        // Enter OTP
        registerOTP.enterOTP(input.get("cellNumber"), input.get("alNumber"));
        registerOTP.clickSubmitButton();
        Thread.sleep(3000);

        Assert.assertTrue(loginPage.loginPageConfirm());
        attachScreenshot(DriverManager.driver, "Device_Pairing_Login_Page_Displayed");
    }

    @Test(dataProvider = "getSingleDataSet", priority = 1)
    public void DevicePairLoginTest(HashMap<String, String> input) throws InterruptedException
    {
        validateInput(input,
                  "prefName", "loginPin");

        try {
            loginPage.loginAccount(input.get("prefName"));
            loginPage.enterLoginPin(Integer.parseInt(input.get("loginPin")));
        } catch (Exception e) {
            try {
                loginPage.loginAccount(input.get("prefName"));
                attachScreenshot(DriverManager.driver, "Login_Retry");
            } catch (NoSuchElementException ex) {
                throw new RuntimeException(ex);
            }
        }

        Assert.assertEquals(homePage.getHomePageConfirm(), "Accounts");
        attachScreenshot(DriverManager.driver, "Login_Success");
        log.info("User logged in, Accounts screen displayed");

    }

    @Test(dataProvider = "getSingleDataSet", priority = 2)
    public void DevicePairLogOutTest(HashMap<String, String> input) throws InterruptedException
    {
        homePage.clickLogoutButtn();
        Assert.assertEquals(loginPage.getLoginPageConfirm(),"Login");
        attachScreenshot(DriverManager.driver, "Logout_Success");
        log.info("User logged out, Login screen displayed");

    }

    @Test(dataProvider = "getSingleDataSet", priority = 3)
    public void SafeModeCheckTest(HashMap<String, String> input) throws InterruptedException
    {
        validateInput(input,
                "prefName", "loginPin","safeModeMsg");

        try {
            loginPage.loginAccount(input.get("prefName"));
            loginPage.enterLoginPin(Integer.parseInt(input.get("loginPin")));
        } catch (Exception e) {
            try {
                loginPage.loginAccount(input.get("prefName"));
            } catch (NoSuchElementException ex) {
                throw new RuntimeException(ex);
            }
        }
        addAccountPage.clickAddAccButtn();
        try {
            Assert.assertEquals(loginPage.getSafeModeMsg(), input.get("safeModeMsg"));
            attachScreenshot(DriverManager.driver, "Safe_Mode_Alert_Displayed");
            log.info("Safe mode alert displayed with expected message: {}", input.get("safeModeMsg"));

        } catch (TimeoutException te) {
            log.error("No alert was displayed after clicking add account button", te);
            Assert.fail("Expected alert was not displayed.");
        } catch (AssertionError ae) {
            log.error("Alert message did not match the expected value", ae);
            throw ae; // Let the test fail
        }finally {
            Thread.sleep(3000);
            homePage.clickLogoutButtn();
        }
    }

//    @Test(dataProvider = "getSingleDataSet", priority = 3)
//    public void NegativeSafeModeCheckTest(HashMap<String, String> input) throws InterruptedException
//    {
//        try {
//            loginPage.loginAccount(input.get("prefName"));
//            loginPage.enterLoginPin(Integer.parseInt(input.get("loginPin")));
//        } catch (Exception e) {
//            try {
//                loginPage.loginAccount(input.get("prefName"));
//            } catch (NoSuchElementException ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//        addAccountPage.clickAddAccButtn();
//        try {
//            Assert.assertEquals(loginPage.getSafeModeMsg(), input.get("negativeSafeModeMsg"));
//
//        } catch (TimeoutException te) {
//            log.error("No alert was displayed after clicking add account button", te);
//            Assert.fail("Expected alert was not displayed.");
//        } catch (AssertionError ae) {
//            log.error("Alert message did not match the expected value", ae);
//            throw ae; // Let the test fail
//        }finally {
//            Thread.sleep(3000);
//            homePage.clickLogoutButtn();
//        }
//    }

    @Test(dataProvider = "getSingleDataSet", priority = 4)
    public void SafeModeLiftTest(HashMap<String, String> input) throws InterruptedException
    {
        AccountMenuActions accountMenuActions = new AccountMenuActions(DriverManager.driver);
        validateInput(input,
                "prefName", "loginPin","safeModeMsg");

        String sshUser = System.getenv("SSH_USER");
        String sshPassword = System.getenv("SSH_PASSWORD");
        String sshHost = System.getenv("SSH_HOST");
        int sshPort = Integer.parseInt(System.getenv("SSH_PORT"));

        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");
        String dbName = System.getenv("DB_NAME");
        String remoteHost = System.getenv("DB_REMOTE_HOST");
        int remotePort = Integer.parseInt(System.getenv("DB_REMOTE_PORT"));
        int localPort = Integer.parseInt(System.getenv("DB_LOCAL_PORT"));

        String phoneNumber = input.get("cellNumber");

        AppiumUtils.disableSafeMode(sshUser, sshHost, sshPort, sshPassword,
                dbUser, dbPassword, dbName,
                phoneNumber, remoteHost, remotePort, localPort);
        Thread.sleep(3000);

        String name = input.get("prefName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        addAccountPage.clickAddAccButtn();
        try {
            Assert.assertEquals(addAccountPage.getPageConfirmation(), "Add");
            attachScreenshot(DriverManager.driver, "Safe_Mode_Lifted_Add_Account_Page");
            log.info("Assertion passed: Page confirmation is 'Add'");
        } catch (AssertionError ae) {
            log.warn("Assertion failed: Expected page confirmation to be 'Add', but was: "
                    + addAccountPage.getPageConfirmation());
            throw new RuntimeException("Page confirmation assertion failed", ae);
        }
        accountMenuActions.clickBack();
        homePage.clickLogoutButtn();
    }


    @DataProvider
    public Object[] [] getSingleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//devicePairData.json");
        return new Object[][]{{data.get(1)}};
    }

}
