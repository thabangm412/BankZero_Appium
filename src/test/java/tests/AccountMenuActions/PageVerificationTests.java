package tests.AccountMenuActions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class PageVerificationTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(PageVerificationTests.class);

    @BeforeMethod
    public void preSetUp() throws IOException {
        AndroidActions androidActions = new AndroidActions(driver);
        androidActions.environmentChange();

        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
        properties.load(fis);

        String appLogin = properties.getProperty("appLogin");
        String profileName = properties.getProperty("profileName");

        loginPage.loginWithRetry(profileName,appLogin,2);

    }

    @Test()
    public void payPageConfirmation() throws IOException {

        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        Assert.assertEquals(accountMenuActions.confirmPage("pay"), "Quick Pay");
        accountMenuActions.clickBack();

    }

    @Test()
    public void payManyPageConfirmation() {
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        Assert.assertEquals(accountMenuActions.confirmPage("pay many"), "Pay Many");
        accountMenuActions.clickBack();
    }


    @Test()
    public void transferPageConfirmation() {

        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        Assert.assertEquals(accountMenuActions.confirmPage("transfer"), "Transfer");
        accountMenuActions.clickBack();
    }

    @Test()
    public void buyPageConfirmation() {

        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        Assert.assertEquals(accountMenuActions.confirmPage("buy"), "Quick Buy");
        accountMenuActions.clickBack();
    }

    @Test()
    public void sendMoneyPageConfirmation() {
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        Assert.assertEquals(accountMenuActions.confirmPage("send money"), "Send Money");
        accountMenuActions.clickBack();
    }

    @Test()
    public void friendsPageConfirmation() {
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        Assert.assertEquals(accountMenuActions.confirmPage("friends"), "Friends");
        accountMenuActions.clickBack();
    }

    @Test()
    public void statementsAndLettersPageConfirmation() {
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        Assert.assertEquals(accountMenuActions.confirmPage("statements"), "Statements & letters");
        accountMenuActions.clickBack();
    }

    @Test()
    public void ficaDocumentsPageConfirmation() {
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        Assert.assertEquals(accountMenuActions.confirmPage("fica documents"), "FICA Documents");
        accountMenuActions.clickBack();
    }

    @Test()
    public void settingsPageConfirmation() {
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        Assert.assertEquals(accountMenuActions.confirmPage("settings"), "Settings");
        accountMenuActions.clickBack();
    }

    @Test()
    public void cardPageConfirmation()
    {
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        accountMenuActions.clickAccountMenuActionsButtn();
        accountMenuActions.clickCardButton();
        Assert.assertTrue(accountMenuActions.cardWidget());
        driver.navigate().back();
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
