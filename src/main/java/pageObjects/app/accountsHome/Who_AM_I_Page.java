package pageObjects.app.accountsHome;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

public class Who_AM_I_Page {
    protected AndroidDriver driver;

    public Who_AM_I_Page(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/toolbarTitle\"]")
    private WebElement whoAmIConfimation;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/toolbarTitle\"]")
    private WebElement myDetails;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/toolbarTitle\"]")
    private WebElement myFicaDocuments;

    public  String getPageConfirmation()
    {
        return whoAmIConfimation.getText();
    }
}
