package tests.Login;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;

import java.util.HashMap;
import java.util.Map;

public class LoginNegativeTests extends BaseTestsConfig {
    @BeforeMethod
    public void preSetUp() {
        //Start app from a loginPage
        Map<String, Object> args = new HashMap<>();
        args.put("appPackage", "za.co.neolabs.bankzero");
        args.put("appActivity", "za.co.neolabs.bankzero.LoginActivity");
        driver.executeScript("mobile: startActivity", args);
    }

    @Test
    public void logInError()
    {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        loginPage.enterLoginPin(12345);
        //Assert.assertNotEquals(homePage.getHomePageConfirm(), "Accounts");*****Need to get element for the error message returned
    }
}
