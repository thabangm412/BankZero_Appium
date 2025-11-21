package pageObjects.app.accountsActionMenu.buy;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AndroidActions;
import utils.AppiumUtils;

import java.time.Duration;
import java.util.List;

public class BuyElectricityPage {

    protected AndroidDriver driver;

    private static final Logger log = LoggerFactory.getLogger(BuyElectricityPage.class);

    public BuyElectricityPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Buy\"]")
    private WebElement buyAccountButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/tile_to_mask")
    private WebElement buyAddtButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnSubmit")
    private WebElement buyOrSubmittButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/toolbar_title")
    private WebElement pageTitle;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/list_text\" and @text=\"Electricity\"]")
    private WebElement electricityButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/_inputText")
    private WebElement itemNameInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/provider_dd_arrow")
    private WebElement proviserDropDownButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/meter_number")
    private WebElement meterNumberInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/token_phone_no")
    private WebElement smsTokenInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/notify_email")
    private WebElement popEmailInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/notify_cellno")
    private WebElement popPhoneInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/amount")
    private WebElement amountInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/reference")
    private WebElement referenceInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/_arrow")
    private WebElement accountsDropDownButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/mnu_top_1")
    private WebElement addElectricityAccButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/_arrow")
    private WebElement selectItemDropDownButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/status_textview")
    private WebElement statusField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnConfirm")
    private WebElement finishButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnConfirm")
    private WebElement confirmButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/edit_note")
    private WebElement editButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/mnu_top_1")
    private WebElement deleteButton;

    @AndroidFindBy(id = "android:id/button1")
    private WebElement deleteConfirmButton;

    @AndroidFindBy(accessibility = "Navigate up")
    private WebElement backButton;


    public void clickBack()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Quick Buy",driver);
        log.info("Quick buy page displayed.");
        backButton.click();
        log.info("Back button clicked");
    }

    public void getExistingRecipient(String name)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Quick Buy",driver);
        log.info("Quick buy page displayed.");
        selectItemDropDownButton.click();
        log.info("Select item dropdown menu button clicked");

        AppiumUtils.waitForElement(By.xpath("/hierarchy/android.widget.FrameLayout"),driver);
        log.info("Frame with group names displayed");

        List<WebElement> groupNames = driver.findElements(By.id("za.co.neolabs.bankzero:id/groupName"));
        log.info("All found group names: {}", groupNames);

        try {
            for (WebElement group : groupNames) {
                if (group.getText().equals("Electricity")) {
                    log.info("Group name matching Data found");
                    AndroidActions androidActions = new AndroidActions(driver);
                    androidActions.scrollToTextAndClick2(name, driver);
                    log.info("Scrolled to the account name: {}", name);
                    break; // stop once found
                } else {
                    log.warn("Data group name not found!");
                }
            }
        } catch (Exception e) {
            log.error("An error occurred while processing group names: {}", e.getMessage(), e);
        }
    }

    public void clickEditButton()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Quick Buy",driver);
        log.info("Quick buy page displayed...");
        editButton.click();
        log.info("Edit button clicked");
    }

    public void clickDeleteButton()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Edit Electricity item",driver);
        log.info("Edit Airtime item page displayed.");
        deleteButton.click();
        log.info("Delete button clicked");
        deleteConfirmButton.click();
        log.info("Confirm delete button clicked");
    }

    public String getDeleteToastMsg()
    {
        String toastMessage = null;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Wait until the specific toast appears
            WebElement toastMsg = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//android.widget.Toast[@text=\"Item deleted!\"]")
            ));

            toastMessage = toastMsg.getText();
            log.info("Toast found: '{}'", toastMessage);

        } catch (Exception e) {
            log.warn("Toast 'Item deleted!' not found within timeout: {}", e.getMessage());
        }
        return toastMessage;
    }

    public void clickBuyButton()
    {
        buyAccountButtn.click();
        log.info("Buy button clicked...");

    }

    public void finishButton()
    {
        finishButton.click();
        log.info("Finish button clicked...");

    }

    public void clickAddButton()
    {
        buyAddtButtn.click();
        log.info("Buy add button clicked...");
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Add Item", driver);
        log.info("Add item page displayed...");

    }

    public void clickBuyOrSubmitButton()
    {
        buyOrSubmittButtn.click();
        log.info("Buy or submit button clicked...");

    }

    public void addAccButton()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Quick Buy", driver);
        buyAddtButtn.click();
        log.info("Add acc button clicked.");

    }

    public void addNewElectricityAcc()
    {

        if(buyAddtButtn.isEnabled())
        {
            buyAddtButtn.click();
            log.info("No existing below add button enabled..");

        } else {

            addElectricityAccButtn.click();
            log.info("Top add account button clicked..");

        }
    }

    public void addElectricityItem(String name,String provider,String tokenNo, String meterNo)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Add Item", driver);

        AndroidActions androidActions = new AndroidActions(driver);

        electricityButton.click();
        log.info("Electricity button clicked...");

        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Add Electricity item",driver);
        log.info("Add Electricity page displayed...");

        itemNameInputField.clear();
        log.info("Item name input field cleared...");
        itemNameInputField.sendKeys(name);
        log.info("Entered item name...");
        androidActions.scrollToTextAndClick2(provider, driver);
        log.info("Provide picked: {}", provider);

        meterNumberInputField.clear();
        log.info("Meter number input field cleared...");
        meterNumberInputField.sendKeys(meterNo);
        log.info("Entered meter number: {}", meterNo);
        log.info("SmS token input field cleared...");
        smsTokenInputField.sendKeys(tokenNo);
        log.info("Entered sms token number: {}", tokenNo);
//        popEmailInputField.clear();
//        log.info("POP email input field cleared...");
//        popEmailInputField.sendKeys(email);
//        log.info("Entered meter number: {}", email);
//        popPhoneInputField.clear();
//        log.info("POP phone number input field cleared...");
//        popPhoneInputField.sendKeys(cellNo);
//        log.info("Entered meter number: {}", cellNo);

        clickBuyOrSubmitButton();
        log.info("Add button clicked...");
        clickBuyOrSubmitButton();
        log.info("Confirm button clicked...");

    }

    public void buyElectricity(String amount, String ref)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Quick Buy",driver);
        log.info("Quick buy page displayed...");

        amountInputField.clear();
        log.info("Amount input field cleared...");
        amountInputField.sendKeys(amount);
        log.info("Amount entered: {}", amount);

        referenceInputField.clear();
        log.info("Reference input field cleared...");
        referenceInputField.sendKeys(ref);
        log.info("Entered reference: {}", ref);
        buyOrSubmittButtn.click();
        log.info("Buy button clicked");
    }


    public void addProofPurchase(String email,String cellNumber)
    {
        popEmailInputField.clear();
        log.info("POP email input field cleared");
        popEmailInputField.sendKeys(email);
        log.info("POP email entered: {}", email);
        popPhoneInputField.clear();
        log.info("POP cell number input field cleared");
        popPhoneInputField.sendKeys(cellNumber);
        log.info("POP cell number entered: {}", cellNumber);
    }

    public void clickConfrimButton()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Buy Electricity",driver);
        confirmButton.click();
        log.info("Confirm button clicked.");
    }


    public void buyElectricityAgain(String name,String amount, String ref)
    {
        selectItemDropDownButton.click();
        log.info("Select item dropdown menu button clicked");
        //AppiumUtils.waitForElementToAppear((WebElement) By.xpath("/hierarchy/android.widget.FrameLayout"), "displayed", "true",driver);
        AppiumUtils.waitForElement(By.xpath("/hierarchy/android.widget.FrameLayout"),driver);
        log.info("Frame with group names displayed");

        List<WebElement> groupNames = driver.findElements(By.id("za.co.neolabs.bankzero:id/groupName"));
        log.info("All found group names: {}", groupNames);

        try {
            for (WebElement group : groupNames) {
                if (group.getText().equals("Electricity")) {
                    log.info("Group name matching Data found");
                    AndroidActions androidActions = new AndroidActions(driver);
                    androidActions.scrollToTextAndClick2(name, driver);
                    log.info("Scrolled to the account name: {}", name);
                    break; // stop once found
                } else {
                    log.warn("Data group name not found!");
                }
            }
        } catch (Exception e) {
            log.error("An error occurred while processing group names: {}", e.getMessage(), e);
        }

        amountInputField.clear();
        log.info("Amount input field cleared...");
        amountInputField.sendKeys(amount);
        log.info("Amount entered: {}", amount);

        referenceInputField.clear();
        log.info("Amount input field cleared...");
        referenceInputField.sendKeys(ref);
        log.info("Entered reference: {}", ref);
        buyOrSubmittButtn.click();
        log.info("Buy button clicked");

    }

    public String getTransactionStatus()
    {
        String status = statusField.getText();
        log.info("Transaction Status: {}", status);

        return status;
    }

    public void clickFinishButton()
    {
        finishButton.click();
        log.info("Finish button clicked...");

    }

}
