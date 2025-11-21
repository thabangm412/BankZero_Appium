package pageObjects.boss;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageObjects.AbstractPage;

public class BossLoginPage extends AbstractPage {

    private static final Logger log = LoggerFactory.getLogger(BossLoginPage.class);


    @FindBy(xpath = "//div[@class='user_card']")
    private WebElement bossLoginPage;

    @FindBy(xpath = "//input[@type='text']")
    private WebElement usernameInputField;

    @FindBy(xpath = "//input[@type='password']")
    private WebElement passwordInputField;

    @FindBy(xpath = "//button[normalize-space()='Login']")
    private WebElement loginButtn;

    public BossLoginPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    public boolean isAt()
    {
        this.wait.until(ExpectedConditions.visibilityOf(this.bossLoginPage));
        return this.bossLoginPage.isDisplayed();
    }

    public void goToLink(String url)
    {
        this.driver.get(url);
        log.info("Boss link opened");
    }

    public void  enterBossLoginDetails(String username, String passwaord)
    {
        this.usernameInputField.clear();
        log.info("Username input field cleared");
        this.usernameInputField.sendKeys(username);
        log.info("Username entered: {}", username);

        this.passwordInputField.clear();
        log.info("Password input field cleared");
        this.passwordInputField.sendKeys(passwaord);
        log.info("Password entered: {}",passwaord);
    }

    public void clickBossLogin()
    {
        this.loginButtn.click();
        log.info("Boss login button clicked");
    }




}
