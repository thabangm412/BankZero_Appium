package tests.DevicePair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.Registration.RegisterOTP;
import pageObjects.app.login.LoginPage;
import pageObjects.app.login.PairOnDevicePage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class DevicePairNegativeTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(DevicePairNegativeTests.class);

    @Test(dataProvider = "getSingleDataSet")
    public void InvalidLoginPinTest(HashMap<String, String> input) throws InterruptedException
    {
        LoginPage loginPage = new LoginPage(driver);
        AndroidActions androidActions = new AndroidActions(driver);
        androidActions.environmentChange();

        String name = input.get("name");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);
        try {
            Assert.assertEquals(loginPage.getLoginErrMsg(), input.get("errMsg"));
            log.info("Assertion passed: Expected error message is displayed.");
        } catch (AssertionError e) {
            log.error("Assertion failed. Expected error message: '{}', but got: '{}'",
                    input.get("errMsg"), loginPage.getLoginErrMsg());
            throw e; // rethrowing to mark the test as failed
        }

        log.info("User logged in, Accounts screen displayed (if assertion passed).");

    }

    @Test(dataProvider = "getSingleDataSet")
    public void AlreadyPairedProfileTest(HashMap<String, String> input) throws InterruptedException {

        PairOnDevicePage pairOnDevicePage = new PairOnDevicePage(driver);
        RegisterOTP registerOTP = new RegisterOTP(driver);
        LoginPage loginPage = new LoginPage(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        // Environment check
        androidActions.environmentChange();
        loginPage.clickAddAccount();

        // Enter user inputs into the app
        try {
            pairOnDevicePage.enterCellNumber(input.get("cellNumber"));
            pairOnDevicePage.enterIdNumber(input.get("idNumber"));
            pairOnDevicePage.enterPreferredName(input.get("prefName"));
            pairOnDevicePage.clickSubmit();

            Assert.assertEquals(pairOnDevicePage.getDeviceAlreadyExitMsg(), "Number already registered on device");
            log.info("Assertion passed: Device already exists message displayed correctly.");

        }catch (AssertionError ae) {
                log.warn("Assertion failed: Expected 'Number already registered on device', but got: "
                        + pairOnDevicePage.getDeviceAlreadyExitMsg());
                Assert.fail("Assertion failed: Incorrect device registration message");
        } catch (Exception e) {
            log.warn("Unexpected exception during AlreadyPairedProfileTest: " + e.getMessage());
            Assert.fail("Test failed due to unexpected exception: " + e.getMessage());
        }
    }


    @DataProvider
    public Object[] [] getSingleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//devicePairData.json");
        return new Object[][]{{data.get(1)}};
    }



}
