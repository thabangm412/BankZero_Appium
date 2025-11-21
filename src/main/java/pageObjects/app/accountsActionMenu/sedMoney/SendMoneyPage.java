package pageObjects.app.accountsActionMenu.sedMoney;

import io.appium.java_client.AppiumBy;
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

public class SendMoneyPage {
    private static final Logger log = LoggerFactory.getLogger(SendMoneyPage.class);

    protected AndroidDriver driver;

    public SendMoneyPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Send Money\"]")
    private WebElement sendMoneyAccountButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/tile_to_mask")
    private WebElement addRecipienttButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/_inputText")
    private WebElement recipientInputNameField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/editPhone")
    private WebElement recipientPhoneInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnSubmit")
    private WebElement addButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/editPhone")
    private WebElement addedphone;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/amount")
    private WebElement amountInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/reference")
    private WebElement refInputField;

    @AndroidFindBy(xpath = "(//android.widget.ImageButton[@resource-id=\"za.co.neolabs.bankzero:id/imgButton\"])[1]")
    private WebElement attachmentLink;

    @AndroidFindBy(accessibility = "Show roots")
    private WebElement openFrom;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnSubmit")
    private WebElement sendButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnConfirm")
    private WebElement confirmButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/toolbar_title")
    private WebElement status;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnConfirm")
    private WebElement finishButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/_arrow")
    private WebElement dropDownButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/fileImage")
    private WebElement fileImage;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/edit_note")
    private WebElement editProfile;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/mnu_top_1")
    private WebElement deleteButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnSubmit")
    private WebElement  updateButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/redo_image")
    private WebElement  redoButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/redo_image")
    private WebElement  getProfileName;


    public void clickSendMoneyButton()
    {
        sendMoneyAccountButtn.click();
        log.info("Send Money button clicked...");
    }

    public void clickAddRecipientButton()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Send Money", driver);
        addRecipienttButtn.click();
        log.info("Add recipient button clicked.");
    }

    public void addRecipient(String name, String phone)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Add recipient", driver);

        recipientInputNameField.clear();
        log.info("Recipient name input field cleared");

        recipientInputNameField.sendKeys(name);
        log.info("Recipient name entered: {}",name);

        recipientPhoneInputField.clear();
        log.info("Recipient phone input field cleared");

        recipientPhoneInputField.sendKeys(phone);
        log.info("Recipient phone entered: {}",name);

        addButton.click();
        log.info("Add button clicked");

    }

    public void updateRecipient(String name, String phone)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Recipient", driver);

        recipientInputNameField.clear();
        log.info("Recipient name input field cleared");

        recipientInputNameField.sendKeys(name);
        log.info("Recipient name entered: {}",name);

        recipientPhoneInputField.clear();
        log.info("Recipient phone input field cleared");

        recipientPhoneInputField.sendKeys(phone);
        log.info("Recipient phone entered: {}",name);

        addButton.click();
        log.info("Add button clicked");

    }

    public void clickAddButton()
    {
        addButton.click();
        log.info("Add button clicked");
    }

    public String getAddedProfile()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Send Money", driver);
        log.info("Retrieving added phone");
        return addedphone.getText();

    }

    public void sendMoney(String amount,String ref)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Send Money", driver);
        amountInputField.clear();
        log.info("Cleared amount input field");
        amountInputField.sendKeys(amount);

        refInputField.clear();
        log.info("Cleared reference input field");
        refInputField.sendKeys(ref);

    }

    public void clickDownloads()
    {
        AndroidActions androidActions = new AndroidActions(driver);
        androidActions.clickWithRetry(By.xpath("//android.widget.TextView[@resource-id=\"android:id/title\" and @text=\"Downloads\"]"),3);
    }

    public void addAttachment()
    {
        AndroidActions androidActions = new AndroidActions(driver);
        attachmentLink.click();
        log.info("Clicked attachments link");
        openFrom.click();
        log.info("Clicked 'Open from'");
        clickDownloads();
        log.info("Clicked downloads");
        androidActions.scrollToTextAndClick2("sample-pdf.pdf",driver);
        log.info("Selected sample-pdf.pdf for upload");
    }

    public void clickSend()
    {
        sendButton.click();
        log.info("Clicked send button");

    }

    public void clickConfirm()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Send money", driver);
        confirmButton.click();
        log.info("Clicked confirm button");

    }

    public String getStatus()
    {
        AppiumUtils.waitForElement(By.id("za.co.neolabs.bankzero:id/trace_id_label"),driver);
        log.info("Retrieving status");
        return status.getText();
    }

    public void clickFinish()
    {
        finishButton.click();
        log.info("Finish button clicked");
    }

    public void getExistingProfile(String name)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Send Money", driver);
        AndroidActions androidActions = new AndroidActions(driver);

        dropDownButton.click();
        log.info("Drop down menu button clicked");
        androidActions.scrollToTextAndClick2(name,driver);

    }

    public boolean getAttachment()
    {
        return driver.findElement(By.xpath("//android.widget.TextView[@text=\"sample-pdf.pdf [84.9kb]\"]")).isDisplayed();
    }

    public void editProfile()
    {
        editProfile.click();
        log.info("Edit profile button clicked");

    }

    public void clickDeleteProfile()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Recipient", driver);
        deleteButton.click();
        log.info("Delete button clicked");

        AppiumUtils.waitForElement(By.id("za.co.neolabs.bankzero:id/alertTitle"),driver);
        log.info("Delete recipient alert displayed");
        driver.findElement(By.id("android:id/button1")).click();
        log.info("Yes button clicked");

    }

    public void clickUpdate()
    {
        AndroidActions androidActions = new AndroidActions(driver);
        try {
            androidActions.clickWithRetry(By.id("za.co.neolabs.bankzero:id/btnSubmit"),3);
            log.info("Update button clicked");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public  void clickBack()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Send Money", driver);
        AndroidActions androidActions = new AndroidActions(driver);
        androidActions.clickWithRetry(new AppiumBy.ByAccessibilityId("Navigate up"),3);
    }

    public void clickRedo()
    {
        redoButton.click();
        log.info("Redo button clicked.");
    }

    public String getProfileName()
    {
        log.info("Retrieving new profile name");
        return getProfileName.getText();
    }



}
