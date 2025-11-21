package pageObjects.app.login;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AppiumUtils;

import java.time.Duration;
import java.util.List;

public class LoginPage {

    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);

    protected AndroidDriver driver;

    public LoginPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@resource-id=\"za.co.neolabs.bankzero:id/main\"]/android.view.ViewGroup")
    private WebElement loginPageConfimation;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/toolbar_title")
    private WebElement login2PageConfimation;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/forgetMe")
    private WebElement forgetAllButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/item")
    private WebElement profileDescription;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/mnu_login_pair")
    private WebElement addAccountButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/loginPINErrorMsg")
    private WebElement loginErrMsg;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/snackbar_text")
    private WebElement safeModeMsg;

    public String getProfileText()
    {
       return profileDescription.getText();
    }


    public void clickForgetAll()
    {
        forgetAllButtn.click();
    }

    public boolean loginPageConfirm()
    {
        //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Accounts",driver);
        return  loginPageConfimation.isDisplayed();

    }

    public  String getLoginPageConfirm()
    {

        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Login",driver);
        return login2PageConfimation.getText();
    }

    public void enterLoginPin(int pin)
    {

        String pinString = String.format("%04d", pin); // handles leading zeros

        // Send each digit to the corresponding input field
        driver.findElement(By.id("za.co.neolabs.bankzero:id/login_pin_0_0_0")).sendKeys(Character.toString(pinString.charAt(0)));
        driver.findElement(By.id("za.co.neolabs.bankzero:id/login_pin_0_0_1")).sendKeys(Character.toString(pinString.charAt(1)));
        driver.findElement(By.id("za.co.neolabs.bankzero:id/login_pin_0_0_2")).sendKeys(Character.toString(pinString.charAt(2)));
        driver.findElement(By.id("za.co.neolabs.bankzero:id/login_pin_0_0_3")).sendKeys(Character.toString(pinString.charAt(3)));
        driver.findElement(By.id("za.co.neolabs.bankzero:id/login_pin_0_0_4")).sendKeys(Character.toString(pinString.charAt(4)));
        log.info("App pin entered:{}",pin);
    }

    public void clickAddAccount()
    {
        addAccountButtn.click();
        log.info("Add/Pair account button clicked");
    }

    public void loginWithRetry(String name,String pin, int maxRetries) {
        int attempts = 0;
        boolean success = false;

        while (attempts < maxRetries && !success) {
            try {
                attempts++;
                loginAccount(name);
                enterLoginPin(Integer.parseInt(pin));
                success = true; // ✅ If no exception, mark as success

            } catch (NoSuchElementException e) {
                // Critical error → retry won't help
                System.err.println("NoSuchElementException on attempt " + attempts + ": " + e.getMessage());
                throw new RuntimeException(e);

            } catch (Exception e) {
                // Log and retry
                System.err.println("Login attempt " + attempts + " failed: " + e.getMessage());

                if (attempts >= maxRetries) {
                    // ✅ Stop retrying if we've hit the limit
                    throw new RuntimeException("Login failed after " + maxRetries + " attempts", e);
                }

                // Optional: small wait before retry
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }
    }


    public void loginAccount(String name) {
        try {
            log.info("Attempting to log in with account name: {}", name);

            // Find all account elements
            List<WebElement> accounts = driver.findElements(By.id("za.co.neolabs.bankzero:id/item"));
            log.info("Found {} account(s) on screen.", accounts.size());

            boolean matchFound = false;

            for (WebElement account : accounts) {
                String accountName = account.getText();
                log.debug("Checking account name: {}", accountName);

                if (accountName.equalsIgnoreCase(name)) {
                    log.info("Match found. Clicking on account: {}", accountName);
                    account.click();
                    matchFound = true;
                    break; // Stop after clicking the matching account
                }
            }

            if (!matchFound) {
                log.warn("No matching account found for name: {}", name);
            }

        } catch (Exception e) {
            log.error("Exception while trying to log in with account '{}': {}", name, e.getMessage(), e);
            throw new RuntimeException("Error in loginAccount: " + e.getMessage(), e);
        }
    }

    public String getLoginErrMsg()
    {
        return loginErrMsg.getText();
    }

    public String getSafeModeMsg()
    {
        return safeModeMsg.getText();
    }

}
