package tests.Business.AuthChain;

import io.appium.java_client.AppiumBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.business.BusinessAuthChainPage;
import pageObjects.app.business.BusinessPage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import tests.Business.OwnersAndAuthorisers.AddOwnersAndAuthorisersTests;
import utils.AndroidActions;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class AuthChainTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(AuthChainTests.class);

    private LoginPage loginPage;
    private BusinessPage businessPage;
    private BusinessAuthChainPage businessAuthChainPage;

    private  AndroidActions androidActions;

    @BeforeMethod
    public void setUpPages() {
        // initialize page objects once per test method
        log.debug("Initializing page objects for test.");
        loginPage = new LoginPage(driver);
        businessPage = new BusinessPage(driver);
        androidActions = new AndroidActions(driver);
        businessAuthChainPage = new BusinessAuthChainPage(driver);
    }

    @Test(dataProvider = "getMultipleDataSet")
    public void addNewOAuthChainOwner(HashMap<String, String> input) throws InterruptedException {
        log.info("Starting addingOwnersAndAuthorisers on auth chain test");

        androidActions.validateInputKeys(input, "profileName", "loginPin", "ownerName", "cellNumber","role","nationality");

        String profileName = input.get("profileName");
        String loginPin = input.get("loginPin");
        log.info("Logging in with profile: {}", profileName);
        // do not log sensitive values such as PIN
        loginPage.loginWithRetry(profileName, loginPin, 2);

        businessPage.clickBusinessMenuActionButtn();
        businessAuthChainPage.clickAuthorisationChain();
        businessPage.addOwnerForAuthChain();
        businessPage.addOwnersAndOfficials(input.get("role"),input.get("nationality"),input.get("cellNumber"),input.get("ownerName"));
        businessPage.saveChanges();
        Thread.sleep(3000);
        businessPage.clickFinish();
        businessPage.clickBusinessMenuActionButtn();
        businessAuthChainPage.clickAuthorisationChain();
        Thread.sleep(3000);
        businessAuthChainPage.scrollRightToTile();
        androidActions.assertTextPresentExact(input.get("ownerName"));
        driver.navigate().back();
        businessAuthChainPage.confirmAbort();

        log.info("adding Owners on Auth Chain test completed successfully.");

    }

    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        String path =
                System.getProperty("user.dir") + File.separator + "src" + File.separator
                        + "test" + File.separator + "java" + File.separator + "testData" + File.separator + "authChainData.json";
        log.debug("Loading test data from: {}", path);

        List<HashMap<String, String>> data = getJsonData(path);
        if (data == null || data.isEmpty()) {
            log.error("No test data found at: {}", path);
            throw new IllegalStateException("Test data is empty: " + path);
        }

        log.info("Providing {} data set(s) to test", data.size());
        return new Object[][]{{data.getFirst()}};
    }

    @AfterMethod
    public void cleanUp() {
        try {
            HomePage homePage = new HomePage(driver);
            homePage.clickLogoutButtn();
            log.info("Logged out successfully during cleanup.");
        } catch (Exception e) {
            log.error("Cleanup failed: ", e);
        }
    }
}
