package pageObjects.app.accountsActionMenu.newAccounts;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AndroidActions;
import utils.AppiumUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WithdrawalPage {
    private static final Logger log = LoggerFactory.getLogger(WithdrawalPage.class);
    protected AndroidDriver driver;

    public WithdrawalPage(AndroidDriver driver) {
        if (driver == null) {
            throw new IllegalStateException("AndroidDriver is NULL. Check DriverManager initialization order.");
        }

        if (driver == null) {
            throw new IllegalStateException("AndroidDriver is NULL. Check DriverManager initialization order.");
        }

        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Withdraw\"]")
    private WebElement withdrawActionMenuButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/amount")
    private WebElement amountField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/reference")
    private WebElement referenceField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/sooner_notice")
    private WebElement tomorrowCheckBox;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnSubmit")
    private WebElement withdrawButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnSubmit")
    private WebElement confirmButton;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/toolbarTitle\"]")
    private WebElement finishButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/toolbar_title")
    private WebElement pageTitle;

     public void clickWithdrawActionMenuButton()
     {
         withdrawActionMenuButton.click();
         log.info("Clicked withdraw action menu button");
     }

     public void clickConfirmButton()
     {
         AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Notice confirm", driver);
         confirmButton.click();
     }

     public void enterAmountDetails()
     {
         AndroidActions androidActions = new AndroidActions(driver);
         AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Give Notice", driver);
         amountField.clear();
         log.info("cleared amount field");
         amountField.sendKeys("35");
         log.info("Entered amount details");

         referenceField.clear();
         log.info("cleared reference field");
         referenceField.sendKeys("Test withdrawal");
         log.info("Entered reference details");

         androidActions.attachScreenshot(driver,"Entered withdrawal details");
     }
     public void clickCheckBox()
     {
         tomorrowCheckBox.click();
         log.info("Clicked tomorrow checkbox");
     }

     public void clickWithdrawButton()
     {
         withdrawButton.click();
         log.info("Clicked withdraw button");
     }

     public void clickFinishButton()
     {
         AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Thank you", driver);
         confirmButton.click();
         log.info("Clicked finish button");
     }

     public boolean isWithdrawalSuccessMessageDisplayed()
     {
         try {
             AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Thank you", driver);
             log.info("Withdrawal successful message appeared");
             return true;
         } catch (Exception e) {
             log.error("Withdrawal successful message did not appear within the expected time", e);
             return false;
         }
     }

//    public String getExpectedAvailabilityMessage(int daysToAdd) {
//        LocalDate expectedDate = LocalDate.now().plusDays(daysToAdd);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM-yy");
//        String formattedDate = expectedDate.format(formatter);
//
//        return "Your money will be available on " + formattedDate + " in your Isagi account";
//    }

    public String getExpectedAvailabilityMessage(int daysToAdd) {
        LocalDate expectedDate = LocalDate.now().plusDays(daysToAdd);

        return "Your money will be available on "
                + AppiumUtils.formatDate(expectedDate)
                + " in your Isagi account";
    }

//    public String getTransactionMessage()
//    {
//        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Thank you", driver);
//        return driver.findElement(By.xpath("//android.widget.TextView[@text=\"\t• Your money will be available on 20 Jun-26 in your Isagi account\"]")).getText();
//    }

    public String getTransactionMessage() {
        List<WebElement> messages = driver.findElements(
                AppiumBy.className("android.widget.TextView")
        );

        for (WebElement message : messages) {
            String text = message.getText();

            if (text.contains("Your money will be available on")) {
                return text.replaceFirst("^\\s*•\\s*", "").trim();
            }
        }

        throw new NoSuchElementException("Transaction message not found");
    }


}
