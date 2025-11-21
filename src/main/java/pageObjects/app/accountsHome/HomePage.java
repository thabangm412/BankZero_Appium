package pageObjects.app.accountsHome;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AppiumUtils;

import java.time.Duration;

public class HomePage {

    private static final Logger log = LoggerFactory.getLogger(HomePage.class);

    protected AndroidDriver driver;

    public HomePage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/toolbarTitle\"]")
    private WebElement homePageConfimation;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/mnu_exit")
    private WebElement logoutButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/mnu_whoami")
    private WebElement accountIcon;

    public  String getHomePageConfirm()
    {
        return homePageConfimation.getText();
    }

    public void clickLogoutButtn()
    {
        AppiumUtils.waitForElementToAppear(homePageConfimation, "text", "Accounts", driver);
        logoutButtn.click();
        log.info("Logout Button clicked.");
    }

    public void clickAccountIcon()
    {
        accountIcon.click();
    }
}
