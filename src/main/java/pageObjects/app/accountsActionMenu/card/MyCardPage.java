package pageObjects.app.accountsActionMenu.card;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.Alert;
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

public class MyCardPage {

    private static final Logger log = LoggerFactory.getLogger(MyCardPage.class);

    protected AndroidDriver driver;

    public MyCardPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/res_address_label")
    private WebElement pageConfimation;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/card_line1_edit")
    private WebElement card1stLineField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/card_line2_edit")
    private WebElement card2ndtLineField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement nextButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Card\"]")
    private WebElement cardAccountButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Card settings\"]")
    private WebElement cardSettingstButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"View Card & PIN\"]")
    private WebElement viewCardButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Replace card\"]")
    private WebElement replaceCardButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Cancel card\"]")
    private WebElement cancelCardButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Lock\"]")
    private WebElement lockCardButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Unlock\"]")
    private WebElement unlockCardButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/autoatmoption_dd_arrow")
    private WebElement atmDropDown;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement updateButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement finishButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/localCardATMValue")
    private WebElement localDailyInput;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/globalCardATMValue")
    private WebElement globalDailyInput;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/onlineperpurchase")
    private WebElement onlineMaxInput;

    public void confirmingBiometrics()
    {
        AppiumUtils.waitForElement(By.id("za.co.neolabs.bankzero:id/viewFinderTitle"),driver);
        finishButtn.click();
        log.info("Biometrics confirmed.");
    }

    public void clickCardMenuActionButton()
    {
        cardAccountButtn.click();
        log.info("Card button clicked");
    }

    public void clickLockCard()
    {
        AppiumUtils.waitForElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout"),driver);
        lockCardButtn.click();
        log.info("Lock card button clicked.");
    }

    public void clickUnlockCard()
    {
        AppiumUtils.waitForElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout"),driver);
        unlockCardButtn.click();
            log.info("Unlock card button clicked.");
    }

    public void clickCardSettings()
    {
        AppiumUtils.waitForElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout"),driver);
        cardSettingstButtn.click();
        log.info("Card settings button clicked.");
    }

    public void clickViewCard()
    {
        AppiumUtils.waitForElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout"),driver);
        viewCardButtn.click();
        log.info("View card button clicked.");
    }

    public String getCurrentPage()
    {
        return  pageConfimation.getText();
    }

    public  void getCard1stLineField(String name)
    {
        AppiumUtils.waitForElementToAppear(pageConfimation,"displayed", "true", driver);
        card1stLineField.clear();
        log.info("Input field cleared");
        card1stLineField.sendKeys(name);
        log.info("Card name entered:{}",name);
    }

    public  void getCard2ndtLineField(String name)
    {
        card1stLineField.sendKeys(name);
    }

    public  void clickNextButtn()
    {
        nextButtn.click();
        log.info("Next button clicked.");
    }

    public void enterCardPin(int pin)
    {
        String pinString = String.format("%04d", pin); // handles leading zeros

        // Send each digit to the corresponding input field
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_0\"])[1]")).sendKeys(Character.toString(pinString.charAt(0)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_1\"])[1]")).sendKeys(Character.toString(pinString.charAt(1)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_2\"])[1]")).sendKeys(Character.toString(pinString.charAt(2)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_3\"])[1]")).sendKeys(Character.toString(pinString.charAt(3)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_4\"])[1]")).sendKeys(Character.toString(pinString.charAt(4)));
        log.info("Card pin entered:{}",pin);
    }

    public void enterConfirmationPin(int pin)
    {
        String pinString = String.format("%04d", pin); // handles leading zeros

        // Send each digit to the corresponding input field
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_0\"])[2]")).sendKeys(Character.toString(pinString.charAt(0)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_1\"])[2]")).sendKeys(Character.toString(pinString.charAt(1)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_2\"])[2]")).sendKeys(Character.toString(pinString.charAt(2)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_3\"])[2]")).sendKeys(Character.toString(pinString.charAt(3)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_4\"])[2]")).sendKeys(Character.toString(pinString.charAt(4)));
        log.info("Card pin entered:{}",pin);
    }

    public String getAlertMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            try {
                String alertTxt = alert.getText();
                log.info("Alert displayed: {}",alertTxt);
                if (alertTxt != null && !alertTxt.isEmpty()) {
                    return alertTxt;
                }
            } catch (Exception ignored) {}
            return null;

    }

    public String getToast1Message() {
        String toastMessage = null;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Wait until the specific toast appears
            WebElement toastMsg = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//android.widget.Toast[@text='Card is now locked']")
            ));

            toastMessage = toastMsg.getText();
            log.info("Toast found: '{}'", toastMessage);

        } catch (Exception e) {
            log.warn("Toast 'Card is now locked' not found within timeout: {}", e.getMessage());
        }

        return toastMessage; // returns null if toast does not appear
    }


//    public String getToast2Message() {
//
//        WebElement toastMsg = driver.findElement(By.xpath("//android.widget.Toast[@text=\"Card is now unlocked\"]"));
//        String toastMessage = toastMsg.getText();
//        return toastMessage; // null if only ignored toasts appeared or timeout reached
//    }

    public String getToast2Message() {
        String toastMessage = null;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Wait until the specific toast appears
            WebElement toastMsg = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//android.widget.Toast[@text='Card is now unlocked']")
            ));

            toastMessage = toastMsg.getText();
            log.info("Toast found: '{}'", toastMessage);

        } catch (Exception e) {
            log.warn("Toast 'Card is now locked' not found within timeout: {}", e.getMessage());
        }

        return toastMessage; // returns null if toast does not appear
    }

    public void clickUpdate()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Card Settings", driver);
        updateButtn.click();
        log.info("Update button clicked.");
    }


    public void enableATM()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Card Settings", driver);
        AndroidActions androidActions = new AndroidActions(driver);
        atmDropDown.click();
        log.info("ATM drop down button clicked.");
        androidActions.scrollToTextAndClick2("Always on",driver);
        clickUpdate();

    }

    public void disableATM()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Card Settings", driver);
        AndroidActions androidActions = new AndroidActions(driver);
        atmDropDown.click();
        log.info("ATM drop down button clicked.");
        androidActions.scrollToTextAndClick2("Always off",driver);
        clickUpdate();

    }

    public void clickFinish()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Thank you", driver);
        finishButtn.click();
        log.info("Finish button clicked.");
    }

    public String getAtmWithdrawUpdate()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Thank you", driver);
        String status = driver.findElement(By.id("za.co.neolabs.bankzero:id/atmcash_textview")).getText();
        log.info("Updated status:{}",status);
        return status;
    }

    public void updateLocalDailyCash(String amount)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Card Settings", driver);
        localDailyInput.clear();
        log.info("Daily cash amount field cleared");
        localDailyInput.sendKeys(amount);
        log.info("Daily cash amount update: {}",amount);
    }

    public String getUpdatedFields(String field) {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Thank you", driver);
        String status = null;
        By locator;

        switch (field.toUpperCase()) {
            case "ON/OFF":
                locator = By.id("za.co.neolabs.bankzero:id/atmcash_textview");
                break;
            case "LOCAL DAILY":
                locator = By.id("za.co.neolabs.bankzero:id/atm_cash_local_limit");
                break;
            case "GLOBAL DAILY":
                locator = By.id("za.co.neolabs.bankzero:id/atm_cash_global_limit");
                break;

            case "ONLINE":
                locator = By.id("za.co.neolabs.bankzero:id/online_limit");
                break;
            default:
                throw new IllegalArgumentException("Unknown field: " + field);
        }

        status = driver.findElement(locator).getText();
        log.info("Updated {}: {}", field, status);

        return status;
    }

    public void updateGlobalDailyCash (String amount)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Card Settings", driver);
        globalDailyInput.clear();
        log.info("Global cash amount field cleared");
        globalDailyInput.sendKeys(amount);
        log.info("Global cash amount update: {}", amount);
    }

    public void onlineMaxUpdate (String amount)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Card Settings", driver);
        onlineMaxInput.clear();
        log.info("Online cash amount field cleared");
        onlineMaxInput.sendKeys(amount);
        log.info("Online cash amount update: {}", amount);
    }

    public boolean getCardDisplayed()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Card details", driver);
        return driver.findElement(By.id("za.co.neolabs.bankzero:id/card_text_front")).isDisplayed();
    }

    public String getCardPin()
    {
        AndroidActions androidActions = new AndroidActions(driver);
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Card details", driver);
        WebElement viewPin = driver.findElement(By.id("za.co.neolabs.bankzero:id/btnViewPIN"));
//        androidActions.clickWithRetry(By.id("za.co.neolabs.bankzero:id/btnViewPIN"),3);
        androidActions.longPressAction(viewPin);
        //viewPin.click();
        log.info("View card pin button clicked");

        String toastMessage = null;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Wait until the specific toast appears
            WebElement toastMsg = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/snackbar_text\"]")
            ));

            toastMessage = toastMsg.getText();
            log.info("Toast found: '{}'", toastMessage);

        } catch (Exception e) {
            log.warn("Toast 'Card is now locked' not found within timeout: {}", e.getMessage());
        }
        return toastMessage; // returns null if toast
    }

    public void clickFinish2()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Card details", driver);
        finishButtn.click();
        log.info("Finish button clicked.");
    }


}
