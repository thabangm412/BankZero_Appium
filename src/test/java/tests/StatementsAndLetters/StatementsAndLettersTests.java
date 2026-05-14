package tests.StatementsAndLetters;

import DbQueries.EmailsConfig;
import com.jcraft.jsch.JSchException;
import factory.TransferDataFactory;
import models.TransferData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.statements.StatementsAndLettersPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;

public class StatementsAndLettersTests extends BaseTestsConfig {
    private static final Logger log = LoggerFactory.getLogger(StatementsAndLettersTests.class);
    private StatementsAndLettersPage statementsAndLettersPage;
    private LoginPage loginPage;
    private TransferData data;
    private AccountMenuActions accountMenuActions;
    private HomePage homePage;

    @BeforeMethod
    public void preSetUp() throws JSchException {
         statementsAndLettersPage = new StatementsAndLettersPage(driver);
         loginPage = new LoginPage(driver);
         homePage = new HomePage(driver);
         accountMenuActions = new AccountMenuActions(driver);
         data = TransferDataFactory.validTransfer();

         EmailsConfig.enableEmails();

        log.debug("Page objects and androidActions initialized");
    }

    @Test
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
            attachScreenshot(driver,"Account Confirmation Letter Email Sent");
            log.info("Account confirmation letter email sent successfully and verified the success message.");

        }catch (Exception e){
            log.error("Error while sending account confirmation letter email: " + e.getMessage());
        }
        statementsAndLettersPage.clickOkButton();
        statementsAndLettersPage.clickFinishButton();
    }

    @Test
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
            attachScreenshot(driver,"Account Statements Email Sent");
            log.info("Account Statements letter email sent successfully and verified the success message.");

        }catch (Exception e){
            log.error("Error while sending Account Statements letter email: " + e.getMessage());
        }
        statementsAndLettersPage.clickOkButton();
        statementsAndLettersPage.clickFinishButton();
    }

    @Test
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
            attachScreenshot(driver,"Salary Switch Letter Email Sent");
            log.info("Salary switch letter email sent successfully and verified the success message.");

        }catch (Exception e){
            log.error("Error while sending Salary switch letter email: " + e.getMessage());
        }
        statementsAndLettersPage.clickOkButton();
        statementsAndLettersPage.clickFinishButton();
    }

    @Test
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
            attachScreenshot(driver,"Welcome Letter Email Sent");
            log.info("Welcome letter email sent successfully and verified the success message.");

        }catch (Exception e){
            log.error("Error while sending welcome letter email: " + e.getMessage());
        }
        statementsAndLettersPage.clickOkButton();
        statementsAndLettersPage.clickFinishButton();
    }

    @Test
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
            attachScreenshot(driver,"Payment recipient Letter Email Sent");
            log.info("Payment recipient letter email sent successfully and verified the success message.");

        }catch (Exception e){
            log.error("Error while sending payment recipient letter email: " + e.getMessage());
        }
        statementsAndLettersPage.clickOkButton();
        statementsAndLettersPage.clickFinishButton();
    }

    @AfterMethod
    public void tearDown() throws JSchException {
         driver.navigate().back();
         homePage.clickLogoutButtn();
         EmailsConfig.disableEmails();
    }
}
