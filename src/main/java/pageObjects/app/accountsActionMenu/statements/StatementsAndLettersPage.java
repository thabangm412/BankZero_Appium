package pageObjects.app.accountsActionMenu.statements;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageObjects.app.accountsActionMenu.pay.QuickPayPage;
import utils.AndroidActions;
import utils.AppiumUtils;

import java.time.Duration;

public class StatementsAndLettersPage {

    private static final Logger log = LoggerFactory.getLogger(StatementsAndLettersPage.class);
    protected AndroidDriver driver;

    public StatementsAndLettersPage(AndroidDriver driver) {
             if (driver == null) {
        throw new IllegalStateException("AndroidDriver is NULL. Check DriverManager initialization order.");
    }

    this.driver = driver;
    PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Statements & Letters\"]")
    private WebElement statementsAndLettersButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/document_type_dd_arrow")
    private WebElement selectDocumentTypeDropDown;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnConfirm")
    private WebElement sendEmailButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/periodoption_dd_arrow")
    private WebElement forPeriodDropDown;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnConfirm")
    private WebElement finishButton;

    @AndroidFindBy(id = "android:id/button3")
    private WebElement okButton;

    public void getDocumentType(String documentType)
    {
        AndroidActions androidActions = new AndroidActions(driver);
        selectDocumentTypeDropDown.click();

        switch (documentType.toLowerCase())
        {
            case "account confirmation letter":
                driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"android:id/text1\" and @text=\"Account Confirmation Letter\"]"))
                        .click();

                break;
            case "account statements":
                driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"android:id/text1\" and @text=\"Account Statements\"]"))
                        .click();
                break;

            case "salary switch letter":
                driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"android:id/text1\" and @text=\"Salary Switch Letter\"]"))
                        .click();
                break;

            case "welcome letter":
                driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"android:id/text1\" and @text=\"Welcome Letter\"]"))
                        .click();
                break;

            case "download payments recipients":
                driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"android:id/text1\" and @text=\"Download Payments Recipients\"]"))
                        .click();
                break;

            case "it3(b)":
                driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"android:id/text1\" and @text=\"It3(b)\"]"))
                        .click();
                break;
            default:
                log.error("Invalid document type provided: {}", documentType);
                throw new IllegalArgumentException("Invalid document type: " + documentType);
        }

        log.info("Selected document type: {}", documentType);
            androidActions.attachScreenshot(driver, "Selected document type: " + documentType);
    }

    public void clickAccountStatements()
    {
        statementsAndLettersButton.click();
        log.info("Clicked on Statements & Letters menu item."
        );
    }
    public void getAccountConfirmationLetter()
    {
        getDocumentType("Account Confirmation Letter");
    }

    public void getAccountStatements(Integer index)
    {
        AndroidActions androidActions = new AndroidActions(driver);
        getDocumentType("Account Statements");
        forPeriodDropDown.click();
        driver.findElements(
                By.xpath("//android.widget.TextView[@resource-id='android:id/text1']")
        ).get(index).click();
        log.info("Selected statement period index: {}", index);
        androidActions.attachScreenshot(driver, "Selected statement period index: " + index);
    }

    public void getSalarySwitchLetter()
    {
        getDocumentType("Salary Switch Letter");
    }

    public void getIt3bLetter(Integer index)
    {
        AndroidActions androidActions = new AndroidActions(driver);
        getDocumentType("It3(b)");
        forPeriodDropDown.click();
        driver.findElements(
                By.xpath("//android.widget.TextView[@resource-id='android:id/text1']")
        ).get(index).click();
        log.info("Selected it3(b) period index: {}", index);
        androidActions.attachScreenshot(driver, "Selected it3(b) period index: " + index);
    }

    public void getWelcomeLetter()
    {
        getDocumentType("Welcome Letter");
    }

    public void getDownloadPaymentsRecipients()
    {
        getDocumentType("Download Payments Recipients");
    }

    public void clickEmailButton() {
        sendEmailButton.click();
        log.info("Clicked on send email button");
    }

    public String getDocumentRequestStatus()
    {
        AndroidActions.waitForElementAttribute(driver, "//android.widget.LinearLayout[@resource-id=\"za.co.neolabs.bankzero:id/action_bar_root\"]", "displayed", "true", 15);
        String status = driver.findElement(By.id("android:id/message")).getText();
        log.info("Document request status: {}", status);
        return status;
    }

    public void clickOkButton()
    {
        okButton.click();
        log.info("Clicked on OK button in status dialog");
    }

    public void clickFinishButton()
    {
        //AndroidActions.waitForElementAttribute(driver, "//android.widget.LinearLayout[@resource-id=\"za.co.neolabs.bankzero:id/action_bar_root\"]", "displayed", "false", 5);
        finishButton.click();
        log.info("Clicked on Finish button in status dialog");
    }
}
