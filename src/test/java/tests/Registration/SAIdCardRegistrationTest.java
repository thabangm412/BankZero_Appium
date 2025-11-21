package tests.Registration;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.Registration.RegisterOTP;
import pageObjects.app.Registration.WhoAmIRegistration;
import pageObjects.app.accountsActionMenu.card.MyCardPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import pageObjects.app.login.PairOnDevicePage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SAIdCardRegistrationTest extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(SAPassportRegistrationTest.class);

    @BeforeMethod
    public void preSetUp() {
        AndroidActions androidActions = new AndroidActions(driver);
        androidActions.environmentChange();
    }

    @Test(dataProvider = "getSingleDataSet", priority = 0)
    public void registrationPositiveFlow(HashMap<String, String> input) throws InterruptedException {
        PairOnDevicePage pairOnDevicePage = new PairOnDevicePage(driver);
        RegisterOTP registerOTP = new RegisterOTP(driver);
        MyCardPage myCardPage = new MyCardPage(driver);
        WhoAmIRegistration whoAmIRegistration = new WhoAmIRegistration(driver);
        AndroidActions androidActions = new AndroidActions(driver);
        LoginPage loginPage = new LoginPage(driver);

        // Clear cell phone data...
        androidActions.clearPhoneData(input.get("phoneNumber"));
        androidActions.wait(3);

        // Enter user inputs into the app
        pairOnDevicePage.enterCellNumber(input.get("phoneNumber"));
        pairOnDevicePage.enterIdNumber(input.get("idNumber"));
        pairOnDevicePage.enterPreferredName(input.get("name"));
        pairOnDevicePage.clickSubmit();


        registerOTP.enterOTP(input.get("phoneNumber"),null);

        myCardPage.getCard1stLineField(input.get("name"));
        myCardPage.enterCardPin(Integer.parseInt(input.get("appPin")));
        myCardPage.enterConfirmationPin(Integer.parseInt(input.get("appPin")));
        myCardPage.clickNextButtn();

        whoAmIRegistration.selectTitle(input.get("title"));
        whoAmIRegistration.enterSurname(input.get("surname"));
        whoAmIRegistration.enterAllNames(input.get("allNames"));
        whoAmIRegistration.enterEmail(input.get("email"));
        whoAmIRegistration.enterConfirmationEmail(input.get("email"));
        whoAmIRegistration.clickProofID();

        whoAmIRegistration.confirmDocument(input.get("idType"));
        androidActions.wait(3);
        whoAmIRegistration.clickIdFinishButtn();
        whoAmIRegistration.clickSourceOfFundsButtn();
        androidActions.scrollToTextAndClick("Salary");
        whoAmIRegistration.clickIncomeSubButtn();
        whoAmIRegistration.clickSourceOfWealthButtn();
        driver.findElement(By.xpath("(//android.widget.CheckBox[@resource-id=\"za.co.neolabs.bankzero:id/listItemChecked\"])[1]")).click();
        //androidActions.scrollToTextAndClick("salary");
        whoAmIRegistration.clickIncomeSubButtn();
        whoAmIRegistration.clickNextButtn();

        // Where am I Flow:
        whoAmIRegistration.clickDwelingButtn(input.get("dwelling"));
        whoAmIRegistration.addAddresss(input.get("street"),input.get("city"),input.get("postalCode"));
        androidActions.wait(3);
        whoAmIRegistration.clickCardDelivery();
        androidActions.scrollToTextAndClick2("Courier to residential address (R119 - R149)",driver);
        log.info("Pick up option chosen.");
        androidActions.wait(3);
        whoAmIRegistration.clickNextButtn();
        androidActions.wait(3);

        // I am saving for Flow:
        whoAmIRegistration.enterIamSavingForDetails("My Future Test 1", "1000.00");
        whoAmIRegistration.clickNextButtn();

        // My Safety Flow:
        whoAmIRegistration.enterAppPin(Integer.parseInt(input.get("appPin")));
        whoAmIRegistration.confirmAppPin(Integer.parseInt(input.get("appPin")));
        whoAmIRegistration.enterRecoveryDetails(input.get("recoveryCellNumber"),input.get("recoveryCellNumber"),input.get("recoveryEmail"));
        whoAmIRegistration.clickNextButtn();

        // Happy page Flow:
        whoAmIRegistration.clickCheckBox2();
        whoAmIRegistration.clickConfirmButtn();
        Thread.sleep(3000);
//        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Thank you",driver);
//        whoAmIRegistration.clickNextButtn();
//        Assert.assertTrue(loginPage.loginPageConfirm());
//        log.info("Login page displayed.");
        String status = whoAmIRegistration.getStatus();
        try {
            Assert.assertEquals(status, "Thank you");
            log.info("Registration successful matched status: {}",status);
        } catch (AssertionError | Exception e) {
            log.warn("Registration failed with status: {}", status);
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;
        }finally {
            whoAmIRegistration.clickNextButtn();
            log.info("Login page displayed.");
        }
    }

    @Test(dataProvider = "getSingleDataSet", priority = 1)
    public void loginTest(HashMap<String, String> input) throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);

        loginPage.loginAccount(input.get("name"));
        loginPage.enterLoginPin(Integer.parseInt(input.get("appPin")));
        loginPage.loginAccount(input.get("name"));

        try {
            Assert.assertEquals(homePage.getHomePageConfirm(), "Accounts");
            log.info("New User logged in, Accounts screen displayed");
            Thread.sleep(3000);
        } catch (AssertionError | Exception e) {
            log.warn("Registration failed with status");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;
        }finally {
            homePage.clickLogoutButtn();
        }
    }

//    public void ficaVerification()
//    {
//
//    }

    @DataProvider
    public Object[] [] getSingleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//customerRegistrationData.json");
        return new Object[][]{{data.get(3)}};
    }
}
