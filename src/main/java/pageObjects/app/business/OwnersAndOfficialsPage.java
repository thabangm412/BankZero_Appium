package pageObjects.app.business;

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
import java.util.ArrayList;
import java.util.List;

public class OwnersAndOfficialsPage {

    private static final Logger log = LoggerFactory.getLogger(OwnersAndOfficialsPage.class);

    protected AndroidDriver driver;
    protected AndroidActions androidActions;

    private final Duration WAIT = Duration.ofSeconds(10);

    public OwnersAndOfficialsPage(AndroidDriver driver) {
        this.driver = driver;
        this.androidActions = new AndroidActions(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/menuItemText\" and @text=\"Owners/Officials\"]")
    private WebElement ownersAndOfficialsButton;

    @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/_inputText\"]")
    private WebElement ownerNameInputField;

    @AndroidFindBy(xpath = "//android.widget.ImageView[@resource-id=\"za.co.neolabs.bankzero:id/countryof_dd_arrow\"]")
    private WebElement ownerNationalityButtn;

    @AndroidFindBy(xpath = "//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/owner_phone\"]")
    private WebElement ownerCellPhoneInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnConfirm")
    private WebElement addButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement finishButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement confirmButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/passportoridnumber")
    private WebElement businessRegNoIinputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/name_input")
    private WebElement businessNameInputField;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/businesstype_dd_arrow")
    private WebElement businessTypeDropdown;


    public void clickFinishButton() {
        try {
            finishButtn.click();
            log.info("Finish button clicked");

        } catch (Exception e) {
            log.error("Finish button not clickable", e);
            throw e;
        }
    }

    public void clickConfirmButton() {
        try {
            confirmButtn.click();
            log.info("Confirm button clicked");

        } catch (Exception e) {
            log.error("Confirm button not clickable", e);
            throw e;
        }
    }

    public void clickAddButton() {
        try {
            addButtn.click();
            log.info("Add button clicked");

        } catch (Exception e) {
            log.error("Add button not clickable", e);
            throw e;
        }
    }

    public void saveChanges(){


        WebElement updateButtn= driver.findElement(By.xpath("//android.widget.Button[@resource-id=\"za.co.neolabs.bankzero:id/submit_btn\"]"));

        updateButtn.click();
        log.info("Update button clicked");
        androidActions.attachScreenshot(driver,"Added owner/officials");
    }
    public void clickOwnersAndOfficialsButton() {
        try {
            ownersAndOfficialsButton.click();
            log.info("Owners/Officials button clicked");

        } catch (Exception e) {
            log.error("Owners/Officials button not clickable", e);
            throw e;
        }
    }

    public boolean getOwnersAndOfficialsPage() {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/header_main"), "Who", driver);

        try {
            WebElement cardView = driver.findElement(
                    By.id("za.co.neolabs.bankzero:id/card_view")
            );

            log.info("Owners and Officials page is displayed");
            return cardView.isDisplayed();

        } catch (Exception e) {
            log.error("Owners and Officials page is not displayed", e);
            return false;
        }
    }

    public List<String> getAddedOwnersAndOfficials() {
        AppiumUtils.waitForTextToAppear(By.xpath("//android.widget.TextView[@text=\"Owners & Officials, Authorisers\"]"), "Owners & Officials, Authorisers", driver);
        List<WebElement> ownersAndOfficials = driver.findElements(
                By.xpath("//android.widget.TextView[@index=1]")
        );

        List<String> ownersAndOfficialsNames = new ArrayList<>();

        for (WebElement owner : ownersAndOfficials) {
            ownersAndOfficialsNames.add(owner.getText());
        }
        log.info("Owners and Officials names found: {}", ownersAndOfficialsNames);

        return ownersAndOfficialsNames;

    }

    public void addOwnersAndOfficials(String role, String nationality, String cellNumber, String name) {
        try {
            AppiumUtils.waitForTextToAppear(By.xpath("//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/toolbar_title\"]"), "Owner/Official/Authoriser", driver);
            //enter owner preferred name
            safeClear(ownerNameInputField, "Owner's preferred name");
            safeSendKeys(ownerNameInputField, "Owner's preferred name", name, true);

            //selecting the required roles
            switch (role.toUpperCase()) {
                case "AUTHORISER":
                    WebElement authoriserButtn = driver.findElement(By.xpath("(//android.widget.CheckBox[@resource-id=\"za.co.neolabs.bankzero:id/chk_selected\"])[1]"));
                    authoriserButtn.click();
                    log.info("Authoriser option selected");
                    break;
                case "BENEFICIAL OWNER":
                    WebElement beneficialButtn = driver.findElement(By.xpath("(//android.widget.CheckBox[@resource-id=\"za.co.neolabs.bankzero:id/chk_selected\"])[2]"));
                    beneficialButtn.click();
                    log.info("Beneficial Owner option selected");
                    break;
                case "DIRECTOR":
                    WebElement directorButtn = driver.findElement(By.xpath("(//android.widget.CheckBox[@resource-id=\"za.co.neolabs.bankzero:id/chk_selected\"])[3]"));
                    directorButtn.click();
                    log.info("Director option selected");
                    break;
                case "MANDATED OFFICIAL":
                    WebElement mandatedButtn = driver.findElement(By.xpath("(//android.widget.CheckBox[@resource-id=\"za.co.neolabs.bankzero:id/chk_selected\"])[4]"));
                    mandatedButtn.click();
                    log.info("Mandated Official option selected");
                    break;
                case "SHAREHOLDER":
                    WebElement shareholderButtn = driver.findElement(By.xpath("(//android.widget.CheckBox[@resource-id=\"za.co.neolabs.bankzero:id/chk_selected\"])[5]"));
                    shareholderButtn.click();
                    log.info("Shareholder option selected");
                    break;
                default:
                    log.warn("Unknown role: {} - no action taken", role);
            }

            //selecting nationality and entering cell number
            ownerNationalityButtn.click();
            log.info("Owner's nationality dropdown clicked");
            androidActions.scrollToTextAndClick2(nationality, driver);
            log.info("Owner's nationality selected:{}", nationality);
            safeClear(ownerCellPhoneInputField, "Owner's cell phone number");
            safeSendKeys(ownerCellPhoneInputField, "Owner's cell phone number", cellNumber, true);

            //clicking add button to add owner/official
            addButtn.click();
            log.info("Owner/official add Button clicked");

        } catch (Exception e) {
            log.error("Error selecting owners and officials options", e);
            throw e;
        }


    }

    public void addBusinessOwnersAndOfficials(String role, String nationality, String businessname, String registrationNumber) {
        try {
            AppiumUtils.waitForTextToAppear(By.xpath("//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/toolbar_title\"]"), "Owner/Official/Authoriser", driver);
            //enter owner preferred name
            safeClear(ownerNameInputField, "Owner's preferred name");
            safeSendKeys(ownerNameInputField, "Owner's preferred name", businessname, true);

            WebElement shareholderButtn = driver.findElement(By.xpath("(//android.widget.CheckBox[@resource-id=\"za.co.neolabs.bankzero:id/chk_selected\"])[5]"));
            shareholderButtn.click();
            log.info("Shareholder option selected");

            //clicking member is business owner checkbox
            WebElement businessCheckBox = driver.findElement(By.id("za.co.neolabs.bankzero:id/is_business_checkbox"));
            businessCheckBox.click();
            log.info("Member is business owner checkbox clicked");

            //select business type
            businessTypeDropdown.click();
            androidActions.scrollToTextAndClick2("Pty (Ltd)", driver);
            log.info("Business type selected: Pty (Ltd)");

            //selecting nationality and entering cell number
            ownerNationalityButtn.click();
            log.info("Owner's nationality dropdown clicked");
            androidActions.scrollToTextAndClick2(nationality, driver);
            log.info("Owner's nationality selected:{}", nationality);
            safeClear(businessRegNoIinputField, "Business registration number");
            safeSendKeys(businessRegNoIinputField, "Business registration number", registrationNumber, true);

            safeClear(businessNameInputField, "Business name");
            safeSendKeys(businessNameInputField, "Business name", businessname, true);

            //clicking add button to add owner/official
            addButtn.click();
            log.info("Owner/official add Button clicked");

        } catch (Exception e) {
            log.error("Error selecting owners and officials options", e);
            throw e;
        }

    }

    private void safeClear(WebElement el, String name) {
        try {
            if (el != null) {
                el.clear();
                log.debug("{} input field cleared", name);
            } else {
                log.warn("{} element is null when trying to clear", name);
            }
        } catch (Exception e) {
            log.warn("Exception clearing {}: {}", name, e.getMessage());
            throw e;
        }
    }

    private void safeSendKeys(WebElement el, String name, String value, boolean logVisible) {
        try {
            if (el == null) {
                log.warn("{} element is null when trying to send keys", name);
                return;
            }
            el.sendKeys(value == null ? "" : value);
            if (logVisible) {
                log.debug("{} entered: {}", name, value);
            } else {
                log.debug("{} entered (masked)", name);
            }
        } catch (Exception e) {
            log.error("Failed to enter {}: {}", name, e.getMessage());
            throw e;
        }
    }

}
