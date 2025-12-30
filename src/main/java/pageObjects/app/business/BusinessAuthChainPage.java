package pageObjects.app.business;

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

public class BusinessAuthChainPage {

    private static final Logger log = LoggerFactory.getLogger(BusinessAuthChainPage.class);

    protected AndroidDriver driver;
    protected AndroidActions androidActions;

    private final Duration WAIT = Duration.ofSeconds(10);
    public BusinessAuthChainPage(AndroidDriver driver) {
        this.driver = driver;
        this.androidActions = new AndroidActions(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Authorisation chain\"]")
    private WebElement menuAuthChainButtn;

    public  void clickAuthorisationChain()
    {
        try {
            menuAuthChainButtn.click();
            log.info("Authorisation menu action button clicked");

        } catch (Exception e) {
            log.error("Authorisation menu action button not clickable", e);
            throw e;
        }

    }

    public void scrollRightToTile() {

        try {
            String containerResourceId = "za.co.neolabs.bankzero:id/owners_and_officials";
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().resourceId(\"" + containerResourceId + "\"))" +
                            ".setAsHorizontalList()" +
                            ".scrollToEnd(5)" // '5' is the max number of swipes to perform until the end is found
            ));
            log.info("✅ Scrolled right and clicked tile with text");

        } catch (Exception e) {
            log.error("❌ Could not scroll", e);
            throw new NoSuchElementException("Element not found after scrolling: " + e.getMessage());
        }
    }

    public void confirmAbort()
    {
        AppiumUtils.waitForElement(By.xpath("//androidx.appcompat.widget.LinearLayoutCompat[@resource-id=\"za.co.neolabs.bankzero:id/parentPanel\"]"),driver);
        WebElement dontSaveButtn = driver.findElement(By.id("android:id/button2"));
        dontSaveButtn.click();
        log.info("✅ Confirmed dont save channges");
    }
}
