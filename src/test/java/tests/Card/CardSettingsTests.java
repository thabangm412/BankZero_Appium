package tests.Card;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.card.MyCardPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CardSettingsTests extends BaseTestsConfig {

    @Test(dataProvider = "getMultipleDataSet")
    public void lockCardTest(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        MyCardPage cardPage = new MyCardPage(driver);
        HomePage homePage = new HomePage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        cardPage.clickCardMenuActionButton();
        cardPage.clickLockCard();

        String expectedTxt = "Card is now locked";
        String actualTxt = cardPage.getToast1Message();

        try {
            Assert.assertEquals(actualTxt,expectedTxt);
        } catch (Exception| AssertionError e) {
            Assert.fail("Test failed due:{}",e);
            throw new RuntimeException(e);
        }
        homePage.clickLogoutButtn();

    }

    @Test(dataProvider = "getMultipleDataSet")
    public void unlockCardTest(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        MyCardPage cardPage = new MyCardPage(driver);
        HomePage homePage = new HomePage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        cardPage.clickCardMenuActionButton();
        cardPage.clickUnlockCard();


        String expectedTxt = "Card is now unlocked";
        String actualTxt = cardPage.getToast2Message();
        try {
            Assert.assertEquals(actualTxt,expectedTxt);
        } catch (Exception| AssertionError e) {
            Assert.fail("Test failed due alert msg not displayed");
            throw new RuntimeException(e);
        }
        homePage.clickLogoutButtn();

    }

    @Test(dataProvider = "getMultipleDataSet")
    public void enableAtmWithdrawals(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        MyCardPage cardPage = new MyCardPage(driver);
        HomePage homePage = new HomePage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        String applogin = input.get("loginPin");

        try {
            loginPage.loginAccount(input.get("profileName"));
            loginPage.enterLoginPin(Integer.parseInt(applogin));
        } catch (Exception e) {
            try {
                loginPage.loginAccount(input.get("profileName"));
            } catch (NoSuchElementException ex) {
                throw new RuntimeException(ex);
            }
        }

        accountMenuActions.clickAccountMenuActionsButtn();
        cardPage.clickCardMenuActionButton();
        cardPage.clickCardSettings();
        cardPage.enableATM();

        try {
            Assert.assertEquals(cardPage.getUpdatedFields("on/off"),"Always on");
        } catch (Exception| AssertionError e) {
            Assert.fail("Test failed");
            throw new RuntimeException(e);
        }finally {
            cardPage.clickFinish();
        }

        homePage.clickLogoutButtn();
    }

    @Test(dataProvider = "getMultipleDataSet")
    public void disableAtmWithdrawals(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        MyCardPage cardPage = new MyCardPage(driver);
        HomePage homePage = new HomePage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        String applogin = input.get("loginPin");

        try {
            loginPage.loginAccount(input.get("profileName"));
            loginPage.enterLoginPin(Integer.parseInt(applogin));
        } catch (Exception e) {
            try {
                loginPage.loginAccount(input.get("profileName"));
            } catch (NoSuchElementException ex) {
                throw new RuntimeException(ex);
            }
        }

        accountMenuActions.clickAccountMenuActionsButtn();
        cardPage.clickCardMenuActionButton();
        cardPage.clickCardSettings();
        cardPage.disableATM();

        try {
            Assert.assertEquals(cardPage.getUpdatedFields("on/off"),"Always off");
        } catch (Exception| AssertionError e) {
            Assert.fail("Test failed");
            throw new RuntimeException(e);
        }finally {
            cardPage.clickFinish();
        }
        homePage.clickLogoutButtn();
    }

    @Test(dataProvider = "getMultipleDataSet")
    public void updateLocalDailyCash(HashMap<String, String> input)
    {

        LoginPage loginPage = new LoginPage(driver);
        MyCardPage cardPage = new MyCardPage(driver);
        HomePage homePage = new HomePage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        //String applogin = input.get("loginPin");

        try {
            loginPage.loginAccount(input.get("profileName"));
            loginPage.enterLoginPin(Integer.parseInt(input.get("loginPin")));
        } catch (Exception e) {
            try {
                loginPage.loginAccount(input.get("profileName"));
                loginPage.enterLoginPin(Integer.parseInt(input.get("loginPin")));
            } catch (NoSuchElementException ex) {
                throw new RuntimeException(ex);
            }
        }

        accountMenuActions.clickAccountMenuActionsButtn();
        cardPage.clickCardMenuActionButton();
        cardPage.clickCardSettings();
        cardPage.updateLocalDailyCash("1200");
        cardPage.clickUpdate();

        try {
            Assert.assertEquals(cardPage.getUpdatedFields("local daily"),"R1 200.00");
        } catch (Exception| AssertionError e) {
            Assert.fail("Test failed");
            throw new RuntimeException(e);
        }finally {
            cardPage.clickFinish();
        }
        homePage.clickLogoutButtn();

    }

    @Test(dataProvider = "getMultipleDataSet")
    public void updateGlobalDailyCash(HashMap<String, String> input)
    {

        LoginPage loginPage = new LoginPage(driver);
        MyCardPage cardPage = new MyCardPage(driver);
        HomePage homePage = new HomePage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        cardPage.clickCardMenuActionButton();
        cardPage.clickCardSettings();
        cardPage.updateGlobalDailyCash("1200");
        cardPage.clickUpdate();

        try {
            Assert.assertEquals(cardPage.getUpdatedFields("global daily"),"R1 200.00");
        } catch (Exception| AssertionError e) {
            Assert.fail("Test failed");
            throw new RuntimeException(e);
        }finally {
            cardPage.clickFinish();
        }
        homePage.clickLogoutButtn();
    }

    @Test(dataProvider = "getMultipleDataSet")
    public void updateOnlineMaxCash(HashMap<String, String> input)
    {

        LoginPage loginPage = new LoginPage(driver);
        MyCardPage cardPage = new MyCardPage(driver);
        HomePage homePage = new HomePage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        String name = input.get("profileName");
        String appPin = input.get("loginPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        cardPage.clickCardMenuActionButton();
        cardPage.clickCardSettings();
        cardPage.onlineMaxUpdate("1200");
        cardPage.clickUpdate();

        try {
            Assert.assertEquals(cardPage.getUpdatedFields("online"),"R1 200.00");
        } catch (Exception| AssertionError e) {
            Assert.fail("Test failed");
            throw new RuntimeException(e);
        }finally {
            cardPage.clickFinish();
        }
        homePage.clickLogoutButtn();
    }



    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//cardData.json");
        return new Object[][]{{data.getFirst()}};
    }
}
