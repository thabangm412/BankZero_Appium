package pageObjects.app.accountsActionMenu;

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

public class AccountMenuActions {

    protected AndroidDriver driver;

    private static final Logger log = LoggerFactory.getLogger(AccountMenuActions.class);

    public AccountMenuActions(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Pay\"]")
    private WebElement payAccountButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Pay many\"]")
    private WebElement payManyAccountButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Transfer\"]")
    private WebElement transferAccountButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Buy\"]")
    private WebElement  buyAccountButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Send Money\"]")
    private WebElement sendMoneyAccountButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Card\"]")
    private WebElement cardAccountButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Friends\"]")
    private WebElement friendsAccountButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Statements & Letters\"]")
    private WebElement statementsAccountButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"FICA Documents\"]")
    private WebElement ficaDocumentsAccountButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Settings\"]")
    private WebElement settingsAccountButtn;


    @AndroidFindBy(accessibility = "Navigate up")
    private WebElement backAccountButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Settings\"]")
    private WebElement pageConfirmation;

    @AndroidFindBy(xpath = "(//android.widget.ImageView[@resource-id=\"za.co.neolabs.bankzero:id/tileMenu\"])[1]")
    private WebElement accountMenuActionsButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/toolbar_title")
    private WebElement pageTitle;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/toolbarTitle\"]")
    private WebElement homePageConfimation;


    public void clickBack()
    {
        AppiumUtils.waitForElementToAppear(backAccountButtn,"clickable", "true",driver);
        backAccountButtn.click();
        log.info("Back button clicked.");
    }


    public void clickAccountMenuActionsButtn()
    {
        AppiumUtils.waitForElementToAppear(homePageConfimation,"text","Accounts",driver);
        AndroidActions androidActions = new AndroidActions(driver);
        By actionMenu = By.xpath("(//android.widget.ImageView[@resource-id=\"za.co.neolabs.bankzero:id/tileMenu\"])[1]");
        androidActions.clickWithRetry(actionMenu,3);
        log.info("Account action menu button clicked.");
    }

    public void clickCardButton()
    {
        cardAccountButtn.click();
        log.info("Card action menu button clicked.");

    }

    public boolean cardWidget()
    {
        return driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout")).isDisplayed();
    }

    public void getPageTitleWait()
    {
        AppiumUtils.waitForElementToAppear(driver.findElement(By.id("za.co.neolabs.bankzero:id/toolbar_title")), "displayed","true",driver);
    }

    public String getPagetitleText()
    {
        return pageTitle.getText();
    }

    public String confirmPage(String pageType) {

        AppiumUtils.waitForElementToAppear(homePageConfimation,"text","Accounts",driver);
        accountMenuActionsButtn.click();
        log.info("Account menu action tab displayed.");

        AppiumUtils.waitForElementToAppear(driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout")), "displayed", "true", driver);

        String pageTitle= "";

        switch (pageType.toUpperCase()) {
            case "PAY":
                log.info("Page type selected: {}",pageType);
                payAccountButtn.click();
                log.info("Pay button clicked.");
                AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Quick Pay",driver);
                pageTitle = getPagetitleText();
                log.info("Page title retrieved.");

                break;

            case "PAY MANY":
                log.info("Page type selected: {}",pageType);
                payManyAccountButtn.click();
                log.info("Pay many button clicked.");
                AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Pay Many",driver);
                pageTitle = getPagetitleText();
                log.info("Page title retrieved.");

                break;

            case "TRANSFER":
                log.info("Page type selected: {}",pageType);
                transferAccountButtn.click();
                log.info("Transfer button clicked.");
                AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Transfer",driver);
                pageTitle = getPagetitleText();
                log.info("Page title retrieved.");

                break;

            case "BUY":
                log.info("Page type selected: {}",pageType);
                buyAccountButtn.click();
                log.info("Buy button clicked.");
                AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Quick Buy",driver);
                pageTitle = getPagetitleText();
                log.info("Page title retrieved.");

                break;

            case "SEND MONEY":
                log.info("Page type selected: {}",pageType);
                sendMoneyAccountButtn.click();
                log.info("Send money button clicked.");
                AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Send Money",driver);
                pageTitle = getPagetitleText();
                log.info("Page title retrieved.");

                break;

            case "CARD":
                log.info("Page type selected: {}",pageType);
                cardAccountButtn.click();
                log.info("Card button clicked.");
                AppiumUtils.waitForElement(By.xpath("/hierarchy/android.widget.FrameLayout"),driver);
                driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Card settings\"]")).click();
                pageTitle = getPagetitleText();
                log.info("Page title retrieved.");

                break;

            case "FRIENDS":
                log.info("Page type selected: {}",pageType);
                friendsAccountButtn.click();
                log.info("Friends button clicked.");
                AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Friends",driver);
                pageTitle = getPagetitleText();
                log.info("Page title retrieved.");

                break;

            case "STATEMENTS":
                log.info("Page type selected: {}",pageType);
                statementsAccountButtn.click();
                log.info("Statements&Letters button clicked.");
                AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Statements & letters",driver);
                pageTitle = getPagetitleText();
                log.info("Page title retrieved.");

                break;

            case "FICA DOCUMENTS":
                log.info("Page type selected: {}",pageType);
                ficaDocumentsAccountButtn.click();
                log.info("Fica documents button clicked.");
                AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"FICA Documents",driver);
                pageTitle = getPagetitleText();
                log.info("Page title retrieved.");

                break;

            case "SETTINGS":
                log.info("Page type selected: {}",pageType);
                settingsAccountButtn.click();
                log.info("Settings button clicked.");
                AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Settings",driver);
                pageTitle = getPagetitleText();
                log.info("Page title retrieved.");

                break;

            default:
                log.warn("Unknown page type provided: {}", pageType);

                break;

        }
        return pageTitle;

    }
}
