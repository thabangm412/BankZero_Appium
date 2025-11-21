package tests.DevicePair;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class DevicePairTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(DevicePairTests.class);


    @Test(dataProvider = "getSingleDataSet", priority = 0)
    public void DevicePairTest(HashMap<String, String> input) throws InterruptedException {

        AndroidActions androidActions = new AndroidActions(driver);
        PairOnDevicePage pairOnDevicePage = new PairOnDevicePage(driver);
        RegisterOTP registerOTP = new RegisterOTP(driver);
        LoginPage loginPage = new LoginPage(driver);

        // Environment check
        androidActions.environmentChange();

        // Enter user inputs into the app
        pairOnDevicePage.enterCellNumber(input.get("cellNumber"));
        pairOnDevicePage.enterIdNumber(input.get("idNumber"));
        pairOnDevicePage.enterPreferredName(input.get("prefName"));
        pairOnDevicePage.clickSubmit();

        pairOnDevicePage.enterLoginPin(Integer.parseInt(input.get("loginPin")));
        pairOnDevicePage.enterCardPin(Integer.parseInt(input.get("loginPin")));
        pairOnDevicePage.setAcceptTermsAndConditions();

        // Enter OTP
        registerOTP.enterOTP(input.get("cellNumber"), input.get("alNumber"));
        registerOTP.clickSubmitButton();
        //Thread.sleep(3000);

        Assert.assertTrue(loginPage.loginPageConfirm());

    }

    @Test(dataProvider = "getSingleDataSet", priority = 1)
    public void DevicePairLoginTest(HashMap<String, String> input) throws InterruptedException
    {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        AndroidActions androidActions = new AndroidActions(driver);
        androidActions.environmentChange();

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

        Assert.assertEquals(homePage.getHomePageConfirm(), "Accounts");
        log.info("User logged in, Accounts screen displayed");

    }

    @Test(dataProvider = "getSingleDataSet", priority = 2)
    public void DevicePairLogOutTest(HashMap<String, String> input) throws InterruptedException
    {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        homePage.clickLogoutButtn();
        Assert.assertEquals(loginPage.getLoginPageConfirm(),"Login");
        log.info("User logged out, Login screen displayed");

    }

    @Test(dataProvider = "getSingleDataSet", priority = 3)
    public void SafeModeCheckTest(HashMap<String, String> input) throws InterruptedException
    {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        AddAccountPage addAccountPage = new AddAccountPage(driver);
        AndroidActions androidActions = new AndroidActions(driver);

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

    @Test(dataProvider = "getSingleDataSet", priority = 3)
    public void NegativeSafeModeCheckTest(HashMap<String, String> input) throws InterruptedException
    {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        AddAccountPage addAccountPage = new AddAccountPage(driver);
        AndroidActions androidActions = new AndroidActions(driver);

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
            Assert.assertEquals(loginPage.getSafeModeMsg(), input.get("negativeSafeModeMsg"));

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

    @Test(dataProvider = "getSingleDataSet", priority = 4)
    public void SafeModeLiftTest(HashMap<String, String> input) throws InterruptedException
    {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        AddAccountPage addAccountPage = new AddAccountPage(driver);
        AndroidActions androidActions = new AndroidActions(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);

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
        return new Object[][]{{data.get(0)}};
    }

}
