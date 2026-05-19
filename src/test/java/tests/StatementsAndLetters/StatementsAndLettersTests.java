package tests.StatementsAndLetters;

import DbQueries.EmailsConfig;
import com.jcraft.jsch.JSchException;
import factory.TransferDataFactory;
import models.TransferData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.statements.StatementsAndLettersPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.DriverManager;

public class StatementsAndLettersTests extends BaseTestsConfig {
    private static final Logger log = LoggerFactory.getLogger(StatementsAndLettersTests.class);
    private StatementsAndLettersPage statementsAndLettersPage;
    private LoginPage loginPage;
    private TransferData data;
    private AccountMenuActions accountMenuActions;
    private HomePage homePage;

    @BeforeClass
    public void preSetUp() throws JSchException {
         statementsAndLettersPage = new StatementsAndLettersPage(DriverManager.driver);
         loginPage = new LoginPage(DriverManager.driver);
         homePage = new HomePage(DriverManager.driver);
         accountMenuActions = new AccountMenuActions(DriverManager.driver);
         data = TransferDataFactory.validTransfer();

         EmailsConfig.enableEmails();

        log.debug("Page objects and androidActions initialized");
    }

    @Test(priority = 0)
    public void accountConfirmationLetterEmailTest(){

        loginPage.loginWithRetry(
                data.getUser().getProfileName(),
                data.getUser().getLoginPin(),
                2
        );
        accountMenuActions.clickAccountMenuActionsButtn();
        statementsAndLettersPage.clickAccountStatements();
        statementsAndLettersPage.getAccountConfirmationLetter();

        statementsAndLettersPage.clickEmailButton();
        try {
            Assert.assertEquals(statementsAndLettersPage.getDocumentRequestStatus(),"Email sent successful. Please check your inbox");
            attachScreenshot(DriverManager.driver,"Account Confirmation Letter Email Sent");
            log.info("Account confirmation letter email sent successfully and verified the success message.");

        }catch (Exception e){
            log.error("Error while sending account confirmation letter email: " + e.getMessage());
        }

    }

    @Test(priority = 1)
    public void accountStatementsEmailTest(){

        loginPage.loginWithRetry(
                data.getUser().getProfileName(),
                data.getUser().getLoginPin(),
                2
        );
        accountMenuActions.clickAccountMenuActionsButtn();
        statementsAndLettersPage.clickAccountStatements();
        statementsAndLettersPage.getAccountStatements(2);
        statementsAndLettersPage.clickEmailButton();
        try {
            Assert.assertEquals(statementsAndLettersPage.getDocumentRequestStatus(),"Email sent successful. Please check your inbox");
            attachScreenshot(DriverManager.driver,"Account Statements Email Sent");
            log.info("Account Statements letter email sent successfully and verified the success message.");

        }catch (Exception e){
            log.error("Error while sending Account Statements letter email: " + e.getMessage());
        }

    }

    @Test(priority = 2)
    public void salarySwitchLetterEmailTest(){

        loginPage.loginWithRetry(
                data.getUser().getProfileName(),
                data.getUser().getLoginPin(),
                2
        );
        accountMenuActions.clickAccountMenuActionsButtn();
        statementsAndLettersPage.clickAccountStatements();
        statementsAndLettersPage.getSalarySwitchLetter();
        statementsAndLettersPage.clickEmailButton();
        try {
            Assert.assertEquals(statementsAndLettersPage.getDocumentRequestStatus(),"Email sent successful. Please check your inbox");
            attachScreenshot(DriverManager.driver,"Salary Switch Letter Email Sent");
            log.info("Salary switch letter email sent successfully and verified the success message.");

        }catch (Exception e){
            log.error("Error while sending Salary switch letter email: " + e.getMessage());
        }

    }

    @Test(priority = 3)
    public void welcomeLetterEmailTest(){

        loginPage.loginWithRetry(
                data.getUser().getProfileName(),
                data.getUser().getLoginPin(),
                2
        );
        accountMenuActions.clickAccountMenuActionsButtn();
        statementsAndLettersPage.clickAccountStatements();
        statementsAndLettersPage.getWelcomeLetter();
        statementsAndLettersPage.clickEmailButton();
        try {
            Assert.assertEquals(statementsAndLettersPage.getDocumentRequestStatus(),"Email sent successful. Please check your inbox");
            attachScreenshot(DriverManager.driver,"Welcome Letter Email Sent");
            log.info("Welcome letter email sent successfully and verified the success message.");

        }catch (Exception e){
            log.error("Error while sending welcome letter email: " + e.getMessage());
        }
    }

    @Test(priority = 4)
    public void paymentRecipientsEmailTest(){

        loginPage.loginWithRetry(
                data.getUser().getProfileName(),
                data.getUser().getLoginPin(),
                2
        );
        accountMenuActions.clickAccountMenuActionsButtn();
        statementsAndLettersPage.clickAccountStatements();
        statementsAndLettersPage.getWelcomeLetter();
        statementsAndLettersPage.clickEmailButton();
        try {
            Assert.assertEquals(statementsAndLettersPage.getDocumentRequestStatus(),"Email sent successful. Please check your inbox");
            attachScreenshot(DriverManager.driver,"Payment recipient Letter Email Sent");
            log.info("Payment recipient letter email sent successfully and verified the success message.");

        }catch (Exception e){
            log.error("Error while sending payment recipient letter email: " + e.getMessage());
        }

    }

    @AfterMethod
    public void finshProcess() {
        statementsAndLettersPage.clickOkButton();
        statementsAndLettersPage.clickFinishButton();
        homePage.clickLogoutButtn();
    }

    @AfterClass
    public void tearDown() throws JSchException {
         EmailsConfig.disableEmails();
    }
}
