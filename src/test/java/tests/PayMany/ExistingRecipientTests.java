package tests.PayMany;

import factory.PaymentDataFactory;
import factory.TransferDataFactory;
import models.PayManyData;
import models.User;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsActionMenu.payMany.PayManyPage;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;
import utils.DriverManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ExistingRecipientTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(ExistingRecipientTests.class);

    private LoginPage loginPage;
    private HomePage homePage;
    private AccountMenuActions accountMenuActions;
    private PayManyPage payManyPage;
    private User appUser;
    private PayManyData payManyData;
    private PayManyData payManyData2;

    @BeforeMethod
    public void preSetUp() {
        loginPage = new LoginPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        payManyPage = new PayManyPage(DriverManager.driver);
        appUser = TransferDataFactory.validAppUser();
        payManyData = PaymentDataFactory.validPayManyData();
        payManyData2 = PaymentDataFactory.validPayManyData2();

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

        accountMenuActions.clickAccountMenuActionsButtn();
        payManyPage.clickPayManyButton();
        payManyPage.getGroups(payManyData.getGroup());
        payManyPage.clickRedoButton(payManyData.getRecipientName());
        payManyPage.clickPayButton();
        payManyPage.clickConfirmButton();
        Assert.assertEquals(payManyPage.transactionStatus(),"Thank you");
        payManyPage.clickFinish();
        homePage.clickLogoutButtn();

    }

    @Test(priority = 1)
    public void updateExistingRecipient()
    {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsButtn();
        payManyPage.clickPayManyButton();
        payManyPage.getGroups(payManyData.getGroup());
        payManyPage.clickEditRecipient(payManyData.getRecipientName());
        payManyPage.updateRecipientDetails(payManyData2.getRecipientName(),payManyData2.getGroup(),payManyData2.getBank(),payManyData2.getAccount(), payManyData2.getAccountNo());
        payManyPage.enterPOPDetails(payManyData2.getPopEmail(),payManyData2.getPopPhone());
        attachScreenshot(DriverManager.driver,"Updated POP details");
        payManyPage.clickAddButton();
        DriverManager.driver.navigate().back();

        accountMenuActions.clickAccountMenuActionsButtn();
        payManyPage.clickPayManyButton();
        payManyPage.getGroups(payManyData2.getGroup());
        Assert.assertTrue(payManyPage.getRecipientNames().contains(payManyData2.getRecipientName()), "Recipient name does not match expected value");
        attachScreenshot(DriverManager.driver,"Recipient added successfully");
        DriverManager.driver.navigate().back();
        homePage.clickLogoutButtn();

    }

    @Test(priority = 3)
    public void paymentToUpdatedRecipient()
    {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsButtn();
        payManyPage.clickPayManyButton();
        payManyPage.getGroups(payManyData2.getGroup());
        payManyPage.clickNewPayment(payManyData2.getRecipientName());
        payManyPage.enterAmount("50");
        payManyPage.clickPayButton();
        payManyPage.clickConfirmButton();
        Assert.assertEquals(payManyPage.transactionStatus(),"Thank you");
        payManyPage.clickFinish();
        homePage.clickLogoutButtn();
    }

    @Test(priority = 4)
    public void deleteExistingRecipient() {
        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsButtn();
        payManyPage.clickPayManyButton();
        payManyPage.getGroups(payManyData2.getGroup());
        payManyPage.clickEditRecipient(payManyData2.getRecipientName());
        payManyPage.clickDelete();

        DriverManager.driver.navigate().back();

        accountMenuActions.clickAccountMenuActionsButtn();
        payManyPage.clickPayManyButton();
        payManyPage.getGroups(payManyData2.getGroup());
        Assert.assertFalse(payManyPage.getRecipientNames().contains(payManyData2.getRecipientName()), "Recipient name still exists after deletion");
        attachScreenshot(DriverManager.driver, "Recipient deleted successfully");

        DriverManager.driver.navigate().back();
        homePage.clickLogoutButtn();
    }

    @Test(priority = 5)
    public void exportFileDownloadTest() {

        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsButtn();
        payManyPage.clickPayManyButton();
        payManyPage.clickExportButton();
        payManyPage.confirmFileExport();
        Assert.assertTrue(payManyPage.isFileDownloaded());
        attachScreenshot(DriverManager.driver, "File downloaded successfully");
        payManyPage.clickOkAfterExport();
        DriverManager.driver.navigate().back();
        homePage.clickLogoutButtn();
    }

    @Test(priority = 6)
    public void importFileUploadTest() throws IOException {

        loginPage.loginWithRetry(
                appUser.getUser().getProfileName(),
                appUser.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsButtn();
        payManyPage.clickPayManyButton();
        payManyPage.clickImportButton();
        payManyPage.uploadImportFile();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(payManyPage.getFileUploaded(), "/document/primary:Download/export_recipients (1) 1.csv", "Selected file name does not match expected value");

        payManyPage.clickConfirmImport();
        Assert.assertTrue(payManyPage.getRecipientNames().contains(payManyData2.getRecipientName()), "Recipient name not found after import");
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
