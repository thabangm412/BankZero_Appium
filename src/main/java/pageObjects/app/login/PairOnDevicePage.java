package pageObjects.app.login;

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

public class PairOnDevicePage {
    protected AndroidDriver driver;

    private static final Logger log = LoggerFactory.getLogger(PairOnDevicePage.class);

    public PairOnDevicePage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/changeURL")
    private WebElement changeUrlButtn;

    @AndroidFindBy(id ="za.co.neolabs.bankzero:id/environment")
    private WebElement environmentField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/editPhone")
    private  WebElement cellNumberField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/editIDNumber")
    private  WebElement idNumberField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/editName")
    private  WebElement preferredNameField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnRegister")
    private  WebElement submitButtn;

    @AndroidFindBy(id ="za.co.neolabs.bankzero:id/url_dd_arrow" )
    private  WebElement urlDropDownButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnSave")
    private  WebElement saveButton;

    @AndroidFindBy(xpath = "(//android.view.ViewGroup[@resource-id=\"za.co.neolabs.bankzero:id/pin_layout\"])[1]")
    private  WebElement loginPinField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/chk_terms_and_conds")
    private  WebElement acceptTermsAndConditions;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnSubmit")
    private  WebElement pairButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/snackbar_text")
    private  WebElement deviceAlreadyExistMsg;


    public void clickChangeUrl()
    {
        changeUrlButtn.click();
        //System.out.println("Clicking Change URL...");
    }

    public String getEnvironment()
    {
        return environmentField.getText();

    }

    public void enterCellNumber(String cell)
    {
        //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Pair/Register", driver);
        cellNumberField.clear();
        log.info("Cleared cell number input field");
        cellNumberField.sendKeys(cell);
        log.info("Entered cell number: {}", cell);
    }

    public void enterIdNumber(String id)
    {
        idNumberField.clear();
        log.info("Cleared Id number input field");
        idNumberField.sendKeys(id);
        log.info("Entered Id number: {}", id);
    }

    public void enterPreferredName(String name)
    {
        preferredNameField.clear();
        log.info("Cleared preferred name input field");
        preferredNameField.sendKeys(name);
        log.info("Entered preferred name: {}", name);
    }

    public void clickSubmit()
    {
        submitButtn.click();
        log.info("Submit button clicked");
    }

    public void clickDropDownButton()
    {
        urlDropDownButtn.click();
    }

    public void clickSave()
    {
        saveButton.click();
    }

    public void enterLoginPin(int pin)
    {
        AppiumUtils.waitForElementToAppear(driver.findElement(By.id("za.co.neolabs.bankzero:id/new_customer_info")), "text", "Existing registration found", driver);
        log.info("Entering login pin: {}", pin);
        String pinString = String.format("%04d", pin); // handles leading zeros

        // Send each digit to the corresponding input field
        driver.findElement(By.id("za.co.neolabs.bankzero:id/pin_0")).sendKeys(Character.toString(pinString.charAt(0)));
        driver.findElement(By.id("za.co.neolabs.bankzero:id/pin_1")).sendKeys(Character.toString(pinString.charAt(1)));
        driver.findElement(By.id("za.co.neolabs.bankzero:id/pin_2")).sendKeys(Character.toString(pinString.charAt(2)));
        driver.findElement(By.id("za.co.neolabs.bankzero:id/pin_3")).sendKeys(Character.toString(pinString.charAt(3)));
        driver.findElement(By.id("za.co.neolabs.bankzero:id/pin_4")).sendKeys(Character.toString(pinString.charAt(4)));
    }

    public void enterCardPin(int pin)
    {
        log.info("Entering card pin: {}", pin);
        String pinString = String.format("%04d", pin); // handles leading zeros

        // Send each digit to the corresponding input field
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_0\"])[2]")).sendKeys(Character.toString(pinString.charAt(0)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_1\"])[2]")).sendKeys(Character.toString(pinString.charAt(1)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_2\"])[2]")).sendKeys(Character.toString(pinString.charAt(2)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_3\"])[2]")).sendKeys(Character.toString(pinString.charAt(3)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_4\"])[2]")).sendKeys(Character.toString(pinString.charAt(4)));
    }

    public void setAcceptTermsAndConditions()
    {
        AppiumUtils.waitForElementToAppear(driver.findElement(By.id("za.co.neolabs.bankzero:id/chk_terms_and_conds")),"clickable","true", driver);
        AndroidActions androidActions = new AndroidActions(driver);

        androidActions.longPressAction(acceptTermsAndConditions);
        log.info("Accepted T&C's");
    }

    public void clickPairButton()
    {
        pairButton.click();
    }

    public String getDeviceAlreadyExitMsg()
    {
        return deviceAlreadyExistMsg.getText();
    }

    public WebElement cellNumberInput()
    {
        return cellNumberField;
    }

    public WebElement idNumberInput()
    {
        return idNumberField;
    }

    public WebElement prefNameInput()
    {
        return preferredNameField;
    }

}
