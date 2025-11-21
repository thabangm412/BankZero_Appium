import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import pageObjects.app.Registration.RegisterOTP;
import pageObjects.app.login.PairOnDevicePage;
import testConfig.BaseTestsConfig;
import utils.AndroidActions;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class AppiumTests extends BaseTestsConfig {

    @Test
    public void EnvironmentChange()
    {
        PairOnDevicePage pairOnDevicePage = new PairOnDevicePage(driver);
        AndroidActions androidActions = new AndroidActions(driver);
//        String currentEnviornment = pairOnDevicePage.getEnvironment();
//        System.out.println("Current environment is:" + currentEnviornment);
        if( pairOnDevicePage.getEnvironment().equalsIgnoreCase("dev-sa")) {
            pairOnDevicePage.clickChangeUrl();
            pairOnDevicePage.clickDropDownButton();
            //androidActions.scrollToTextAndClick("QA Selby Env");
            driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"android:id/text1\" and @text=\"QA Selby Env\"]")).click();
            pairOnDevicePage.clickSave();
            Assert.assertEquals(pairOnDevicePage.getEnvironment(),"qa");
            System.out.println("New environment is:" + pairOnDevicePage.getEnvironment());
        }else {
            //To update this
            System.out.println("Current environment is not 'Dev-SA ENV'. Skipping environment change.");
        }
    }

    @Test
    //Pairing Test
    public void AppLaunch() throws InterruptedException {

        PairOnDevicePage pairOnDevicePage = new PairOnDevicePage(driver);
        RegisterOTP registerOTP = new RegisterOTP(driver);
        AndroidActions androidActions = new AndroidActions(driver);
        pairOnDevicePage.clickChangeUrl();
        pairOnDevicePage.clickDropDownButton();
        driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"android:id/text1\" and @text=\"QA Selby Env\"]")).click();
        pairOnDevicePage.clickSave();
        Assert.assertEquals(pairOnDevicePage.getEnvironment(),"qa");

        String enteredPhone = "0862961777"; // Entered phone number in the app
        String recoveryPhone = "0865123456";
        String enteredIdNumber = "9108291857086"; // Entered ID number (not used in comparison)
        String enteredName = "AutomationTest1"; // Entered name (not used in comparison)
        int loginPin = 22222;
        int cardPin = 22222;

        // Enter user inputs into the app
        driver.findElement(By.id("za.co.neolabs.bankzero:id/editPhone")).sendKeys(enteredPhone);
        driver.findElement(By.id("za.co.neolabs.bankzero:id/editIDNumber")).sendKeys(enteredIdNumber);
        driver.findElement(By.id("za.co.neolabs.bankzero:id/editName")).sendKeys(enteredName);
        driver.findElement(By.id("za.co.neolabs.bankzero:id/btnRegister")).click();
        waitForElementToAppear(driver.findElement(By.id("za.co.neolabs.bankzero:id/new_customer_info")), "text", "Existing registration found", driver);
        //driver.findElement(By.xpath("(//android.view.ViewGroup[@resource-id=\"za.co.neolabs.bankzero:id/pin_layout\"])[1]")).sendKeys("1233");
        pairOnDevicePage.enterLoginPin(loginPin);
        pairOnDevicePage.enterCardPin(cardPin);
        driver.hideKeyboard();
        pairOnDevicePage.setAcceptTermsAndConditions();
        //waitForElementToAppear(driver.findElement(By.id("za.co.neolabs.bankzero:id/toolbar_title")), "text", "Register - OTP", driver);
        registerOTP.enterOTP(enteredPhone,recoveryPhone);
        Thread.sleep(3000);


    }

    @Test
    public void OTPtest()
    {

        String otpResponse = getOtpData("https://api-qa.bankzerosa.co.za/phoenix-test/zero/otplatest");

        OTPData otpData = parseOtpResponse(otpResponse);

        System.out.println("OTP returned: " + otpData.getOtp());

    }

    @Test
    public void accountTest()
    {
        List<WebElement> accounts = driver.findElements(AppiumBy.id("za.co.neolabs.bankzero:id/main_tile"));
        int accountsSize = accounts.size();


    }

    // Method to send HTTP GET request and retrieve the OTP response
    private String getOtpData(String apiUrl) {
        StringBuilder response = new StringBuilder();
        try {
            // Create URL object
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);  // Timeout in milliseconds

            // Read the response
            BufferedReader in = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    // Method to parse OTP XML response
    private OTPData parseOtpResponse(String response) {
        OTPData otpData = new OTPData();
        try {
            // Parse XML response using DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(response));
            Document doc = builder.parse(inputSource);

            // Extract the phone number from the OTP response
            NodeList phoneNumberNode = doc.getElementsByTagName("phoneNumber");
            NodeList otpNode = doc.getElementsByTagName("otp");

            if (phoneNumberNode.getLength() > 0) {
                otpData.setPhoneNumber(phoneNumberNode.item(0).getTextContent());
            }
            if (otpNode.getLength() > 0) {
                otpData.setOtp(otpNode.item(0).getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return otpData;
    }
    // OTP Data object to hold the parsed values
    public static class OTPData {
        private String phoneNumber;
        private String otp;

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }
    }

}
