package tests.Card;

import factory.TransferDataFactory;
import lombok.extern.slf4j.Slf4j;
import models.CardData;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.card.MyCardPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;
import utils.DriverManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;



public class ViewCardTests extends BaseTestsConfig {
    private static final Logger log = LoggerFactory.getLogger(ViewCardTests.class);
    private LoginPage loginPage;
    private HomePage homePage;
    private AccountMenuActions accountMenuActions;
    private MyCardPage cardPage;
    private User appUser;
    private CardData cardData;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        cardPage = new MyCardPage(DriverManager.driver);
        appUser = TransferDataFactory.validAppUser();
        cardData = TransferDataFactory.validCardData();

        log.debug("Page objects and androidActions initialized");
    }

    @Test(priority = 0)
    public void viewCardTest()
    {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsButtn();
        cardPage.clickCardMenuActionButton();
        cardPage.clickViewCard();
        Assert.assertEquals(cardPage.getCardDisplayed(),cardData.getCardNumber());
        attachScreenshot(DriverManager.driver,"ViewCard");
    }

    @Test(priority = 1)
    public void viewCardPinTest()
    {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsButtn();
        cardPage.clickCardMenuActionButton();
        cardPage.clickViewCard();
        Assert.assertEquals(cardPage.getCardPin(),"Your PIN is " + cardData.getCardPin());
        attachScreenshot(DriverManager.driver,"ViewCardPin");
    }

    @AfterMethod
    public void postTestCleanUp()
    {
        log.info("Post-test cleanup: Resetting card settings to default values.");
        cardPage.tryClickFinish();
        homePage.clickLogoutButtn();
    }
}
