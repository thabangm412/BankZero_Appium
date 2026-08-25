package tests.Business.AuthChain;

import factory.BusinessDataFactory;
import models.OwnersAndOfficials;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pageObjects.app.accountsActionMenu.AccountMenuActions;
import pageObjects.app.accountsHome.HomePage;
import pageObjects.app.business.BusinessAuthChainPage;
import pageObjects.app.business.BusinessPage;
import pageObjects.app.login.LoginPage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;
import utils.DriverManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class AuthChainTests extends BaseTestsConfig {

    private static final Logger log = LoggerFactory.getLogger(AuthChainTests.class);

    private LoginPage loginPage;
    private BusinessPage businessPage;
    private BusinessAuthChainPage businessAuthChainPage;
    private  AndroidActions androidActions;
    private OwnersAndOfficials authChain;
    private AccountMenuActions accountMenuActions;
    private HomePage homePage;
    private SoftAssert softAssert;

    @BeforeMethod
    public void setUpPages() {
        log.debug("Initializing page objects for test.");
        loginPage = new LoginPage(DriverManager.driver);
        businessPage = new BusinessPage(DriverManager.driver);
        androidActions = new AndroidActions(DriverManager.driver);
        accountMenuActions = new AccountMenuActions(DriverManager.driver);
        businessAuthChainPage = new BusinessAuthChainPage(DriverManager.driver);
        homePage = new HomePage(DriverManager.driver);
        softAssert = new SoftAssert();
        authChain = BusinessDataFactory.ownersAndOfficials();

    }

    @Test(priority = 0, description = "Verify that the account name and account balance are displayed in the crumb bar")
    public void verifyAccountNameAndBalance() {

        loginPage.loginWithRetry(
                authChain.getUser().getProfileName(),
                authChain.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        businessAuthChainPage.clickAuthorisationChain();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(
                businessAuthChainPage.returnAccountNameElement().isDisplayed(),
                "Account name not displayed"
        );
        softAssert.assertTrue(
                businessAuthChainPage.returnAccountBalanceElement().isDisplayed(),
                "Account balance not displayed"
        );
        softAssert.assertAll();

        businessAuthChainPage.clickBackButton();
        //businessAuthChainPage.confirmUnsavedChanges();
    }

    @Test(priority = 1, description = "Verify that a new OAuth chain owner can be added successfully")
    public void addNewOAuthChainOwner() throws InterruptedException {

        loginPage.loginWithRetry(
                authChain.getUser().getProfileName(),
                authChain.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        businessAuthChainPage.clickAuthorisationChain();
        businessPage.addOwnerForAuthChain();
        businessPage.addOwnersAndOfficials(authChain.getRole1(), authChain.getNationality(), authChain.getCellNumber(), authChain.getOwnerName());
        businessPage.saveChanges();
        businessPage.clickUpdate();
        businessPage.clickConfirm();
        Thread.sleep(2000);
        accountMenuActions.clickAccountMenuActionsOption("Business");
        businessAuthChainPage.clickAuthorisationChain();
        softAssert.assertTrue(businessPage.getFinalOwnersAndOfficialsNames().contains(authChain.getOwnerName()));
        businessPage.clickFinish();
        softAssert.assertAll();

    }

    @Test(priority = 2, description = "Verify that valid OAuth chain owner from recycler view can be added to level A with respective amounts successfully")
    public void addOAuthChainOwnerToLevelA(){

        loginPage.loginWithRetry(
                authChain.getUser().getProfileName(),
                authChain.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        businessAuthChainPage.clickAuthorisationChain();
        businessAuthChainPage.dragLastOwner(BusinessAuthChainPage.AuthLevelSlot.A1.locator());
        businessAuthChainPage.inputLevelAmount("A", authChain.getLevelA_amount());

        businessAuthChainPage.clickUpdate();
        businessAuthChainPage.clickConfirm();

        BusinessAuthChainPage.AuthSlotState expected =
                businessAuthChainPage.getSlotState(BusinessAuthChainPage.AuthLevelSlot.A1);
        String expectedAmount = authChain.getLevelA_amount();

        softAssert.assertEquals(
                businessAuthChainPage.getSlotState(BusinessAuthChainPage.AuthLevelSlot.A1).authoriser(),
                expected.authoriser());
        softAssert.assertEquals(
                businessAuthChainPage.getLevelAmount(BusinessAuthChainPage.AuthLevelInput.LEVEL_A),
                expectedAmount);
        softAssert.assertAll();
        businessAuthChainPage.clickFinish();
    }

    @Test(priority = 3, description = "Verify that an existing OAuth chain owner can be moved to slot B on Level A")
    public void moveExistingOAuthChainOwnerToSlotBLevelA(){

        loginPage.loginWithRetry(
                authChain.getUser().getProfileName(),
                authChain.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        businessAuthChainPage.clickAuthorisationChain();
        businessAuthChainPage.dragSlotToSlot(
                BusinessAuthChainPage.AuthLevelSlot.A1,
                BusinessAuthChainPage.AuthLevelSlot.A2
        );
        businessAuthChainPage.clickUpdate();
        businessAuthChainPage.clickConfirm();
        BusinessAuthChainPage.AuthSlotState expected =
                businessAuthChainPage.getSlotState(BusinessAuthChainPage.AuthLevelSlot.A2);
        softAssert.assertEquals(
                businessAuthChainPage.getSlotState(BusinessAuthChainPage.AuthLevelSlot.A2).authoriser(),
                expected.authoriser());
        softAssert.assertAll();
        businessAuthChainPage.clickFinish();
    }

    @Test(priority = 4, description = "Verify that an existing OAuth chain owner can be moved to slot C on Level A")
    public void moveExistingOAuthChainOwnerToSlotCLevelA(){

        loginPage.loginWithRetry(
                authChain.getUser().getProfileName(),
                authChain.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        businessAuthChainPage.clickAuthorisationChain();
        businessAuthChainPage.dragSlotToSlot(
                BusinessAuthChainPage.AuthLevelSlot.A2,
                BusinessAuthChainPage.AuthLevelSlot.A3
        );
        businessAuthChainPage.clickUpdate();
        businessAuthChainPage.clickConfirm();
        BusinessAuthChainPage.AuthSlotState expected =
                businessAuthChainPage.getSlotState(BusinessAuthChainPage.AuthLevelSlot.A3);
        softAssert.assertEquals(
                businessAuthChainPage.getSlotState(BusinessAuthChainPage.AuthLevelSlot.A3).authoriser(),
                expected.authoriser());
        softAssert.assertAll();
        businessAuthChainPage.clickFinish();
    }

    @Test(priority = 5, description = "Verify that user can remove an existing OAuth chain owner from Level A and clear the amount")
    public void removeAuthChainLevelAowner(){

        loginPage.loginWithRetry(
                authChain.getUser().getProfileName(),
                authChain.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        businessAuthChainPage.clickAuthorisationChain();
        businessAuthChainPage.dragOwnerToDelete(
                BusinessAuthChainPage.AuthLevelSlot.A3
        );
        businessAuthChainPage.confirmRemoveAuthorizer();
        businessAuthChainPage.clearLevelAmount(BusinessAuthChainPage.AuthLevelInput.LEVEL_A);
        businessAuthChainPage.clickUpdate();
        businessAuthChainPage.clickConfirm();
        softAssert.assertFalse(
                businessAuthChainPage.isSlotOccupied(BusinessAuthChainPage.AuthLevelSlot.A1));
        softAssert.assertTrue(
                businessAuthChainPage.isLevelAmountCleared(BusinessAuthChainPage.AuthLevelInput.LEVEL_A));
        softAssert.assertAll();
        businessAuthChainPage.clickFinish();
    }

    @Test(priority = 6, description = "Verify that valid OAuth chain owner from recycler view can be added to level B with respective amounts successfully")
    public void addOAuthChainOwnerToLevelB(){

        loginPage.loginWithRetry(
                authChain.getUser().getProfileName(),
                authChain.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        businessAuthChainPage.clickAuthorisationChain();
        businessAuthChainPage.dragLastOwner(BusinessAuthChainPage.AuthLevelSlot.B1.locator());
        businessAuthChainPage.inputLevelAmount("B", authChain.getLevelB_amount());
        attachScreenshot(DriverManager.driver, "Auth_Chain_Level_B_Added");
        businessAuthChainPage.clickUpdate();
        businessAuthChainPage.clickConfirm();

        BusinessAuthChainPage.AuthSlotState expected =
                businessAuthChainPage.getSlotState(BusinessAuthChainPage.AuthLevelSlot.B1);
        String expectedAmount = authChain.getLevelB_amount();

        softAssert.assertEquals(
                businessAuthChainPage.getSlotState(BusinessAuthChainPage.AuthLevelSlot.B1).authoriser(),
                expected.authoriser());
        softAssert.assertEquals(
                businessAuthChainPage.getLevelAmount(BusinessAuthChainPage.AuthLevelInput.LEVEL_B),
                expectedAmount);
        attachScreenshot(DriverManager.driver, "Auth_Chain_Level_B_Verified");
        softAssert.assertAll();
        businessAuthChainPage.clickFinish();
    }

    @Test(priority = 7, description = "Verify that an existing OAuth chain owner can be moved to slot B on Level B")
    public void moveExistingOAuthChainOwnerToSlotBLevelB(){

        loginPage.loginWithRetry(
                authChain.getUser().getProfileName(),
                authChain.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        businessAuthChainPage.clickAuthorisationChain();
        attachScreenshot(DriverManager.driver, "Auth_Chain_Level_B_Before_Move");
        businessAuthChainPage.dragSlotToSlot(
                BusinessAuthChainPage.AuthLevelSlot.B1,
                BusinessAuthChainPage.AuthLevelSlot.B2
        );

        businessAuthChainPage.clickUpdate();
        businessAuthChainPage.clickConfirm();
        BusinessAuthChainPage.AuthSlotState expected =
                businessAuthChainPage.getSlotState(BusinessAuthChainPage.AuthLevelSlot.B2);
        softAssert.assertEquals(
                businessAuthChainPage.getSlotState(BusinessAuthChainPage.AuthLevelSlot.B2).authoriser(),
                expected.authoriser());
        attachScreenshot(DriverManager.driver, "Auth_Chain_Level_B_After_Move");
        softAssert.assertAll();
        businessAuthChainPage.clickFinish();
    }

    @Test(priority = 8, description = "Verify that an existing OAuth chain owner can be moved to slot C on Level B")
    public void moveExistingOAuthChainOwnerToSlotCLevelB(){

        loginPage.loginWithRetry(
                authChain.getUser().getProfileName(),
                authChain.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        businessAuthChainPage.clickAuthorisationChain();
        attachScreenshot(DriverManager.driver, "Auth_Chain_Level_B_Before_Move");
        businessAuthChainPage.dragSlotToSlot(
                BusinessAuthChainPage.AuthLevelSlot.B2,
                BusinessAuthChainPage.AuthLevelSlot.B3
        );
        businessAuthChainPage.clickUpdate();
        businessAuthChainPage.clickConfirm();
        BusinessAuthChainPage.AuthSlotState expected =
                businessAuthChainPage.getSlotState(BusinessAuthChainPage.AuthLevelSlot.B3);
        softAssert.assertEquals(
                businessAuthChainPage.getSlotState(BusinessAuthChainPage.AuthLevelSlot.B3).authoriser(),
                expected.authoriser());
        attachScreenshot(DriverManager.driver, "Auth_Chain_Level_B_After_Move");
        softAssert.assertAll();
        businessAuthChainPage.clickFinish();
    }

    @Test(priority = 9, description = "Verify that user can remove an existing OAuth chain owner from Level B and clear the amount")
    public void removeAuthChainLevelBowner(){

        loginPage.loginWithRetry(
                authChain.getUser().getProfileName(),
                authChain.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        businessAuthChainPage.clickAuthorisationChain();
        attachScreenshot(DriverManager.driver, "Auth_Chain_Level_B_Before_Remove");
        businessAuthChainPage.dragOwnerToDelete(
                BusinessAuthChainPage.AuthLevelSlot.B3
        );
        businessAuthChainPage.confirmRemoveAuthorizer();
        businessAuthChainPage.clearLevelAmount(BusinessAuthChainPage.AuthLevelInput.LEVEL_B);

        businessAuthChainPage.clickUpdate();
        businessAuthChainPage.clickConfirm();
        softAssert.assertFalse(
                businessAuthChainPage.isSlotOccupied(BusinessAuthChainPage.AuthLevelSlot.B1));
        softAssert.assertTrue(
                businessAuthChainPage.isLevelAmountCleared(BusinessAuthChainPage.AuthLevelInput.LEVEL_B));
        attachScreenshot(DriverManager.driver, "Auth_Chain_Level_B_After_Remove");
        softAssert.assertAll();
        businessAuthChainPage.clickFinish();
    }

    @Test(priority = 10, description = "Verify that valid OAuth chain owner from recycler view can be added to level C with respective amounts successfully")
    public void addOAuthChainOwnerToLevelC(){

        loginPage.loginWithRetry(
                authChain.getUser().getProfileName(),
                authChain.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        businessAuthChainPage.clickAuthorisationChain();
        businessAuthChainPage.dragLastOwner(BusinessAuthChainPage.AuthLevelSlot.C1.locator());
        businessAuthChainPage.inputLevelAmount("C", authChain.getLevelC_amount());
        attachScreenshot(DriverManager.driver, "Auth_Chain_Level_C_Added");
        businessAuthChainPage.clickUpdate();
        businessAuthChainPage.clickConfirm();
        androidActions.scrollToBottom();
        BusinessAuthChainPage.AuthSlotState expected =
                businessAuthChainPage.getSlotState(BusinessAuthChainPage.AuthLevelSlot.C1);
        String expectedAmount = authChain.getLevelC_amount();

        softAssert.assertEquals(
                businessAuthChainPage.getSlotState(BusinessAuthChainPage.AuthLevelSlot.C1).authoriser(),
                expected.authoriser());
        softAssert.assertEquals(
                businessAuthChainPage.getLevelAmount(BusinessAuthChainPage.AuthLevelInput.LEVEL_C),
                expectedAmount);
        attachScreenshot(DriverManager.driver, "Auth_Chain_Level_C_Verified");
        softAssert.assertAll();
        businessAuthChainPage.clickFinish();
    }

    @Test(priority = 11, description = "Verify that an existing OAuth chain owner can be moved to slot B on Level C")
    public void moveExistingOAuthChainOwnerToSlotBLevelC(){

        loginPage.loginWithRetry(
                authChain.getUser().getProfileName(),
                authChain.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        businessAuthChainPage.clickAuthorisationChain();
        attachScreenshot(DriverManager.driver, "Auth_Chain_Level_C_Before_Move");
        businessAuthChainPage.dragSlotToSlot(
                BusinessAuthChainPage.AuthLevelSlot.C1,
                BusinessAuthChainPage.AuthLevelSlot.C2
        );
        businessAuthChainPage.clickUpdate();
        businessAuthChainPage.clickConfirm();
        androidActions.scrollToBottom();
        BusinessAuthChainPage.AuthSlotState expected =
                businessAuthChainPage.getSlotState(BusinessAuthChainPage.AuthLevelSlot.C2);
        softAssert.assertEquals(
                businessAuthChainPage.getSlotState(BusinessAuthChainPage.AuthLevelSlot.C2).authoriser(),
                expected.authoriser());
        attachScreenshot(DriverManager.driver, "Auth_Chain_Level_C_After_Move");
        softAssert.assertAll();
        businessAuthChainPage.clickFinish();
    }

    @Test(priority = 12, description = "Verify that an existing OAuth chain owner can be moved to slot C on Level C")
    public void moveExistingOAuthChainOwnerToSlotCLevelC(){

        loginPage.loginWithRetry(
                authChain.getUser().getProfileName(),
                authChain.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        businessAuthChainPage.clickAuthorisationChain();
        androidActions.scrollToBottom();
        attachScreenshot(DriverManager.driver, "Auth_Chain_Level_C_Before_Move");
        businessAuthChainPage.dragSlotToSlot(
                BusinessAuthChainPage.AuthLevelSlot.C2,
                BusinessAuthChainPage.AuthLevelSlot.C3
        );
        businessAuthChainPage.clickUpdate();
        businessAuthChainPage.clickConfirm();
        androidActions.scrollToBottom();
        BusinessAuthChainPage.AuthSlotState expected =
                businessAuthChainPage.getSlotState(BusinessAuthChainPage.AuthLevelSlot.C3);
        softAssert.assertEquals(
                businessAuthChainPage.getSlotState(BusinessAuthChainPage.AuthLevelSlot.C3).authoriser(),
                expected.authoriser());
        softAssert.assertAll();
        attachScreenshot(DriverManager.driver, "Auth_Chain_Level_C_After_Move");
        businessAuthChainPage.clickFinish();
    }

    @Test(priority = 13, description = "Verify that user can remove an existing OAuth chain owner from Level C and clear the amount")
    public void removeAuthChainLevelCowner(){

        loginPage.loginWithRetry(
                authChain.getUser().getProfileName(),
                authChain.getUser().getLoginPin(),
                2
        );

        accountMenuActions.clickAccountMenuActionsOption("Business");
        businessAuthChainPage.clickAuthorisationChain();
        //androidActions.scrollToBottom();
        attachScreenshot(DriverManager.driver, "Auth_Chain_Level_C_Before_Remove");
        businessAuthChainPage.dragOwnerToDelete(
                BusinessAuthChainPage.AuthLevelSlot.C3
        );
        businessAuthChainPage.confirmRemoveAuthorizer();
        businessAuthChainPage.clearLevelAmount(BusinessAuthChainPage.AuthLevelInput.LEVEL_C);
        businessAuthChainPage.clickUpdate();
        businessAuthChainPage.clickConfirm();
        androidActions.scrollToBottom();
        softAssert.assertFalse(
                businessAuthChainPage.isSlotOccupied(BusinessAuthChainPage.AuthLevelSlot.C1));
        softAssert.assertTrue(
                businessAuthChainPage.isLevelAmountCleared(BusinessAuthChainPage.AuthLevelInput.LEVEL_C));
        softAssert.assertAll();
        attachScreenshot(DriverManager.driver, "Auth_Chain_Level_C_After_Remove");
        businessAuthChainPage.clickFinish();
    }


    @DataProvider
    public Object[] [] getMultipleDataSet() throws IOException {

        String path =
                System.getProperty("user.dir") + File.separator + "src" + File.separator
                        + "test" + File.separator + "java" + File.separator + "testData" + File.separator + "authChainData.json";
        log.debug("Loading test data from: {}", path);

        List<HashMap<String, String>> data = getJsonData(path);
        if (data == null || data.isEmpty()) {
            log.error("No test data found at: {}", path);
            throw new IllegalStateException("Test data is empty: " + path);
        }

        log.info("Providing {} data set(s) to test", data.size());
        return new Object[][]{{data.getFirst()}};
    }

    @AfterMethod
    public void cleanUp() {
            homePage.clickLogoutButtn();
            log.info("Logged out successfully during cleanup.");
    }
}
