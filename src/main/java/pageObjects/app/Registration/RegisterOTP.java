package pageObjects.app.Registration;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AndroidActions;
import utils.AppiumUtils;

import java.time.Duration;

public class RegisterOTP {
    protected AndroidDriver driver;

    private static final Logger log = LoggerFactory.getLogger(RegisterOTP.class);

    public RegisterOTP(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/otpInput")
    private WebElement  otpField;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/toolbar_title")
    private WebElement registerOptPage;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/otpRequestStatus")
    private WebElement invalidOtpMessg;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/btnRequestOTP")
    private WebElement reSendOtpLink;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/submit_btn")
    private WebElement otpCofirmButtn;

    @AndroidFindBy(id = "za.co.neolabs.bankzero:id/otpSentTo")
    private WebElement otpLocator;

    public void enterOTP(String cellNumber, String altNumber) {

        AppiumUtils.waitForElementToAppear(otpLocator ,"displayed","true",driver);

        String otp = AppiumUtils.getOTP(cellNumber, altNumber); // Now a String

        if (otp != null) {
            log.info("OTP retrieved successfully: {}", otp);

            AndroidActions androidActions = new AndroidActions(driver);
            androidActions.sendKeysAction(otpField, otp); // Use string directly
            log.info("OTP entered into the field: {}", otp);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String errorMsg = getOtpErrorMessg();
            log.info("OTP error message received: '{}'", errorMsg);

            if ("Invalid OTP".equalsIgnoreCase(errorMsg != null ? errorMsg.trim() : "")) {
                log.warn("Invalid OTP received. Initiating resend process...");

                clickResebdOtpButtn();
                log.info("Resend OTP button clicked");
                clearField();
                log.info("OTP input field cleared");

                otp = AppiumUtils.getOTP(cellNumber, altNumber);
                if (otp != null) {
                    log.info("New OTP retrieved after resend: {}", otp);
                    androidActions.sendKeysAction(otpField, otp);
                    log.info("New OTP entered successfully after resend");
                } else {
                    log.error("Failed to retrieve new OTP after resend.");
                }
            } else {
                log.info("OTP validated successfully. No error message detected.");
            }
        } else {
            log.error("OTP not retrieved. Cannot proceed with authentication.");
        }
    }



    public String getOtpErrorMessg()
    {
        //return invalidOtpMessg.getText();
        try {
            if (invalidOtpMessg.isDisplayed()) {
                return invalidOtpMessg.getText().trim();
            }
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            // Element not present or already gone — no error message shown
        }
        return null;

    }

    public void sendOtp(String otp)
    {
        otpField.sendKeys(otp);
    }

    public String getOtpPage()
    {
        return registerOptPage.getText();
    }

    public void clickResebdOtpButtn()
    {
        reSendOtpLink.click();
    }

    public void clickSubmitButton()
    {
        try {
            otpCofirmButtn.click();
            log.info("OTP Confirm button clicked.");
        } catch (Exception e) {
            log.warn("Could not click OTP Confirm button: {}", e.getMessage());
        }
    }

    public void clearField()
    {
        otpField.clear();
    }
}
