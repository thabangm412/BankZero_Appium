package tests.Child;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.Registration.RegisterOTP;
import pageObjects.app.Registration.WhoAmIRegistration;
import pageObjects.app.accountsActionMenu.card.MyCardPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.addAccount.AddAccountPage;
import pageObjects.app.addAccount.ChildAccPage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ChildAccountRegistrationTest extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(ChildAccountRegistrationTest.class);

//    @BeforeMethod
//    public void preSetUp()
//    {
//        AndroidActions androidActions = new AndroidActions(driver);
//        androidActions.environmentChange();
//    }

    @Test(dataProvider = "getMultipleDataSet")
    public void childInviteTest(HashMap<String, String> input) throws IOException {

        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        AddAccountPage addAccountPage = new AddAccountPage(driver);
        ChildAccPage childAccPage = new ChildAccPage(driver);
        AndroidActions androidActions = new AndroidActions(driver);

//        Properties properties = new Properties();
//        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//java//resources//data.properties");
//        properties.load(fis);


        String name = input.get("name");
        String profileName = input.get("profileName");
        String loginPin = input.get("loginPin");
        String id = input.get("id");
        String allNames = input.get("allNames");
        String nationality = input.get("nationality");
        String idType = input.get("idType");

        androidActions.clearPhoneData(input.get("phoneNumber"));
        androidActions.wait(3);

        loginPage.loginWithRetry(profileName,loginPin,2);

        addAccountPage.clickAddAccButtn();
        addAccountPage.clickChildButtn();

        childAccPage.enterChildDetails(id,name,allNames,nationality);
        childAccPage.addProofOfId(idType);
        childAccPage.clickConfirm();
        childAccPage.clickSubmit();

        try {
            String status = childAccPage.getConfirmationText();

            try {
                Assert.assertEquals(status,"Thank you");
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

    @Test(dataProvider = "getMultipleDataSet")
    public void childRegistrationTest(HashMap<String, String> input) throws InterruptedException {
        ChildAccPage childAccPage = new ChildAccPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        RegisterOTP registerOTP = new RegisterOTP(driver);
        AndroidActions androidActions = new AndroidActions(driver);
        MyCardPage myCardPage = new MyCardPage(driver);
        WhoAmIRegistration whoAmIRegistration = new WhoAmIRegistration(driver);


        String idNumber = input.get("id");
        String cellNumber = input.get("phoneNumber");
        String prefName = input.get("allNames");
        String surname = input.get("surname");
        String email = input.get("email");

        //androidActions.clearPhoneData(cellNumber);
        androidActions.wait(3);

        loginPage.clickAddAccount();
        childAccPage.enterCellNumber(cellNumber);
        childAccPage.enterIdNumber(idNumber);
        childAccPage.enterPreferredName(prefName);
        childAccPage.clickSubmitReg();

        registerOTP.enterOTP(cellNumber,null);
        registerOTP.clickSubmitButton();

        myCardPage.getCard1stLineField(prefName);
        myCardPage.enterCardPin(Integer.parseInt(input.get("childPin")));
        myCardPage.enterConfirmationPin(Integer.parseInt(input.get("childPin")));
        myCardPage.clickNextButtn();

        //childAccPage.enterNames(surname,prefName);
        childAccPage.enterEmailDetails(email);
        childAccPage.enterSourceFunds();
        childAccPage.clickNext();

        childAccPage.cardDelivery();
        childAccPage.clickNext();
        whoAmIRegistration.enterIamSavingForDetails("Child Future", "1000.00");
        whoAmIRegistration.clickNextButtn();

        whoAmIRegistration.enterAppPin(Integer.parseInt(input.get("childPin")));
        whoAmIRegistration.confirmAppPin(Integer.parseInt(input.get("childPin")));
        whoAmIRegistration.enterRecoveryDetails(input.get("recoveryNumber"),input.get("recoveryNumber"),input.get("recoveryEmail"));
        whoAmIRegistration.clickNextButtn();

        whoAmIRegistration.clickCheckBox2();
        whoAmIRegistration.clickConfirmButtn();
        Thread.sleep(3000);

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

    @Test(dataProvider = "getMultipleDataSet")
    public void childLoginTest(HashMap<String, String> input) throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);

        String name = input.get("allNames");
        String appPin = input.get("childPin");

        loginPage.loginWithRetry(name,appPin,2);


        Assert.assertEquals(homePage.getHomePageConfirm(), "Accounts");
        log.info("User logged in, Accounts screen displayed");
        Thread.sleep(3000);
    }

    @Test
    public void childLogoutTest()
    {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        homePage.clickLogoutButtn();
        Assert.assertEquals(loginPage.getLoginPageConfirm(),"Login");
    }

    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        List<HashMap<String, String>> data = getJsonData(System.getProperty("user.dir") + "//src//test//java//testData//childRegistrationData.json");
        return new Object[][]{{data.getFirst()}};
    }

    @AfterMethod
    public void cleanUp() {
        try {
            ChildAccPage childAccPage = new ChildAccPage(driver);
            HomePage homePage = new HomePage(driver);
            childAccPage.clickFinishButton();
            homePage.clickLogoutButtn();

        } catch (Exception e) {
            log.error("Cleanup failed: ", e);
        }
    }
}
