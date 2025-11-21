package tests.Login;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;

public class LoginlaunchTest extends BaseTestsConfig {

    @Test
    public void launchScreenTest()
    {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        int loginPin = 33333;
        loginPage.enterLoginPin(loginPin);
        Assert.assertEquals(homePage.getHomePageConfirm(), "Accounts");
        homePage.clickLogoutButtn();
        Assert.assertTrue(loginPage.loginPageConfirm());
    }
}
