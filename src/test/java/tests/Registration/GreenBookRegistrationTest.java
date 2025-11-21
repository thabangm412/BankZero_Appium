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

import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GreenBookRegistrationTest extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(GreenBookRegistrationTest.class);

    private AndroidActions androidActions;
    private PairOnDevicePage pairOnDevicePage;
    private RegisterOTP registerOTP;
    private MyCardPage myCardPage;
    private WhoAmIRegistration whoAmIRegistration;
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void preSetUp() {
        androidActions  = new AndroidActions(driver);
        androidActions.environmentChange();

        // initialize page objects once per test
        pairOnDevicePage = new PairOnDevicePage(driver);
        registerOTP = new RegisterOTP(driver);
        myCardPage = new MyCardPage(driver);
        whoAmIRegistration = new WhoAmIRegistration(driver);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);

        log.debug("Page objects and androidActions initialized");
    }

    @Test(dataProvider = "getSingleDataSet", priority = 0)
    public void registrationPositiveFlow(HashMap<String, String> input) throws InterruptedException {

        validateInput(input,
                "phoneNumber", "idNumber", "name", "appPin",
                "title", "surname", "allNames", "email",
                "dwelling", "street", "city", "postalCode",
                "recoveryCellNumber", "recoveryEmail");

        int appPin = Integer.parseInt(input.get("appPin"));

        log.info("Starting registration flow for {}", maskForLog(input.get("name")));

        // Clear cell phone data...
        androidActions.clearPhoneData(input.get("phoneNumber"));
        androidActions.wait(3);

        // Enter user inputs into the app
        pairOnDevicePage.enterCellNumber(input.get("phoneNumber"));
        pairOnDevicePage.enterIdNumber(input.get("idNumber"));
        pairOnDevicePage.enterPreferredName(input.get("name"));
        pairOnDevicePage.clickSubmit();

        registerOTP.enterOTP(input.get("phoneNumber"),null);
        myCardPage.confirmingBiometrics();

        myCardPage.getCard1stLineField(input.get("name"));
        myCardPage.enterCardPin(appPin);
        myCardPage.enterConfirmationPin(appPin);
        myCardPage.clickNextButtn();

        whoAmIRegistration.selectTitle(input.get("title"));
        whoAmIRegistration.enterSurname(input.get("surname"));
        whoAmIRegistration.enterAllNames(input.get("allNames"));
        whoAmIRegistration.enterEmail(input.get("email"));
        whoAmIRegistration.enterConfirmationEmail(input.get("email"));
        whoAmIRegistration.clickProofID();

        whoAmIRegistration.confirmDocument(input.get("idType"));
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
        whoAmIRegistration.enterAppPin(appPin);
        whoAmIRegistration.confirmAppPin(appPin);
        whoAmIRegistration.enterRecoveryDetails(input.get("recoveryCellNumber"),input.get("recoveryCellNumber"),input.get("recoveryEmail"));
        whoAmIRegistration.clickNextButtn();

        // Happy page Flow:
        whoAmIRegistration.clickCheckBox2();
        whoAmIRegistration.clickNextButtn();
        androidActions.wait(3);

        String status = whoAmIRegistration.getStatus();
        try {
            Assert.assertEquals(status, "Thank you");
            log.info("Transactional successful matched status: {}",status);
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

        validateInput(input, "name", "appPin");
        int appPin = Integer.parseInt(input.get("appPin"));

        loginPage.loginAccount(input.get("name"));
        loginPage.enterLoginPin(appPin);


        try {
            Assert.assertEquals(homePage.getHomePageConfirm(), "Accounts");
            log.info("New User logged in, Accounts screen displayed");
            androidActions.wait(3);
        } catch (AssertionError | Exception e) {
            log.warn("Registration failed with status");
            Assert.fail("Test failed due to exception: " + e.getMessage());
            throw e;
        }finally {
            homePage.clickLogoutButtn();
        }
    }

    @DataProvider
    public Object[] [] getSingleDataSet() throws IOException {
        String path = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "java"
                + File.separator + "testData" + File.separator + "customerRegistrationData.json";
        log.debug("Loading test data from: {}", path);
        List<HashMap<String, String>> data = getJsonData(path);
        if (data == null || data.isEmpty()) {
            throw new IllegalStateException("Test data not found: " + path);
        }
        // return second entry as original code did
        return new Object[][]{{data.get(1)}};
    }

    private void validateInput(HashMap<String, String> input, String... required) {
        if (input == null) throw new IllegalArgumentException("Input map is null");
        StringBuilder missing = new StringBuilder();
        for (String k : required) {
            if (input.get(k) == null || input.get(k).trim().isEmpty()) {
                if (missing.length() > 0) missing.append(", ");
                missing.append(k);
            }
        }
        if (missing.length() > 0) {
            log.error("Missing required keys: {}", missing.toString());
            throw new IllegalArgumentException("Missing required keys: " + missing.toString());
        }
    }

    private String maskForLog(String s) {
        if (s == null) return "";
        return s.length() <= 12 ? s : s.substring(0, 6) + "..." + s.substring(s.length() - 3);
    }
}
