package pageObjects.app.accountsActionMenu.buy;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AndroidActions;
import utils.AppiumUtils;

import java.time.Duration;
import java.util.List;

public class BuyDataPage {

    protected AndroidDriver driver;

    private static final Logger log = LoggerFactory.getLogger(BuyDataPage.class);

    public BuyDataPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/list_text\" and @text=\"Data\"]")
    private WebElement dataButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/product_dd_arrow")
    private WebElement productDropDownButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/recipient_phone")
    private WebElement recipientInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/single_amount")
    private WebElement dataAmountInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/_inputText")
    private WebElement itemNameInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/provider_dd_arrow")
    private WebElement proviserDropDownButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnSubmit")
    private WebElement buyOrSubmittButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnConfirm")
    private WebElement confirmtButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/notify_email")
    private WebElement popEmailInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/notify_cellno")
    private WebElement popPhoneInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/_arrow")
    private WebElement selectItemDropDownButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/amount")
    private WebElement amountInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/reference")
    private WebElement referenceInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/status_textview")
    private WebElement statusField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnConfirm")
    private WebElement finishButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/tile_to_mask")
    private WebElement buyAddtButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Buy\"]")
    private WebElement buyAccountButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/redo_image")
    private WebElement redoButtn;


    public void clickBuyButton()
    {
        buyAccountButtn.click();
        log.info("Buy button clicked...");

    }

    public void openDataPage()
    {
        dataButton.click();
        log.info("Data Button clicked");
    }

    public void  addDataItem(String name,String provider,String product,String cellNumber)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Add Item", driver);

        AndroidActions androidActions = new AndroidActions(driver);

        dataButton.click();
        log.info("Data button clicked...");

        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Add Data item",driver);
        log.info("Add Data page displayed...");

        itemNameInputField.clear();
        log.info("Item name input field cleared");
        itemNameInputField.sendKeys(name);
        log.info("Item name entered");

        proviserDropDownButtn.click();
        log.info("Provider options displayed");
        androidActions.scrollToTextAndClick2(provider,driver);
        log.info("Provider options chosen: {}", provider);

        productDropDownButtn.click();
        log.info("Product options displayed");
        androidActions.scrollToTextAndClick2(product,driver);
        log.info("Product options chosen: {}", provider);

        recipientInputField.sendKeys(cellNumber);
        log.info("Recipient cell number input field cleared");

        clickBuyOrSubmitButton();
        log.info("Add button clicked...");
        clickBuyOrSubmitButton();
        log.info("Confirm button clicked...");

    }

    public void addAccButton()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Quick Buy", driver);
        buyAddtButtn.click();
        log.info("Add acc button clicked.");

    }

    public void clickBuyOrSubmitButton()
    {
        buyOrSubmittButtn.click();
        log.info("Buy or submit button clicked...");

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

    public void clickHomeBuyButton()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Quick Buy",driver);
        buyOrSubmittButtn.click();
        log.info("Buy has been clicked");
    }

    public void clickConfirmButton()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Buy Data",driver);
        confirmtButtn.click();
        log.info("Confirm button has been clicked");
    }

    public void buyData(String amount, String ref)
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

    public void getExistingProfile(String name)
    {
        selectItemDropDownButton.click();
        log.info("Select item dropdown menu button clicked");
        AppiumUtils.waitForElement(By.xpath("/hierarchy/android.widget.FrameLayout"),driver);
        log.info("Frame with group names displayed");

        List<WebElement> groupNames = driver.findElements(By.id("za.co.neolabs.bankzero:id/groupName"));
        log.info("All found group names: {}", groupNames);

        try {
            for (WebElement group : groupNames) {
                if (group.getText().equals("Data")) {
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
            log.error("An error occurred while processing Data group names: {}", e.getMessage(), e);
        }

//        amountInputField.clear();
//        log.info("Amount input field cleared...");
//        amountInputField.sendKeys(amount);
//        log.info("Amount entered: {}", amount);
//
//        referenceInputField.clear();
//        log.info("Amount input field cleared...");
//        referenceInputField.sendKeys(ref);
//        log.info("Entered reference: {}", ref);
//        buyOrSubmittButtn.click();
//        log.info("Buy button clicked.");

    }

    public void clickRedo()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Quick Buy",driver);
        redoButtn.click();
        log.info("Redo button clicked");
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
