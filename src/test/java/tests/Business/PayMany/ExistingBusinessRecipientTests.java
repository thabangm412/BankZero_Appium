package tests.Business.PayMany;

import factory.BusinessDataFactory;
import factory.PaymentDataFactory;
import factory.TransferDataFactory;
import models.BusinessData;
import models.PayManyData;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.payMany.PayManyPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import tests.PayMany.ExistingRecipientTests;
import utils.DriverManager;

import java.io.IOException;
import java.util.List;

public class ExistingBusinessRecipientTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(ExistingBusinessRecipientTests.class);

    private LoginPage loginPage;
    private HomePage homePage;
    private AccountMenuActions accountMenuActions;
    private PayManyPage payManyPage;
    private User appUser;
    private BusinessData payManyData;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        payManyPage = new PayManyPage(DriverManager.driver);
        appUser = TransferDataFactory.validAppUser();
        payManyData = BusinessDataFactory.validPayBusinessData();

        log.debug("Page objects and androidActions initialized");
    }

    @Test(priority = 0)
    public void PayManyRedoTest()
    {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        payManyPage.clickPayManyButton();
        payManyPage.getGroups(payManyData.getGroup());
        payManyPage.clickRedoButton(payManyData.getRecipientName1());
        payManyPage.clickPayButton();
        payManyPage.clickConfirmButton();
        payManyPage.duplicatePaymentCheck();
        try {
            Assert.assertEquals(payManyPage.transactionStatus(),"Thank you");
            attachScreenshot(DriverManager.driver,"Payment successful");
        } catch (AssertionError e) {
            log.error("Payment failed: {}", e.getMessage());
            throw e; // Rethrow the exception to fail the test
        }finally {
            payManyPage.clickFinish();
            homePage.clickLogoutButtn();
        }
    }

    @Test(priority = 1)
    public void deleteExistingRecipientForBusiness() {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        payManyPage.clickPayManyButton();
        payManyPage.getGroups(payManyData.getGroup());
        payManyPage.clickEditRecipient(payManyData.getRecipientName1());
        payManyPage.clickDelete();
        DriverManager.driver.navigate().back();
        accountMenuActions.clickAccountMenuActionsOption("Business");
        payManyPage.clickPayManyButton();
        payManyPage.getGroups(payManyData.getGroup());
        Assert.assertFalse(payManyPage.getRecipientNames().contains(payManyData.getRecipientName1()), "Recipient name still exists after deletion");
        attachScreenshot(DriverManager.driver, "Recipient deleted successfully");
        DriverManager.driver.navigate().back();
        homePage.clickLogoutButtn();
    }

    @Test(priority = 2)
    public void exportBusinessFileTest() {

        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        payManyPage.clickPayManyButton();
        payManyPage.clickExportButton();
        payManyPage.confirmFileExport();
        Assert.assertTrue(payManyPage.isFileDownloaded());
        attachScreenshot(DriverManager.driver, "File downloaded successfully");
        payManyPage.clickOkAfterExport();
        DriverManager.driver.navigate().back();
        homePage.clickLogoutButtn();
    }


    @Test(priority = 3)
    public void importFileUploadBusinessTest() throws IOException {

        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        payManyPage.clickPayManyButton();
        payManyPage.clickImportButton();
        payManyPage.uploadImportFile();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(payManyPage.getFileUploaded(), "/document/primary:Download/export_recipients (1) 1.csv", "Selected file name does not match expected value");

        payManyPage.clickConfirmImport();
        Assert.assertTrue(payManyPage.getRecipientNames().contains(payManyData.getRecipientName()), "Recipient name not found after import");
        attachScreenshot(DriverManager.driver, "Recipient imported successfully");

        List<String> actualRecipients = payManyPage.getRecipientNames();
        log.info("Actual recipient names in UI: {}", actualRecipients);
        List<String> importedRecipients =
                PayManyPage.getRecipientNamesFromExcel(
                        "src/test/java/testData/export_recipients (1).xlsx"
                );
        log.info("Recipient names from imported file: {}", importedRecipients);

        for (String recipient : importedRecipients) {
            softAssert.assertTrue(
                    actualRecipients.contains(recipient),
                    "Recipient name from imported file does not exist: " + recipient
            );
        }

        softAssert.assertTrue(
                actualRecipients.containsAll(importedRecipients),
                "Some recipient names from imported file do not exist in UI"
        );
        attachScreenshot(DriverManager.driver, "Recipients imported successfully");

        DriverManager.driver.navigate().back();
        homePage.clickLogoutButtn();
    }
}
