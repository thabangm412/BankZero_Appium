package pageObjects.app.addAccount;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AppiumUtils;

import java.time.Duration;
import java.util.List;

public class AddAccountPage {

    private static final Logger log = LoggerFactory.getLogger(AddAccountPage.class);

    protected AndroidDriver driver;

    public AddAccountPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(xpath = "(//android.widget.FrameLayout[@resource-id=\"za.co.neolabs.bankzero:id/tile_cardview\"])[5]")
    private WebElement addAccButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/toolbar_title")
    private WebElement addAccPage;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/add_account_text")
    private WebElement newAccButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/add_business_text")
    private WebElement newBussinessButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/add_child_text")
    private WebElement childAccButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/invite_someone_text")
    private WebElement inviteSomeoneButtn;

    @AndroidFindBy(accessibility = "Navigate up")
    private WebElement backButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/toolbarTitle\"]")
    private WebElement homePageConfimation;

    public void clickAddAccButtn()
    {
        AppiumUtils.waitForElementToAppear(homePageConfimation,"text","Accounts",driver);


        List<WebElement> accounts = driver.findElements(AppiumBy.id("za.co.neolabs.bankzero:id/main_tile"));
        int accountsSize = accounts.size();
        log.info("Total number of account elements found: {} ",accountsSize);

        if (accountsSize > 0) {
            int middleIndex = accountsSize / 2;
            log.info("Index of the middle account element to be clicked: {}",middleIndex);

            WebElement addAccount = accounts.get(middleIndex);
            log.info("Attempting to click on the middle account element...");

            addAccount.click();
            log.info("Successfully clicked on the middle account element.");
        } else {
            log.warn("No account elements found. Skipping click action.");
        }
    }

    public String getPageConfirmation()
    {
        AppiumUtils.waitForElementToAppear(addAccPage,"text","Add", driver);
        return addAccPage.getText();
    }

    public void clickChildButtn()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Add",driver);
        childAccButtn.click();
        log.info("Child account button clicked");
    }

    public void newBusinessButtn()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Add",driver);
        newBussinessButtn.click();
        log.info("New business account button clicked");
    }

    public void clickNewAccount()
    {
        newAccButtn.click();
    }




}
