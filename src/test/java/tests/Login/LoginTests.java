package tests.Login;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class LoginTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(LoginTests.class);

    @Test(dataProvider = "getSingleDataSet")
    public void loginPositiveFlow(HashMap<String, String> input) throws InterruptedException, IOException {
        AndroidActions androidActions = new AndroidActions(driver);
        androidActions.loginEnvironmentCheck();

        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);

        String name = input.get("name");
        String appPin = input.get("appLogin");

        loginPage.loginWithRetry(name,appPin,2);

        Assert.assertEquals(homePage.getHomePageConfirm(), "Accounts");
        log.info("User logged in, Accounts screen displayed");
        Thread.sleep(3000);

    }

    @Test(priority = 1)
    public void logOutPositiveFlow()
    {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        homePage.clickLogoutButtn();
        Assert.assertEquals(loginPage.getLoginPageConfirm(),"Login");

    }

    @DataProvider
    public Object[] [] getSingleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//customerLoginData.json");
        return new Object[][]{{data.getFirst()}};
    }
}
