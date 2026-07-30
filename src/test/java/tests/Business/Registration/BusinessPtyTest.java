package tests.Business.Registration;

import DbQueries.DbConfig;
import com.jcraft.jsch.JSchException;
import factory.BusinessDataFactory;
import models.BusinessRegData;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.addAccount.AddAccountPage;
import pageObjects.app.business.BusinessPage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;
import utils.DriverManager;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessPtyTest extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(BusinessPtyTest.class);
    private LoginPage loginPage;
    private AddAccountPage addAccountPage;
    private BusinessPage businessPage;
    private AndroidActions androidActions;
    private BusinessRegData businessDataFactory;
    private HomePage homePage;

    @BeforeMethod
    public void setUpPages() throws JSchException {
        // initialize page objects once per test method
        log.debug("Initializing page objects for test.");
        loginPage = new LoginPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        addAccountPage = new AddAccountPage(DriverManager.driver);
        businessPage = new BusinessPage(DriverManager.driver);
        androidActions = new AndroidActions(DriverManager.driver);
        businessDataFactory = BusinessDataFactory.validBusinessRegData();
//        DbConfig.customerExists(businessDataFactory.getRegistrationNo());
    }

    @Test
    public void PtyRegistration() throws JSchException, InterruptedException {

        boolean customerExists =
                DbConfig.customerExists(businessDataFactory.getRegistrationNo());

        if (customerExists) {
            log.warn("Customer already exists. Skipping test execution.");

            throw new SkipException(
                    "Test skipped because customer already exists in DB: "
                            + businessDataFactory.getRegistrationNo()
            );
        }


        loginPage.loginWithRetry(
                businessDataFactory.getUser().getProfileName(),
                businessDataFactory.getUser().getLoginPin(),
                2
        );

        addAccountPage.clickAddAccButtn();
        addAccountPage.newBusinessButtn();
        businessPage.chooseBusiness(businessDataFactory.getBusinessType());
        businessPage.ptyBusinessDetails(businessDataFactory.getTradingName(), businessDataFactory.getRegisteredName(), businessDataFactory.getRegistrationNo());
        businessPage.sicDetails(businessDataFactory.getSicGroup(), businessDataFactory.getSicIndustry());
        businessPage.chooseSourceOfFundsAndWealth();
        businessPage.enterNotifyEmail(businessDataFactory.getNotifyEmail());
        businessPage.selectFromAccount(businessDataFactory.getFundsAccount());
        businessPage.clickNextButton();
        businessPage.enterRegisteredAddress(businessDataFactory.getStreet(), businessDataFactory.getCity(),businessDataFactory.getPostalCode());
        businessPage.clickNextButton();
        businessPage.selectCardOptions(businessDataFactory.getTradingName());
        businessPage.clickNextButton();
        businessPage.enterCardPin(businessDataFactory.getCardPin());
        businessPage.clickNextButton();
        businessPage.selectOwnersAndOfficials();
        businessPage.clickNextButton();
        businessPage.verifyTermsAndConditions();
        businessPage.clickNextButton();
        assertTextPresentExact(businessDataFactory.getSuccessMsg());
        attachScreenshot(DriverManager.driver,"Business_Registration Completed");

    }

    @AfterMethod
    public void cleanUp() {
        try {
            businessPage.clickFinish();
            homePage.clickLogoutButtn();
            log.info("Logged out successfully during cleanup.");
        } catch (Exception e) {
            log.error("Cleanup failed: ", e);
        }
    }

    private void validateInputKeys(Map<String, String> input, String... requiredKeys) {
        if (input == null) {
            log.error("Input map is null");
            throw new IllegalArgumentException("Input data cannot be null");
        }
        StringBuilder missing = new StringBuilder();
        Arrays.stream(requiredKeys).forEach(k -> {
            if (input.get(k) == null || input.get(k).trim().isEmpty()) {
                if (missing.length() > 0) missing.append(", ");
                missing.append(k);
            }
        });
        if (missing.length() > 0) {
            log.error("Missing required test input keys: {}", missing);
            throw new IllegalArgumentException("Missing required test input keys: " + missing);
        }
        log.debug("All required keys present in input");
    }


    private void assertTextPresentExact(String exactText) {
        By xpath = By.xpath("//android.widget.TextView[@text=\"" + exactText + "\"]");
        WebDriverWait wait = new WebDriverWait(DriverManager.driver, Duration.ofSeconds(15));
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(xpath));
            Assert.assertTrue(el.isDisplayed(), "Expected text not visible: " + exactText);
            log.info("Found expected text: {}", maskForLog(exactText));
        } catch (TimeoutException e) {
            log.error("Expected text not found within timeout: {}", exactText);
            Assert.fail("Expected text not found: " + exactText);
        }
    }

    private String maskForLog(String s) {
        if (s == null) return "";
        if (s.length() <= 20) return s;
        return s.substring(0, 20) + "...";
    }
}
