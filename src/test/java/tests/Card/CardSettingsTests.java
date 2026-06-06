package tests.Card;

import factory.TransferDataFactory;
import models.User;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;
import pageObjects.app.Registration.RegisterOTP;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.card.MyCardPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.addAccount.AddAccountPage;
import pageObjects.app.login.LoginPage;
import pageObjects.app.login.PairOnDevicePage;
import testConfig.BaseTestsConfig;
import tests.DevicePair.DevicePairTests;
import utils.AndroidActions;
import utils.DriverManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CardSettingsTests extends BaseTestsConfig {
    private static final Logger log = LoggerFactory.getLogger(CardSettingsTests.class);

    private LoginPage loginPage;
    private HomePage homePage;
    private AccountMenuActions accountMenuActions;
    private MyCardPage cardPage;
    private User appUser;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        cardPage = new MyCardPage(DriverManager.driver);
        appUser = TransferDataFactory.validAppUser();

        log.debug("Page objects and androidActions initialized");
    }


    @Test(priority = 0)
    public void lockCardTest()
    {

        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsButtn();
        cardPage.clickCardMenuActionButton();
        cardPage.clickLockCard();
        Assert.assertEquals(cardPage.getToastMessage(),"Card is now locked");
        attachScreenshot(DriverManager.driver,"LockCard");

    }

    @Test(priority = 1)
    public void unlockCardTest()
    {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );
        accountMenuActions.clickAccountMenuActionsButtn();
        cardPage.clickCardMenuActionButton();
        cardPage.clickUnlockCard();
        Assert.assertEquals(cardPage.getToastMessage(),"Card is now unlocked");
        attachScreenshot(DriverManager.driver,"UnlockCard");

    }

    @Test()
    public void enableAtmWithdrawals()
    {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsButtn();
        cardPage.clickCardMenuActionButton();
        cardPage.clickCardSettings();
        cardPage.atmConfiguration("Always on");
        Assert.assertEquals(cardPage.getUpdatedFields("on/off"),"Always on");
        attachScreenshot(DriverManager.driver,"Enable_ATM_Withdrawals");
    }

    @Test()
    public void disableAtmWithdrawals()
    {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsButtn();
        cardPage.clickCardMenuActionButton();
        cardPage.clickCardSettings();
        cardPage.atmConfiguration("Always off");
        Assert.assertEquals(cardPage.getUpdatedFields("on/off"),"Always off");
        attachScreenshot(DriverManager.driver,"Disable_ATM_Withdrawals");
    }

    @Test()
    public void set1Cash()
    {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsButtn();
        cardPage.clickCardMenuActionButton();
        cardPage.clickCardSettings();
        cardPage.atmConfiguration("1 cash");
        Assert.assertEquals(cardPage.getUpdatedFields("on/off"),"1 cash");
        attachScreenshot(DriverManager.driver,"Set_1_Cash");

    }

    @Test()
    public void updateLocalDailyCash()
    {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsButtn();
        cardPage.clickCardMenuActionButton();
        cardPage.clickCardSettings();
        cardPage.updateLocalDailyCash("1200");
        attachScreenshot(DriverManager.driver,"Local_Daily_Cash_Updated");
        cardPage.clickUpdate();
        Assert.assertEquals(cardPage.getUpdatedFields("local daily"),"R1 200.00");
        attachScreenshot(DriverManager.driver,"Update_Local_Daily_Cash");

    }

    @Test()
    public void updateGlobalDailyCash()
    {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsButtn();
        cardPage.clickCardMenuActionButton();
        cardPage.clickCardSettings();
        cardPage.updateGlobalDailyCash("1200");
        attachScreenshot(DriverManager.driver,"Global_Daily_Cash_Updated");
        cardPage.clickUpdate();
        Assert.assertEquals(cardPage.getUpdatedFields("global daily"),"R1 200.00");
        attachScreenshot(DriverManager.driver,"Update_Global_Daily_Cash");
    }

    @Test()
    public void updateOnlineMaxCash( )
    {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsButtn();
        cardPage.clickCardMenuActionButton();
        cardPage.clickCardSettings();
        cardPage.onlineMaxUpdate("1200");
        attachScreenshot(DriverManager.driver,"Online_Max_Cash_Updated");
        cardPage.clickUpdate();
        Assert.assertEquals(cardPage.getUpdatedFields("online"),"R1 200.00");
        attachScreenshot(DriverManager.driver,"Update_Online_Max_Cash");
    }

    @AfterMethod
    public void postTestCleanUp()
    {
        log.info("Post-test cleanup: Resetting card settings to default values.");
        cardPage.tryClickFinish();
        homePage.clickLogoutButtn();
    }

}
