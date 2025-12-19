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
import java.util.Locale;

public class BusinessPage {
    private static final Logger log = LoggerFactory.getLogger(BusinessPage.class);

    protected AndroidDriver driver;
    private final AndroidActions androidActions;
    private final Duration WAIT = Duration.ofSeconds(10);
    public BusinessPage(AndroidDriver driver) {
        this.driver = driver;
        this.androidActions = new AndroidActions(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/trading_as_input")
    private WebElement tradingNameField;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/registeredname_input")
    private WebElement registeredNameField;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/trading_registration_no")
    private WebElement registrationNoField;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/sic_groups_dd_arrow")
    private WebElement sicGroups;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/sic_industry_dd_arrow")
    private WebElement sicIndustries;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/trading_tax_number")
    private WebElement taxNoField;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/trading_vat_number")
    private WebElement vatNoField;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/sof_pencil")
    private WebElement sourceOfFunds;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/trading_email_address")
    private WebElement notifyEmail;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/taptoedit_wealth")
    private WebElement sourceOfWealth;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/from_account_dd_arrow")
    private WebElement fromAcc;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/input_transfer_amount")
    private WebElement amountField;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement nextButtn;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/registeredOwnership_dd_arrow")
    private WebElement ownershipDropdown;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/registeredAddressType_dd_arrow")
    private WebElement addressTypeDropdown;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/selected_registered_address_2")
    private WebElement streetInput;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/selected_registered_address_3")
    private WebElement surbubInput;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/cardDeliveryOption_dd_arrow")
    private WebElement cardDropdown;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/card_line1_edit")
    private WebElement card1stName;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/required_text2")
    private WebElement directorButtn;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/required_text3")
    private WebElement shareholderButtn;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/required_text4")
    private WebElement BOButtn;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/required_text5")
    private WebElement MOButtn;
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/tnc_checkbox")
    private WebElement termsAndConditionsButtn;

    public void verifyTermsAndConditions()
    {
        try {
            //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Happy?", driver);
            log.info("Verifying terms and conditions");
            termsAndConditionsButtn.click();
            log.info("Terms and conditions accepted");
        } catch (Exception e) {
            log.error("Terms & Conditions page did not appear as expected", e);
            throw e;
        }
    }

    public void selectOwnersAndOfficials()
    {
        try {
            //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Owners & Officials", driver);
            log.info("Selecting owners and officials options");
            directorButtn.click();
            log.info("Director option selected");
            shareholderButtn.click();
            log.info("Shareholder option selected");
            BOButtn.click();
            log.info("BO option selected");
//            MOButtn.click();
//            log.info("MO option selected");
        } catch (Exception e) {
            log.error("Owners & officials page did not appear as expected", e);
            throw e;
        }
    }



    public void enterCardPin(String cardPin) {
        try {
            //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Card delivery & security", driver);
            log.info("Entering card PIN");
            driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_0\"])[1]")).sendKeys(Character.toString(cardPin.charAt(0)));
            driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_1\"])[1]")).sendKeys(Character.toString(cardPin.charAt(1)));
            driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_2\"])[1]")).sendKeys(Character.toString(cardPin.charAt(2)));
            driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_3\"])[1]")).sendKeys(Character.toString(cardPin.charAt(3)));
            driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_4\"])[1]")).sendKeys(Character.toString(cardPin.charAt(4)));
            log.info("Confirming card PIN");
            driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_0\"])[2]")).sendKeys(Character.toString(cardPin.charAt(0)));
            driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_1\"])[2]")).sendKeys(Character.toString(cardPin.charAt(1)));
            driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_2\"])[2]")).sendKeys(Character.toString(cardPin.charAt(2)));
            driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_3\"])[2]")).sendKeys(Character.toString(cardPin.charAt(3)));
            driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_4\"])[2]")).sendKeys(Character.toString(cardPin.charAt(4)));

        } catch (Exception e) {
            log.error("Card PIN page did not appear as expected", e);
            throw e;
        }
    }


    public void enterCarPinDetails(String cardPin, String confirmPin) {
        try {
            AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Card delivery & security", driver);
            log.info("Entering card PIN details");
            safeClear(taxNoField,"Card PIN");
            safeSendKeys(taxNoField,"Card PIN",cardPin,false);

            safeClear(vatNoField,"Confirm Card PIN");
            safeSendKeys(vatNoField,"Confirm Card PIN",confirmPin,false);
        } catch (Exception e) {
            log.error("Card PIN page did not appear as expected", e);
            throw e;
        }
    }
    public void selectCardOptions(String name) {
        try {

            String cardName =  name.toUpperCase();//AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Bank card", driver);
            log.info("Selecting card delivery option");
            cardDropdown.click();
            androidActions.scrollToTextAndClick2("Courier to residential address (R119 - R149)", driver);

            AppiumUtils.waitForElement(By.id("za.co.neolabs.bankzero:id/card_options"),driver);
            log.info("Entering card customisation details");
            safeClear(card1stName,"Card 1st line name");
            safeSendKeys(card1stName,"Card 1st line name",cardName,true);

        } catch (Exception e) {
            log.error("Card options page did not appear as expected", e);
            throw e;
        }
    }
    public void enterRegisteredAddress(String street, String suburb) {
        try {
            //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Addresses", driver);
            ownershipDropdown.click();
            log.info("Registered ownership dropdown clicked");
            androidActions.scrollToTextAndClick2("Rented from a landlord", driver);
            addressTypeDropdown.click();
            log.info("Registered address type dropdown clicked");
            androidActions.scrollToTextAndClick2("House", driver);

            log.info("Entering street address and suburb details");
            safeClear(streetInput,street);
            safeSendKeys(streetInput,"Street name",street,false);
            safeSendKeys(surbubInput,"Surbub name",suburb,false);
        } catch (Exception e) {
            log.error("Registered address page did not appear as expected", e);
            throw e;
        }
    }
    public void selectFromAccount(String accountName) {
        try {
            fromAcc.click();
            log.info("From account dropdown clicked");
            String xpath = String.format(
                    "//android.widget.TextView[@resource-id='za.co.neolabs.bankzero:id/accountName' and @text='%s']",
                    accountName
            );
            log.info("Selecting from account: {}", accountName);
            AppiumUtils.waitForElement(By.xpath(xpath), driver);
            driver.findElement(By.xpath(xpath)).click();
            log.info("Selected from account: {}", accountName);
        } catch (Exception e) {
            log.error("From account page did not appear as expected", e);
            throw e;
        }
    }
    public void enterNotifyEmail(String email) {
        try {
            notifyEmail.clear();
            log.info("Notify email input field cleared");
            notifyEmail.sendKeys(email);
            log.info("Notify email entered:{}",email);
        } catch (Exception e) {
            log.error("Notify email field not displayed as expected", e);
            throw e;
        }

    }
    public void clickNextButton() {
        try {
            nextButtn.click();
            log.info("Next button clicked");
        } catch (Exception e) {
            log.error("Failed to click Next button", e);
            throw e;
        }
    }

    public void chooseSourceOfFundsAndWealth() {
        try {
            sourceOfWealth.click();
            log.info("Source of wealth dropdown clicked");
            driver.findElement(By.xpath("(//android.widget.CheckBox[@resource-id=\"za.co.neolabs.bankzero:id/listItemChecked\"])[4]"))
                            .click();
            log.info("Selected source of wealth");
            nextButtn.click();
            sourceOfFunds.click();
            log.info("Source of funds dropdown clicked");
            driver.findElement(By.xpath("(//android.widget.CheckBox[@resource-id=\"za.co.neolabs.bankzero:id/listItemChecked\"])[1]"))
                            .click();
            nextButtn.click();
            log.info("Selected source of funds");
        } catch (Exception e) {
            log.error("Failed to select source of wealth", e);
            throw e;
        }
    }
    public void clickSourceOfWealthButtn() {
        try {
            sourceOfWealth.click();
            log.info("Source of wealth button clicked");
        } catch (Exception e) {
            log.error("Failed to click source of wealth button", e);
            throw e;
        }
    }

    public void clickSourceOfFundsButtn() {
        try {
            sourceOfFunds.click();
            log.info("Source of funds button clicked");
        } catch (Exception e) {
            log.error("Failed to click source of funds button", e);
            throw e;
        }
    }
    public void chooseBusiness(String businessType) {
        String normalized = businessType == null ? "" : businessType.trim().toUpperCase(Locale.ROOT);
        log.info("Choosing business type: {}", normalized);
        try {
            switch (normalized) {
                case "PTY (LTD)":
                    //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Pty (Ltd)", driver);
                    WebElement ptyButtn = driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/expanded_list_text\" and @text=\"Pty (Ltd)\"]"));
                    ptyButtn.click();
                    log.info("Pty option selected");
                    break;

                case "CLOSED CORPORATION":
                    AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Closed Corporation", driver);
                    // locate and click Closed Corporation option if needed
                    log.info("Closed Corporation path selected (no implementation)");
                    break;

                default:
                    log.warn("Unknown business type: {} - no action taken", businessType);
            }
        } catch (Exception e) {
            log.error("Error while choosing business type {}", businessType, e);
            throw e;
        }
    }

//    public void ptyBusinessDetails(String tradingName, String registeredName, String registrationNo)
//    {
//        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Who is this", driver);
//        tradingNameField.clear();
//        log.info("Trading name input field cleared");
//        tradingNameField.sendKeys(tradingName);
//        log.info("Trading name entered:{}",tradingName);
//
//        registeredNameField.click();
//        log.info("Registered name input field cleared");
//        registeredNameField.sendKeys(registeredName);
//        log.info("Registered name entered:{}",registeredName);
//
//        registrationNoField.clear();
//        log.info("Registration No input field cleared");
//        registrationNoField.sendKeys(registrationNo);
//        log.info("Registration No entered:{}",registeredName);
//
//    }

    public void ptyBusinessDetails(String tradingName, String registeredName, String registrationNo) {
        try {
            //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"), "Who is this", driver);

            safeClear(tradingNameField, "Trading name");
            safeSendKeys(tradingNameField, "Trading name", tradingName, true);

            safeClear(registeredNameField, "Registered name");
            safeSendKeys(registeredNameField, "Registered name", registeredName, true);

            safeClear(registrationNoField, "Registration No");
            safeSendKeys(registrationNoField, "Registration No", registrationNo, false);
            log.info("PTY business details entered (trading='{}', registered='{}', regNo='{}')",
                    mask(tradingName), mask(registeredName), mask(registrationNo));
        } catch (Exception e) {
            log.error("Failed to enter PTY business details", e);
            throw e;
        }
    }

    public void sicDetails(String sicGroup, String sicIndustry) {
        try {
            sicGroups.click();
            log.info("SIC groups dropdown clicked");
            androidActions.scrollToTextAndClick2(sicGroup, driver);
            log.info("Selected SIC group: {}", sicGroup);

            sicIndustries.click();
            log.info("SIC industries dropdown clicked");
            androidActions.scrollToTextAndClick2(sicIndustry, driver);
            log.info("Selected SIC industry: {}", sicIndustry);
        } catch (Exception e) {
            log.error("Failed to select SIC details", e);
            throw e;
        }
    }

    /* ---------- Helper methods ---------- */

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
//    private void enterPin(String pin, String action) {
//        Objects.requireNonNull(pin, "PIN must not be null");
//        pin = pin.trim();
//        if (pin.isEmpty()) {
//            log.error("{} PIN called with empty value", action);
//            throw new IllegalArgumentException("PIN must not be empty");
//        }
//
//        String masked = mask(pin);
//        log.info("{} app PIN: {}", action, masked);
//
//        WebDriverWait wait = new WebDriverWait(driver, WAIT);
//        try {
//            for (char ch : pin.toCharArray()) {
//                String digit = String.valueOf(ch);
//                By locator = By.xpath(
//                        "//android.widget.Button[@text='" + digit + "'] | " +
//                                "//android.widget.Button[@content-desc='" + digit + "'] | " +
//                                "//*[@text='" + digit + "' and (self::android.widget.Button or self::android.view.View)]"
//                );
//                WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(locator));
//                btn.click();
//                // small implicit pacing to avoid flakiness on slow devices
//                Thread.sleep(120);
//            }
//            log.debug("{} PIN entry completed (masked={})", action, masked);
//        } catch (Exception e) {
//            log.error("{} PIN entry failed", action, e);
//            throw new RuntimeException(action + " PIN failed", e);
//        }
//    }


    private String mask(String s) {
        if (s == null || s.isEmpty()) return "";
        if (s.length() <= 3) return "***";
        return s.substring(0, 1) + "***" + s.substring(s.length() - 1);
    }


}
