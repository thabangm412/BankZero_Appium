package tests.Child;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.Registration.RegisterOTP;
import pageObjects.app.Registration.WhoAmIRegistration;
import pageObjects.app.accountsActionMenu.card.MyCardPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.addAccount.AddAccountPage;
import pageObjects.app.addAccount.ChildAccPage;
import pageObjects.app.login.LoginPage;
import pageObjects.app.login.PairOnDevicePage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;
import utils.DriverManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ChildAccountRegistrationTest extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(ChildAccountRegistrationTest.class);

    private AndroidActions androidActions;
    private LoginPage loginPage;
    private HomePage homePage;
    private AddAccountPage addAccountPage;
    private ChildAccPage childAccPage;
    private RegisterOTP registerOTP;
    private MyCardPage myCardPage;
    private WhoAmIRegistration whoAmIRegistration;
    private PairOnDevicePage pairOnDevicePage;

    @BeforeMethod
    public void preSetUp()
    {
        androidActions = new AndroidActions(DriverManager.driver);
        androidActions.environmentChange();

        loginPage = new LoginPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        addAccountPage = new AddAccountPage(DriverManager.driver);
        childAccPage = new ChildAccPage(DriverManager.driver);
        registerOTP = new RegisterOTP(DriverManager.driver);
        myCardPage = new MyCardPage(DriverManager.driver);
        whoAmIRegistration = new WhoAmIRegistration(DriverManager.driver);
        pairOnDevicePage = new PairOnDevicePage(DriverManager.driver);
    }

    @Test(dataProvider = "getMultipleDataSet", priority = 0)
    public void childInviteTest(HashMap<String, String> input) throws IOException, InterruptedException {

        validateInput(input,
                "name", "profileName", "loginPin",
                "id", "allNames", "phoneNumber", "nationality", "idType");

        androidActions.clearPhoneData(input.get("phoneNumber"));
        androidActions.wait(3);

        loginPage.loginWithRetry(input.get("profileName"),input.get("loginPin"),2);

        addAccountPage.clickAddAccButtn();
        addAccountPage.clickChildButtn();

        childAccPage.enterChildDetails(input.get("id"),input.get("name"),input.get("allNames"),input.get("nationality"));
        childAccPage.addProofOfId(input.get("idType"));
        childAccPage.clickConfirm();
        childAccPage.clickSubmit();
        Thread.sleep(3000);

        try {
            String status = childAccPage.getConfirmationText();

            try {
                Assert.assertEquals(status,"Thank you");
                attachScreenshot(DriverManager.driver,"Child Account Registration Confirmation");
                log.info("Registration status: {}",status);
            } catch (AssertionError e) {
                log.warn("Registration failed with status: {}", status);
                throw e;
            }
        } catch (Exception e) {
            log.error("Exception occurred during registraion handling: ", e);
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }

    }

    @Test(dataProvider = "getMultipleDataSet", priority = 1)
    public void childRegistrationTest(HashMap<String, String> input) throws InterruptedException {

     validateInput(input,
                "name", "profileName", "loginPin",
                 "id", "allNames", "phoneNumber", "surname", "email", "childPin",
                 "recoveryNumber", "recoveryEmail"
        );

        androidActions.clearPhoneData(input.get("phoneNumber"));
        androidActions.wait(3);

        pairOnDevicePage.addProfile();
        pairOnDevicePage.partialRegistrationCheck();
        childAccPage.enterCellNumber(input.get("phoneNumber"));
        childAccPage.enterIdNumber(input.get("id"));
        childAccPage.enterPreferredName(input.get("allNames"));
        childAccPage.clickSubmitReg();

        registerOTP.enterOTP(input.get("phoneNumber"),null);
        registerOTP.clickSubmitButton();

        myCardPage.getCard1stLineField(input.get("allNames"));
        myCardPage.enterCardPin(Integer.parseInt(input.get("childPin")));
        myCardPage.enterConfirmationPin(Integer.parseInt(input.get("childPin")));
        myCardPage.clickNextButtn();

        //childAccPage.enterNames(surname,prefName);
        childAccPage.enterEmailDetails(input.get("email"));
        childAccPage.enterSourceFunds();
        childAccPage.clickNext();

        childAccPage.cardDelivery();
        childAccPage.addAddresss(input.get("street"),input.get("city"),input.get("postalCode"));
        attachScreenshot(DriverManager.driver,"Card Delivery Details Entered");
        childAccPage.clickNext();
        whoAmIRegistration.enterIamSavingForDetails("Child Future", "1000.00");
        whoAmIRegistration.clickNextButtn();

        whoAmIRegistration.enterAppPin(Integer.parseInt(input.get("childPin")));
        whoAmIRegistration.confirmAppPin(Integer.parseInt(input.get("childPin")));
        whoAmIRegistration.enterRecoveryDetails(input.get("recoveryNumber"),input.get("recoveryNumber"),input.get("recoveryEmail"));
        attachScreenshot(DriverManager.driver,"Recovery Details Entered");
        whoAmIRegistration.clickNextButtn();

        whoAmIRegistration.clickCheckBox2();
        whoAmIRegistration.clickConfirmButtn();
        Thread.sleep(3000);

        String status = whoAmIRegistration.getStatus();
        try {
            Assert.assertEquals(status, "Thank you");
            attachScreenshot(DriverManager.driver,"Child Registration Success");
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

    @Test(dataProvider = "getMultipleDataSet", priority = 2)
    public void childLoginTest(HashMap<String, String> input) throws InterruptedException {

       validateInput(input,
                "allNames", "childPin"
        );

        loginPage.loginWithRetry(input.get("allNames"),input.get("childPin"),2);

        try {
            Assert.assertEquals(homePage.getHomePageConfirm(), "Accounts");
            attachScreenshot(DriverManager.driver, "Child Login Success");
            log.info("User logged in, Accounts screen displayed");
        } catch (AssertionError | Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
            log.warn("Child login failed with status: {}", homePage.getHomePageConfirm());
            throw e;
        }finally {
            homePage.clickLogoutButtn();
            attachScreenshot(DriverManager.driver, "Child Logout Success");
             log.info("User logged out, Login screen displayed");
        }
    }

//    @Test
//    public void childLogoutTest()
//    {
//
//        homePage.clickLogoutButtn();
//        Assert.assertEquals(loginPage.getLoginPageConfirm(),"Login");
//    }

    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//childRegistrationData.json");
        return new Object[][]{{data.getFirst()}};
    }

    @AfterMethod
    public void cleanUp() {
        try {
            ChildAccPage childAccPage = new ChildAccPage(DriverManager.driver);
            HomePage homePage = new HomePage(DriverManager.driver);
            childAccPage.clickFinishButton();
            homePage.clickLogoutButtn();

        } catch (Exception e) {
            log.error("Cleanup failed: ", e);
        }
    }
}
