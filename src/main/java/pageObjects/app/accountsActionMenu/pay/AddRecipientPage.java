package pageObjects.app.accountsActionMenu.pay;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import utils.AndroidActions;
import utils.AppiumUtils;

import java.time.Duration;

public class AddRecipientPage {
    protected AndroidDriver driver;

    public AddRecipientPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/toolbarTitle\"]")
    private WebElement pageConfimation;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnSubmit")
    private WebElement addRecipientButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/account_name")
    private WebElement recipientNameField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/group_dd_arrow")
    private WebElement groupDropDownButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/bank_label")
    private WebElement bankButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/sars_label")
    private WebElement sarsButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/accountType_dd_arrow")
    private WebElement accTypeDropDownButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/bank_dd_arrow")
    private WebElement bankDropDownButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/account_code")
    private WebElement accountNoField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/notify_email")
    private WebElement emailInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/notify_email")
    private WebElement cellNumberField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/schedule_clock_instruct")
    private WebElement schedulePayment;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/schedule_type_dd_arrow")
    private WebElement scheduleTypeDropDownButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/fromdate")
    private WebElement fromDateButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/reference")
    private WebElement refInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/amount")
    private WebElement amountInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnSubmit")
    private WebElement submitButtn;



    public void addRecipientName(String name)
    {
        recipientNameField.sendKeys(name);
    }

    public void SelectGroup(String groupTxt)
    {
        AndroidActions androidActions = new AndroidActions(driver);
        groupDropDownButtn.click();
        androidActions.scrollToTextAndClick(groupTxt);

    }

    public void enterBankDetails(String bank, String accType, String accNumber)
    {
        AndroidActions androidActions = new AndroidActions(driver);
        bankDropDownButtn.click();
        androidActions.scrollToTextAndClick(bank);
        accTypeDropDownButtn.click();
        androidActions.scrollToTextAndClick(accType);
        accountNoField.sendKeys(accNumber);
        submitButtn.click();

    }
    public void enterProofOfPayment(String email, String cellNumber)
    {
        emailInputField.sendKeys(email);
        cellNumberField.sendKeys(cellNumber);
    }

    public void addScheduledPaymentDetails(String scheduleType, String date, String amount)
    {
        AndroidActions androidActions = new AndroidActions(driver);
        scheduleTypeDropDownButtn.click();
        // Only accommodate onceOff for now
        androidActions.scrollToTextAndClick(scheduleType);
        fromDateButtn.click();
        AppiumUtils.waitForElementToAppear(driver.findElement(By.id("android:id/datePicker")),"displayed","true",driver);
        driver.findElement(AppiumBy.accessibilityId(date)).click();
        driver.findElement(By.id("android:id/button1")).click();
        refInputField.sendKeys("Testing");
        amountInputField.sendKeys(amount);

    }

    public void addAmountAndRef(String amount, String ref)
    {
        amountInputField.sendKeys(amount);
        refInputField.sendKeys(ref);

    }

    public void clickAddButtn()
    {
        submitButtn.click();
    }

    public void clickPayButtn()
    {
        submitButtn.click();
    }










}
