package tests.NewAccounts.SevenDaysNotice;

import DbQueries.EmailsConfig;
import com.jcraft.jsch.JSchException;
import factory.TransferDataFactory;
import models.TransferData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.statements.StatementsAndLettersPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.DriverManager;

public class StatementsAndLettersFrom7DaysNoticeTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(StatementsAndLettersFrom7DaysNoticeTests.class);
    private LoginPage loginPage;
    private HomePage homePage;
    private AccountMenuActions accountMenuActions;
    private TransferData data;
    private StatementsAndLettersPage statementsAndLettersPage;

    @BeforeMethod
    public void preSetUp() throws JSchException {
        loginPage = new LoginPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        data = TransferDataFactory.validTransfer();
        statementsAndLettersPage = new StatementsAndLettersPage(DriverManager.driver);

        EmailsConfig.enableEmails();

        log.debug("Page objects and androidActions initialized");
    }

    @Test(priority = 0)
    public void downloadAccountStatementsFrom7DaysNotice() {
        loginPage.loginWithRetry(
                data.getUser().getProfileName(),
                data.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("7 Days Notice");
        statementsAndLettersPage.clickAccountStatements();
        statementsAndLettersPage.getAccountStatements(2);
        statementsAndLettersPage.clickEmailButton();
        Assert.assertEquals(statementsAndLettersPage.getDocumentRequestStatus(),"Email sent successful. Please check your inbox");
        attachScreenshot(DriverManager.driver,"Account Statements Email Sent");
        log.info("Account Statements letter email sent successfully and verified the success message.");

    }

    @Test(priority = 1)
    public void downloadIt3bFrom7DaysNotice() {
        loginPage.loginWithRetry(
                data.getUser().getProfileName(),
                data.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("7 Days Notice");
        statementsAndLettersPage.clickAccountStatements();
        statementsAndLettersPage.getIt3bLetter(1);
        statementsAndLettersPage.clickEmailButton();
        Assert.assertEquals(statementsAndLettersPage.getDocumentRequestStatus(),"Email sent successful. Please check your inbox");
        attachScreenshot(DriverManager.driver,"Account Statements Email Sent");
        log.info("Account Statements letter email sent successfully and verified the success message.");

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
