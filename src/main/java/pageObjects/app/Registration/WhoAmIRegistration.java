package pageObjects.app.Registration;

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

public class WhoAmIRegistration {
    protected AndroidDriver driver;

    private static final Logger log = LoggerFactory.getLogger(WhoAmIRegistration.class);

    public WhoAmIRegistration(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/surname")
    private WebElement surnameField;

    //Get new selector here
    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/title_dd_arrow")
    private WebElement dropDownButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/emailaddress")
    private WebElement emailField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/emailaddress_confirm")
    private WebElement confirmEmailField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/fullNames")
    private WebElement allNamesField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/idAttachmentView")
    private WebElement proofOfId;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/taptoedit_funds")
    private WebElement sourceOfFunds;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/taptoedit_wealth")
    private WebElement sourceOfWealth;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/document_label\" and @text=\"Green ID book\"]")
    private WebElement greenBookIdBookButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/document_label\" and @text=\"SA ID card\"]")
    private WebElement saIdCardButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/document_label\" and @text=\"SA driving license\"]")
    private WebElement saDrivingLicenseButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/document_label\" and @text=\"SA passport\"]")
    private WebElement saPassportButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btn_upload_image")
    private WebElement attachmentLink;

    @AndroidFindBy(accessibility = "Show roots")
    private WebElement openFrom;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"android:id/title\" and @text=\"Downloads\"]")
    private WebElement downloadsButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"android:id/title\" and @text=\"greenIDBook.jpeg\"]")
    private WebElement getGreenBookIdBookSelection;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"android:id/title\" and @text=\"ZA_Smart_ID_Front.png\"]")
    private WebElement getSaIdCardSelection;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"android:id/title\" and @text=\"passpot.jpeg\"]")
    private WebElement getSaPassportSelection;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"android:id/title\" and @text=\"licenceFront.jpeg\"]")
    private WebElement getSaDrivingLicenseSelection;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement idTypeFinishButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement incomeSubmissionhButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement salaryOptionButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement nextButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/res_address_label")
    private WebElement residentialAddress;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/res_dwelling_option_dd_arrow")
    private WebElement typrOfDwellingButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"android:id/text1\" and @text=\"Please select\"]")
    private WebElement  cardDeliveryButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/cardDeliveryOption_dd_arrow")
    private WebElement  cardDeliveryDropButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/savingsGoal")
    private WebElement  savingForInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/savingsGoalAmount")
    private WebElement  savingForAmountInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/recovery_cellno")
    private WebElement  recoverCellInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/recovery_cellno_confirm")
    private WebElement  confirmRecoverCellInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/recovery_email_address")
    private WebElement  recoverEmailInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/termsandConditions_checkbox")
    private WebElement  checkbox2;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/toolbarTitle")
    private WebElement status;


    public void selectTitle(String title)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbarTitle"),"Who am I",driver);
        clickDropDownButtn();
        AndroidActions androidActions = new AndroidActions(driver);
        androidActions.scrollToTextAndClick(title);
        log.info("Title selected: {}", title);
    }

    public void clickDropDownButtn()
    {
        dropDownButtn.click();
    }

    public void enterSurname(String name)
    {
        surnameField.clear();
        log.info("Cleared surname input field");
        surnameField.sendKeys(name);
        log.info("Entered surname: {}", name);
    }

    public void enterAllNames(String name)
    {
        allNamesField.clear();
        log.info("Cleared all names input field");
        allNamesField.sendKeys(name);
        log.info("Entered all names: {}", name);
    }

    public void enterEmail(String email)
    {
        emailField.clear();
        log.info("Cleared email input field");
        emailField.sendKeys(email);
        log.info("Entered email: {}", email);
    }

    public void enterConfirmationEmail(String email)
    {
        confirmEmailField.clear();
        log.info("Cleared confirm email input field");
        confirmEmailField.sendKeys(email);
        log.info("Entered confirmed email: {}", email);
    }

    public void clickProofID()
    {
        proofOfId.click();
        log.info("Proof of Id page displayed.");
    }

    public void clickAttachmentLink()
    {
        attachmentLink.click();
        log.info("Attachments hyperlink opened.");
    }

    public void clickDownloads()
    {
        AndroidActions androidActions = new AndroidActions(driver);
        androidActions.clickWithRetry(By.xpath("//android.widget.TextView[@resource-id=\"android:id/title\" and @text=\"Downloads\"]"),3);
    }

    public void confirmDocument(String idType) {
        AndroidActions androidActions = new AndroidActions(driver);
        log.info("Starting document confirmation for ID type: {}", idType);

        switch (idType.toUpperCase()) {
            case "GREEN ID BOOK":
                greenBookIdBookButtn.click();
                log.info("Selected Green ID Book option");

                clickAttachmentLink();
                log.info("Clicked attachment link");

                openFrom.click();
                log.info("Clicked 'Open from'");

                clickDownloads();
                log.info("⬇Navigated to Downloads folder");

                androidActions.scrollToTextAndClick2("greenIDBook.jpeg", driver);
                log.info("Selected greenIDBook.jpeg for upload");

                break;

            case "SA ID CARD":
                saIdCardButtn.click();
                log.info("Selected South African ID option");

                AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/instruction"),"ID card front", driver);

                clickAttachmentLink();
                log.info("Clicked attachment link");

                openFrom.click();
                log.info("Clicked 'Open from'");

                clickDownloads();
                log.info("Navigated to Downloads folder");

                androidActions.scrollToTextAndClick2("ZA_Smart_ID_Front.png", driver);
                log.info("Selected front side of ID card for upload");

                AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/instruction"),"ID card back", driver);
                androidActions.wait(3);

                clickAttachmentLink();
                log.info("Clicked attachment link");

                openFrom.click();
                log.info("Clicked 'Open from'");

                clickDownloads();
                log.info("Navigated to Downloads folder");

                androidActions.scrollToTextAndClick2("ZA_Smart_ID_Reverse.png", driver);
                log.info("Selected back side of ID card for upload");


                break;

            case "SA DRIVING LICENCE":
                saDrivingLicenseButtn.click();
                log.info("Selected SA Driving Licence option");

                AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/instruction"),"Licence front", driver);

                clickAttachmentLink();
                log.info("Clicked attachment link");

                openFrom.click();
                log.info("Clicked 'Open from'");

                clickDownloads();
                log.info("Navigated to Downloads folder");

                androidActions.scrollToTextAndClick2("licenceFront.jpeg", driver);
                log.info("Selected front side of licence for upload");

                AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/instruction"),"Licence back", driver);
                androidActions.wait(3);

                clickAttachmentLink();
                log.info("Clicked attachment link");

                openFrom.click();
                log.info("Clicked 'Open from'");

                clickDownloads();
                log.info("Navigated to Downloads folder");

                androidActions.scrollToTextAndClick2("licenceBack.jpeg", driver);
                log.info("Selected back side of licence for upload");

                break;

            case "SA PASSPORT":
                saPassportButtn.click();
                log.info("Selected SA Passport option");

                clickAttachmentLink();
                log.info("Clicked attachment link");

                openFrom.click();
                log.info("Clicked 'Open from'");

                clickDownloads();
                log.info("Navigated to Downloads folder");

                androidActions.scrollToTextAndClick2("passpot.jpeg", driver);
                log.info("Selected passport image for upload");

                break;

            default:
                log.warn("Unknown ID type provided: {}", idType);
                break;
        }

        log.info("Document confirmation flow completed for ID type: {}", idType);
    }



    public void clickSourceOfFundsButtn()
    {
        sourceOfFunds.click();
    }


    public void clickSourceOfWealthButtn()
    {
        sourceOfWealth.click();
    }

    public void chooseSourceOfWealth(String name)
    {
        clickSourceOfWealthButtn();
        AndroidActions androidActions = new AndroidActions(driver);
        androidActions.scrollToTextsAndClick(name);
    }

    public void chooseSourceOfFunds(String name)
    {
        clickSourceOfFundsButtn();
        AndroidActions androidActions = new AndroidActions(driver);
        androidActions.scrollToTextsAndClick(name);
    }

    public void clickIdFinishButtn()
    {
        idTypeFinishButton.click();
    }

    public void clickIncomeSubButtn()
    {
        incomeSubmissionhButton.click();
    }

    public void clickNextButtn()
    {
        AndroidActions androidActions = new AndroidActions(driver);
        androidActions.clickWithRetry(By.id("za.co.neolabs.bankzero:id/submit_btn"),3);
        log.info("Next button clicked");

        //nextButton.click();
    }

    public void clickConfirmButtn()
    {
        AndroidActions androidActions = new AndroidActions(driver);
        androidActions.clickWithRetry(By.id("za.co.neolabs.bankzero:id/submit_btn"),3);
        log.info("Confirm button clicked");
        //nextButton.click();
    }

    public void clickFinishButtn()
    {
        AndroidActions androidActions = new AndroidActions(driver);
        androidActions.clickWithRetry(By.id("za.co.neolabs.bankzero:id/submit_btn"),3);
        log.info("Finish button clicked");
        //nextButton.click();
    }

    public void clickDwelingButtn(String name)
    {
        AppiumUtils.waitForElementToAppear(residentialAddress,"displayed","true",driver);
        AndroidActions androidActions = new AndroidActions(driver);
        typrOfDwellingButtn.click();
        androidActions.scrollToTextAndClick2(name,driver);
        log.info("Dwelling option chosen: {}",name);
    }

    public void clickCardDelivery()
    {
        cardDeliveryDropButtn.click();
        log.info("Card delivery dropdown button selected.");
    }

    public void addAddresss(String street,String city,String postalCode)
    {
        AndroidActions androidActions = new AndroidActions(driver);
        driver.findElement(By.id("za.co.neolabs.bankzero:id/selected_res_address_2"))
                .sendKeys(street);
        log.info("Street name entered: {}",street);
        driver.findElement(By.id("za.co.neolabs.bankzero:id/selected_res_address_3"))
                .sendKeys(city);
        log.info("City name entered: {}",city);
        driver.findElement(By.id("za.co.neolabs.bankzero:id/resprovince_dd_arrow")).click();
        androidActions.scrollToTextAndClick("Gauteng");
        log.info("Province name entered Gauteng");
        driver.findElement(By.id("za.co.neolabs.bankzero:id/selected_res_address_4"))
                .sendKeys(postalCode);
        log.info("Postal code entered");

    }

    public void enterIamSavingForDetails( String txt, String amount)
    {

        //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbarTitle"),"I am saving for",driver);

        log.info("Starting to enter saving details...");
        savingForInputField.clear();
        log.info("Cleared saving for input field");
        savingForInputField.sendKeys(txt);
        log.info("Entered saving for number: {}", txt);
        savingForAmountInputField.clear();
        log.info("Cleared amount input field");
        savingForAmountInputField.sendKeys(amount);
        log.info("Entered amount: {}", amount);

    }


    public void enterRecoveryDetails(String recoveryCell, String confirmRecoveryCell, String recoveryEmail) {

        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbarTitle"),"My safety",driver);
        log.info("Starting to enter recovery details...");

        recoverCellInputField.clear();
        log.info("Cleared recovery cell number input field");
        recoverCellInputField.sendKeys(recoveryCell);
        log.info("Entered recovery cell number: {}", recoveryCell);

        confirmRecoverCellInputField.clear();
        log.info("Cleared confirm recovery cell number input field");
        confirmRecoverCellInputField.sendKeys(confirmRecoveryCell);
        log.info("Entered confirm recovery cell number: {}", confirmRecoveryCell);

        recoverEmailInputField.clear();
        log.info("Cleared recovery email input field");
        recoverEmailInputField.sendKeys(recoveryEmail);
        log.info("Entered recovery email: {}", recoveryEmail);

    }


    public void enterAppPin(int pin)
    {
        String pinString = String.format("%04d", pin); // handles leading zeros

        // Send each digit to the corresponding input field
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_0\"])[1]")).sendKeys(Character.toString(pinString.charAt(0)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_1\"])[1]")).sendKeys(Character.toString(pinString.charAt(1)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_2\"])[1]")).sendKeys(Character.toString(pinString.charAt(2)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_3\"])[1]")).sendKeys(Character.toString(pinString.charAt(3)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_4\"])[1]")).sendKeys(Character.toString(pinString.charAt(4)));
        log.info("App pin entered:{}",pin);
    }

    public void confirmAppPin(int pin)
    {
        String pinString = String.format("%04d", pin); // handles leading zeros

        // Send each digit to the corresponding input field
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_0\"])[2]")).sendKeys(Character.toString(pinString.charAt(0)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_1\"])[2]")).sendKeys(Character.toString(pinString.charAt(1)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_2\"])[2]")).sendKeys(Character.toString(pinString.charAt(2)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_3\"])[2]")).sendKeys(Character.toString(pinString.charAt(3)));
        driver.findElement(By.xpath("(//android.widget.EditText[@resource-id=\"za.co.neolabs.bankzero:id/pin_4\"])[2]")).sendKeys(Character.toString(pinString.charAt(4)));
        log.info("App pin entered:{}",pin);
    }

    public void clickCardDropButtn()
    {
        cardDeliveryDropButtn.click();
    }

    public void clickCheckBox2()
    {
        AppiumUtils.waitForElement(By.id("za.co.neolabs.bankzero:id/termsConditions_label"), driver);
        checkbox2.click();
        log.info("Terms and conditions checkbox cliked");

    }

    public String getStatus()
    {

        log.info("Retrieving status: {}",status.getText());
        return status.getText();
    }

}
