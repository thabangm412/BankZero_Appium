package pageObjects.app.addAccount;

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

public class NewAccPage {

    private static final Logger log = LoggerFactory.getLogger(NewAccPage.class);
    protected AndroidDriver driver;

    public NewAccPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/toolbar_title")
    private WebElement pageConfirmation;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/entityAccount_dd_arrow")
    private WebElement newAccDropDownButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/add_existing_account")
    private WebElement addButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/existing_layout")
    private WebElement addButtnLayout;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/_inputText")
    private WebElement accNameInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/goal_amount")
    private WebElement goalAmountInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/input_transfer_amount")
    private WebElement amountInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement submitButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement nextButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/termsandConditions_checkbox")
    private WebElement checkBox;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement confirmButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement finishButtn;


    public void selectAccountType(String accType)
    {
        AndroidActions androidActions = new AndroidActions(driver);
        newAccDropDownButtn.click();
        log.info("Acc type drop down menu displayed");
        androidActions.scrollToTextAndClick2(accType,driver);
        log.info("Account type selected: {}", accType);

    }

    public void clickAdd()
    {
        AppiumUtils.waitForElementToAppear(addButtnLayout,"displayed","true",driver);
        addButtn.click();
        log.info("Add account buuton clicked");
    }

    public void enterSavingsDetails(String name, String goalAmount, String amount)
    {
        //AppiumUtils.waitForElementToAppear(pageConfirmation,"text","Add Savings",driver);
        AppiumUtils.waitForElement(By.id("za.co.neolabs.bankzero:id/toolbar_title"),driver);

        accNameInputField.clear();
        log.info("Account name input field cleared");
        accNameInputField.sendKeys(name);
        log.info("Account name entered");

        goalAmountInputField.clear();
        log.info("Goal amount inout field cleared");
        goalAmountInputField.sendKeys(goalAmount);
        log.info("Goal amount entered");

        amountInputField.clear();
        log.info("Amount input field cleared");
        amountInputField.sendKeys(amount);
        log.info("Amount entered");

        submitButtn.click();
    }

    public void clickCheckbox()
    {
        AppiumUtils.waitForElement(By.id("za.co.neolabs.bankzero:id/tnc_headertext"),driver);
        checkBox.click();
        log.info("Checkbox clicked");
        confirmButtn.click();
    }

    public void clickNext()
    {
        nextButtn.click();
        log.info("Next button clicked");
    }

    public void clickSubmit()
    {
        submitButtn.click();
    }

    // Use it for assert
    public String getLatestAccount(String name)
    {
        try {
            List<WebElement> accounts = driver.findElements(By.id("za.co.neolabs.bankzero:id/title"));

            for (WebElement account : accounts) {
                String accountName = account.getText();
                if (accountName.equalsIgnoreCase(name)) {
                    return accountName;
                }
            }

            System.out.println("Matching account not found");
            return null;

        } catch (Exception e) {
            System.out.println("An error occurred while retrieving account: " + e.getMessage());
            return null;
        }
    }

    public void clickFinish()
    {
        finishButtn.click();
        log.info("Finish button clicked");
    }

    public String getAccStatus()
    {
        log.info("Getting account registration status");
        return driver.findElement(By.xpath("//android.widget.TextView[@text=\"Account successfully added.  You can access this account from your canvas.\"]")).getText();
    }
}
