package pageObjects.app.addAccount;

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

public class ChildAccPage {

    private static final Logger log = LoggerFactory.getLogger(ChildAccPage.class);

    protected AndroidDriver driver;

    public ChildAccPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/rsa_idNo")
    private WebElement saIdInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/surname")
    private WebElement surnameInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/fullNames")
    private WebElement allNamesInputField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/countryofbirth_dd_arrow")
    private WebElement nationalityDropDownButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/attach_clip")
    private WebElement proofOfIdButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/document_label\" and @text=\"Birth certificate\"]")
    private WebElement birthCertificateButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/document_label\" and @text=\"SA passport\"]")
    private WebElement saPassportButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btn_upload_image")
    private WebElement attachmentLink;

    @AndroidFindBy(accessibility = "Show roots")
    private WebElement openFrom;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"android:id/title\" and @text=\"Downloads\"]")
    private WebElement downloadsButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"android:id/title\" and @text=\"BirthCertificate.jpeg\"]")
    private WebElement birthCertificateSelection;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"android:id/title\" and @text=\"passpot.jpeg\"]")
    private WebElement saPassportSelection;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement submitButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement confirmButtn;

    @AndroidFindBy(xpath = "//android.widget.TextView[@resource-id=\"za.co.neolabs.bankzero:id/toolbarTitle\"]")
    private WebElement homePageConfimation;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement uploadButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement finishButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement nextButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/mnu_login_pair")
    private WebElement addProfileButton;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/editIDNumber")
    private  WebElement idNumberField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/editPhone")
    private  WebElement cellNumberField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/editName")
    private  WebElement preferredNameField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnRegister")
    private  WebElement submitRegButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/surname")
    private  WebElement surnameInput;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/fullNames")
    private  WebElement allNamesInput;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/emailaddress")
    private  WebElement emailInput;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/emailaddress_confirm")
    private  WebElement emailConfirm;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/income_grid")
    private  WebElement sourceIncome;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/wealth_grid")
    private  WebElement sourceWealth;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/cardDeliveryOption_dd_arrow")
    private  WebElement cardDelivery;



    public void enterChildDetails(String id, String surname, String allNames, String nationality)
    {
        //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Add child",driver);
        AndroidActions androidActions = new AndroidActions(driver);

        saIdInputField.clear();
        log.info("SA Id number input field cleared");
        saIdInputField.sendKeys(id);
        log.info("SA Id number entered");

        surnameInputField.clear();
        log.info("Surname input field cleared");
        surnameInputField.sendKeys(surname);
        log.info("Surname entered");

        allNamesInputField.clear();
        log.info("All names input field cleared");
        allNamesInputField.sendKeys(allNames);
        log.info("All names entered");

        nationalityDropDownButtn.click();
        log.info("Nationality drop down menu displayed");
        androidActions.scrollToTextAndClick2(nationality,driver);
        log.info("Nationality chosen: {}", nationality);
    }

    public void addProofOfId(String idType)
    {
        AndroidActions androidActions = new AndroidActions(driver);
        proofOfIdButtn.click();
        log.info("Clicked proof of ID button");
        switch (idType.toUpperCase()) {
            case "BIRTH CERTIFICATE":
                birthCertificateButtn.click();
                log.info("Selected Birth Certificate option");

                attachmentLink.click();
                log.info("Clicked attachment link");

                openFrom.click();
                log.info("Clicked 'Open from'");

                clickDownloads();
                log.info("Navigated to Downloads folder");

                androidActions.scrollToTextAndClick2("passpot.jpeg", driver);
                log.info("Selected birth certificate image for upload");

               // AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbarTitle"), "Birth certificate", driver);

                clickNext();
                clickNext();

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

                //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbarTitle"), "Passport as ID", driver);

                clickUpload();
                clickNext();


                break;

            default:
                System.out.println("Unknown ID type provided: " + idType);
                break;

        }
        log.info("Document confirmation flow completed for ID type: {}", idType);
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
        log.info("Downloads file opened");
    }

    public void clickUpload()
    {
        //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/image_instruction"), "All done. Tap on Finish to continue", driver);
        uploadButtn.click();
        log.info("Upload button clicked");
    }

    public void clickNext()
    {
        nextButton.click();
        log.info("Next button clicked");
    }

    public String getConfirmationText()
    {
        return driver.findElement(By.id("za.co.neolabs.bankzero:id/toolbarTitle")).getText();
    }

    public void clickInvite()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbarTitle"), "Add child", driver);
        submitButtn.click();
        log.info("Invite button clicked");
    }
    public void clickConfirm()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbarTitle"), "Add child", driver);
        submitButtn.click();
        log.info("Invite button clicked");
    }

    public void clickSubmit()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbarTitle"), "Add child", driver);
        submitButtn.click();
        log.info("Invite button clicked");
    }

    public void clickFinishButton()
    {
        finishButton.click();
        log.info("Finish button clicked...");

    }

    public void clickLoginAddAccount()
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbarTitle"), "Login", driver);
        addProfileButton.click();
        log.info("Add profile button clicked");
    }
    public void enterCellNumber(String cell)
    {
        //AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbar_title"),"Pair/Register", driver);
        cellNumberField.clear();
        log.info("Cleared cell number input field");
        cellNumberField.sendKeys(cell);
        log.info("Entered cell number: {}", cell);
    }

    public void enterIdNumber(String id)
    {
        idNumberField.clear();
        log.info("Cleared Id number input field");
        idNumberField.sendKeys(id);
        log.info("Entered Id number: {}", id);
    }

    public void enterPreferredName(String name)
    {
        preferredNameField.clear();
        log.info("Cleared preferred name input field");
        preferredNameField.sendKeys(name);
        log.info("Entered preferred name: {}", name);
    }

    public void clickSubmitReg()
    {
        submitRegButtn.click();
        log.info("Submit button clicked");
    }

    public void enterNames(String name,String alName)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbarTitle"),"Who am I",driver);
        surnameInput.clear();
        log.info("Surname input cleared");
        surnameInput.sendKeys(name);
        log.info("Surname entered");

        allNamesInput.clear();
        log.info("All names input cleared");
        allNamesInput.sendKeys(alName);
        log.info("All names enetered");


    }

    public void enterEmailDetails(String email)
    {
        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbarTitle"),"Who am I",driver);

        emailInput.clear();
        log.info("Email input field cleared");
        emailInput.sendKeys(email);
        log.info("Email entered:{}",email);

        emailConfirm.clear();
        log.info("Confirm email input field cleared");
        emailConfirm.sendKeys(email);
        log.info("Confirm email entered:{}",email);

    }

    public void enterSourceFunds()
    {
        sourceIncome.click();
        log.info("Source of income grid clicked");
        driver.findElement(By.xpath("(//android.widget.CheckBox[@resource-id=\"za.co.neolabs.bankzero:id/listItemChecked\"])[2]"))
                .click();
        log.info("Allowance clicked");
        clickNext();

        sourceWealth.click();
        log.info("Source of wealth grid clicked");
        driver.findElement(By.xpath("(//android.widget.CheckBox[@resource-id=\"za.co.neolabs.bankzero:id/listItemChecked\"])[2]"))
                .click();
        log.info("Allowance clicked");
        clickNext();

    }

    public void cardDelivery()
    {
        AndroidActions androidActions = new AndroidActions(driver);

        AppiumUtils.waitForTextToAppear(By.id("za.co.neolabs.bankzero:id/toolbarTitle"),"Who am I",driver);
        cardDelivery.click();
        log.info("Card delivery dropdown clicked");

        androidActions.scrollToTextAndClick2("Courier to residential address (R119 - R149)",driver);
        driver.findElement(By.id("za.co.neolabs.bankzero:id/cardDeliveryAddressType_dd_arrow"))
                .click();
        log.info("Second card delivery dropdown clicked");
        androidActions.scrollToTextAndClick2("House",driver);

    }

}
