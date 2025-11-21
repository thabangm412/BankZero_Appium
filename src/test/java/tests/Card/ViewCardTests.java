package tests.Card;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class ViewCardTests extends BaseTestsConfig {

    @Test(dataProvider = "getMultipleDataSet",priority = 0)
    public void viewCardTest(HashMap<String, String> input)
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
        cardPage.clickViewCard();

        try {
            Assert.assertTrue(cardPage.getCardDisplayed());
            log.info("Card is displayed.");
        } catch (Exception| AssertionError e) {
            log.warn("Test failed due to: {}",e);
            Assert.fail("Test failed");
            throw new RuntimeException(e);
        }
        cardPage.clickFinish2();
        homePage.clickLogoutButtn();
    }

    @Test(dataProvider = "getMultipleDataSet",priority = 1)
    public void viewCardPinTest(HashMap<String, String> input)
    {
        LoginPage loginPage = new LoginPage(driver);
        MyCardPage cardPage = new MyCardPage(driver);
        HomePage homePage = new HomePage(driver);
        AccountMenuActions accountMenuActions = new AccountMenuActions(driver);
        AndroidActions androidActions = new AndroidActions(driver);

        String name = input.get("profileName");
        String appPin = input.get("loginPin");
        String cardPin = input.get("cardPin");

        loginPage.loginWithRetry(name,appPin,2);

        accountMenuActions.clickAccountMenuActionsButtn();
        cardPage.clickCardMenuActionButton();
        cardPage.clickViewCard();
        String actualTxt = cardPage.getCardPin();

        try {
            Assert.assertEquals(actualTxt,"Your PIN is " + cardPin);
            log.info("Card is displayed.");
        } catch (Exception| AssertionError e) {
            log.warn("Test failed due to: {}",e);
            Assert.fail("Test failed");
            throw new RuntimeException(e);
        }

        cardPage.clickFinish2();
        homePage.clickLogoutButtn();
    }
//    @AfterMethod
//    public void cleanUp()
//    {
//        MyCardPage cardPage = new MyCardPage(driver);
//        HomePage homePage = new HomePage(driver);
//        cardPage.clickFinish2();
//        homePage.clickLogoutButtn();
//    }
    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//cardData.json");
        return new Object[][]{{data.getFirst()}};
    }

}
