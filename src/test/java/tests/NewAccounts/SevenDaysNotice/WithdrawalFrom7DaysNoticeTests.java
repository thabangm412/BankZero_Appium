package tests.NewAccounts.SevenDaysNotice;

import com.jcraft.jsch.JSchException;
import factory.TransferDataFactory;
import models.TransferData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.newAccounts.WithdrawalPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import tests.NewAccounts.SavingsAccount.WithdrawalFromSavingsTests;
import utils.DriverManager;

public class WithdrawalFrom7DaysNoticeTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(WithdrawalFromSavingsTests.class);
    private LoginPage loginPage;
    private HomePage homePage;
    private AccountMenuActions accountMenuActions;
    private TransferData data;
    private WithdrawalPage withdrawalPage;

    @BeforeMethod
    public void preSetUp() throws JSchException {
        loginPage = new LoginPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        data = TransferDataFactory.validTransfer();

        withdrawalPage = new WithdrawalPage(DriverManager.driver);

        log.debug("Page objects and androidActions initialized");
    }

    @Test(priority = 0)
    public void withdrawalFrom7DaysNotice() {
        loginPage.loginWithRetry(
                data.getUser().getProfileName(),
                data.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("7 Days Notice");
        withdrawalPage.clickWithdrawActionMenuButton();
        withdrawalPage.enterAmountDetails();
        withdrawalPage.clickWithdrawButton();
        withdrawalPage.clickConfirmButton();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(withdrawalPage.isWithdrawalSuccessMessageDisplayed(), "Withdrawal success message should be displayed");
        softAssert.assertEquals(withdrawalPage.getTransactionMessage(),withdrawalPage.getExpectedAvailabilityMessage(7));
        softAssert.assertAll();
        attachScreenshot(DriverManager.driver, "Withdrawal from 7 Days Notice");
        withdrawalPage.clickFinishButton();
        homePage.clickLogoutButtn();
    }

    @Test(priority = 1)
    public void immediateWithdrawalFrom7DaysNotice()
    {
        loginPage.loginWithRetry(
                data.getUser().getProfileName(),
                data.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("7 Days Notice");
        withdrawalPage.clickWithdrawActionMenuButton();
        withdrawalPage.enterAmountDetails();
        withdrawalPage.clickCheckBox();
        withdrawalPage.clickWithdrawButton();
        withdrawalPage.clickConfirmButton();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(withdrawalPage.isWithdrawalSuccessMessageDisplayed(), "Withdrawal success message should be displayed");
        softAssert.assertEquals(withdrawalPage.getTransactionMessage(),withdrawalPage.getExpectedAvailabilityMessage(1));
        softAssert.assertAll();
        attachScreenshot(DriverManager.driver, "Immediate Withdrawal from 7 Days Notice");
        withdrawalPage.clickFinishButton();
        homePage.clickLogoutButtn();
    }

}
