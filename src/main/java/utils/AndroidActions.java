package utils;

import com.aventstack.extentreports.ExtentTest;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.offset.ElementOption;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import pageObjects.app.login.LoginPage;
import pageObjects.app.login.PairOnDevicePage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.*;

import static org.openqa.selenium.interactions.PointerInput.Kind.TOUCH;
import static org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT;
import static org.openqa.selenium.interactions.PointerInput.Origin.viewport;
import static utils.Listeners.TEST_THREAD;

public class AndroidActions extends AppiumUtils {

    private static final String LOCAL_ATTACHMENTS_DIR = "src/test/java/resources/doc";
    private static final String DEVICE_DOWNLOAD_DIR = "/sdcard/Download/";
    private static final Logger log = LoggerFactory.getLogger(AndroidActions.class);

    AndroidDriver driver;

    public AndroidActions(AndroidDriver driver)
    {
        this.driver = driver;
    }

    public static void copyAttachmentsToDevice(AndroidDriver driver) throws IOException {
        File localDir = new File(LOCAL_ATTACHMENTS_DIR);

        if (!localDir.exists() || !localDir.isDirectory()) {
            throw new IOException("Local attachments directory not found: " + LOCAL_ATTACHMENTS_DIR);
        }

        File[] files = localDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!fileExistsOnDevice(driver, file.getName())) {
                        pushFileToDevice(driver, file);
                    } else {
                        log.info("File already exists on device, skipping: " + file.getName());
                    }
                }
            }
        }
    }

    private static boolean fileExistsOnDevice(AndroidDriver driver, String fileName) {
        try {
            // Execute shell command to check if file exists
            String filePath = DEVICE_DOWNLOAD_DIR + fileName;
            String command = String.format("[ -f \"%s\" ] && echo \"exists\" || echo \"not exists\"", filePath);
            String output = (String) driver.executeScript("mobile: shell", ImmutableMap.of(
                    "command", command,
                    "args", Collections.emptyList()
            ));

            return output.trim().equals("exists");
        } catch (Exception e) {
            log.warn("Error checking file existence on device: " + e.getMessage());
            return false;
        }
    }

    private static void pushFileToDevice(AndroidDriver driver, File file) throws IOException {
        byte[] fileBytes = FileUtils.readFileToByteArray(file);
        String fileBase64 = Base64.getEncoder().encodeToString(fileBytes);
        driver.pushFile(DEVICE_DOWNLOAD_DIR + file.getName(), fileBase64.getBytes());
        log.info("Copied file to device: " + file.getName());
    }

    public void longPressAction(WebElement element)
    {
        ((JavascriptExecutor)driver).executeScript("mobile: longClickGesture",
                ImmutableMap.of("elementId",((RemoteWebElement)element).getId(),
                        "duration",2000));
    }

    public void sendKeysAction(WebElement element, String string)
    {
        element.sendKeys(string);
    }

    public String scrollToText(String visibleText) {
        WebElement element = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().text(\"" + visibleText + "\"))"));
        return element.getText();
    }

    public void scrollToTextAndClick2(String visibleText, AndroidDriver driver) {
        int maxRetries = 3;
        int attempts = 0;

        while (attempts < maxRetries) {
            try {
                log.info("🔍 Attempting to scroll and find text: '{}'. Attempt {}/{}", visibleText, attempts + 1, maxRetries);

                WebElement element = driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true))" +
                                ".scrollIntoView(new UiSelector().text(\"" + visibleText + "\"))"
                ));

                element.click();
                log.info("✅ Successfully clicked on element with text: '{}'", visibleText);
                return;
            } catch (StaleElementReferenceException | NoSuchElementException e) {
                log.warn("⚠️ Attempt {}/{} failed due to element not being stable or found: {}", attempts + 1, maxRetries, e.getMessage());
                attempts++;
            } catch (Exception e) {
                log.error("❌ Unexpected error while scrolling to and clicking on text '{}': {}", visibleText, e.getMessage(), e);
                throw e;
            }
        }

        log.error("❌ Failed to scroll and click on text: '{}' after {} attempts", visibleText, maxRetries);
        throw new RuntimeException("Failed to scroll and click on text: " + visibleText + " after " + maxRetries + " attempts");
    }

    public void scrollToTextAndClick(String visibleText) {
        WebElement element = driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().text(\"" + visibleText + "\"))"
        ));
        element.click();
    }

    public void scrollToTextsAndClick(String... visibleTexts) {
        for (String visibleText : visibleTexts) {
            try {
                WebElement element = driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true))" +
                                ".scrollIntoView(new UiSelector().text(\"" + visibleText + "\"))"
                ));
                element.click();
                System.out.println("✅ Found and clicked: " + visibleText);
                return; // Exit after the first successful click
            } catch (Exception e) {
                System.out.println("⚠️ Could not find: " + visibleText + ", trying next...");
            }
        }

        throw new NoSuchElementException("❌ None of the provided texts were found: " + Arrays.toString(visibleTexts));
    }

    public void swipeToAction(WebElement element, String direction)
    {
        ((JavascriptExecutor)driver).executeScript("mobile: swipeGesture",
                ImmutableMap.of("elementId",((RemoteWebElement)element).getId(),
                        "direction", direction,
                        "percent",0.5));
    }

    public void environmentChange() {
        log.info("Checking current environment before changing...");

        LoginPage loginPage = new LoginPage(driver);
        PairOnDevicePage pairOnDevicePage = new PairOnDevicePage(driver);
        String env = pairOnDevicePage.getEnvironment();

        log.info("Current environment detected: {}", env);

        if (env.equalsIgnoreCase("dev-sa") || env.equalsIgnoreCase("int")) {
            log.info("Changing environment to 'QA Selby Env'...");

            pairOnDevicePage.clickChangeUrl();
            log.info("Clicked change URL.");

            pairOnDevicePage.clickDropDownButton();
            log.info("Clicked drop-down to view environments.");

            driver.findElement(By.xpath("//android.widget.TextView[@resource-id='android:id/text1' and @text='QA Selby Env']")).click();
            log.info("Selected 'QA Selby Env' from the list.");

            pairOnDevicePage.clickSave();
            log.info("Clicked Save to confirm environment change.");

            String newEnv = pairOnDevicePage.getEnvironment();
            Assert.assertEquals(newEnv, "qa");
            log.info("Environment successfully changed to: {}", newEnv);

            try {
                WebElement addAccountButton = driver.findElement(By.id("za.co.neolabs.bankzero:id/mnu_login_pair"));
                if (addAccountButton.isDisplayed()) {
                    log.info("Add account button is displayed.");
                    loginPage.clickAddAccount();
                    log.info("Add account button clicked.");
                }
            } catch (NoSuchElementException e) {
                log.warn("Add account button is not displayed. Skipping add account step.");
            }
        } else {
            log.warn("Current environment '{}' is not eligible for change. Skipping environment change.", env);
        }
    }

    public void loginEnvironmentCheck() {
        log.info("Checking current environment before changing...");

        LoginPage loginPage = new LoginPage(driver);
        PairOnDevicePage pairOnDevicePage = new PairOnDevicePage(driver);
        String env = pairOnDevicePage.getEnvironment();

        log.info("Current environment detected: {}", env);

        if (env.equalsIgnoreCase("dev-sa") || env.equalsIgnoreCase("int")) {
            log.info("Changing environment to 'QA Selby Env'...");

            pairOnDevicePage.clickChangeUrl();
            log.info("Clicked change URL.");

            pairOnDevicePage.clickDropDownButton();
            log.info("Clicked drop-down to view environments.");

            driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"android:id/text1\" and @text=\"QA Selby Env\"]")).click();
            log.info("Selected 'QA Selby Env' from the list.");

            pairOnDevicePage.clickSave();
            log.info("Clicked Save to confirm environment change.");

            String newEnv = pairOnDevicePage.getEnvironment();
            Assert.assertEquals(newEnv, "qa");
            log.info("Environment successfully changed to: {}", newEnv);

        } else {
            log.warn("Current environment '{}' is not eligible for change. Skipping environment change.", env);
        }
    }

    public void clearPhoneData(String cellNumber) {
        String urlString = "https://api-qa.bankzerosa.co.za/phoenix-test/zero/clearUserData?phonenumbers=" + cellNumber;
        log.info("🔄 Attempting to clear user data for phone number: {}", cellNumber);

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            log.info("🌐 HTTP GET request sent to URL: {}", urlString);
            log.info("📥 Response Code: {}", responseCode);

            if (responseCode != 200) {
                log.error("❌ Failed to clear data. HTTP error code: {}", responseCode);
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder responseBuilder = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                responseBuilder.append(output);
            }

            conn.disconnect();

            String response = responseBuilder.toString();
            log.info("✅ Response received: {}", response);

            if (response.contains("CLEARED")) {
                log.info("🎉 Successfully cleared user data for: {}", cellNumber);
            } else {
                log.warn("⚠️ Unexpected response for clearing data: {}", response);
            }

        } catch (Exception e) {
            log.error("❗ Exception occurred while clearing user data for {}: {}", cellNumber, e.getMessage(), e);
        }
    }

    public void closeApp()
    {
        driver.terminateApp("za.co.neolabs.bankzero");
        log.info("Bankzero App closed");
    }

    public void wait(int timeInSeconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeInSeconds));
    }

    public void clickWithRetry(By locator, int maxRetries) {
        int retries = 0;
        while (retries < maxRetries) {
            try {
                WebElement element = driver.findElement(locator);
                element.click();
                return; // Success
            } catch (StaleElementReferenceException e) {
                System.out.println("⚠️ StaleElementReferenceException caught. Retrying... (" + (retries + 1) + ")");
                retries++;
                try {
                    Thread.sleep(500); // Small delay before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new RuntimeException("❌ Failed to click element after " + maxRetries + " attempts due to StaleElementReferenceException.");
    }

    public String getTextWithRetry(By locator, int maxRetries) {
        int retries = 0;
        while (retries < maxRetries) {
            try {
                WebElement element = driver.findElement(locator);
                return element.getText(); // Success
            } catch (StaleElementReferenceException e) {
                System.out.println("⚠️ StaleElementReferenceException caught while getting text. Retrying... (" + (retries + 1) + ")");
                retries++;
                try {
                    Thread.sleep(500); // Small delay before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new RuntimeException("❌ Failed to get text from element after " + maxRetries + " attempts due to StaleElementReferenceException.");
    }

    public void clickWithRetries(WebElement element, WebDriver driver) {
        int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                element.click();
                log.info("Clicked element successfully on attempt {}", attempt + 1);
                break; // Success, exit the loop
            } catch (Exception e) {
                attempt++;
                log.warn("Click attempt {} failed: {}", attempt, e.getMessage());

                // Optional: check for popup/toast/alert containing "Bad gateway"
                if (isBadGatewayPopupPresent(driver)) {
                    log.warn("'Bad gateway' popup detected. Retrying click...");
                } else {
                    // If not due to Bad Gateway, break early to avoid unnecessary retries
                    log.warn("Failure not related to 'Bad gateway'. Stopping retries.");
                    break;
                }

                try {
                    Thread.sleep(1000); // Optional: wait between retries
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public boolean isBadGatewayPopupPresent(WebDriver driver) {
        try {
            // Update locator as per your actual popup/message element
            List<WebElement> popups = driver.findElements(By.xpath("//*[contains(text(), 'Bad gateway')]"));
            return !popups.isEmpty();
        } catch (Exception e) {
            log.warn("Error while checking for 'Bad gateway' popup: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTextNotPresentInDropDown(WebElement element,String text) {
        try {
            //Click to open dropdown
            element.click();
            log.info("Dropdown clicked to check for text");
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiSelector().textContains(\"" + text + "\")"));
            return false; // Text found
        } catch (NoSuchElementException e) {
            return true; // Text not found
        }
    }


    public void validateInputKeys(Map<String, String> input, String... requiredKeys) {
        if (input == null) {
            log.error("Input map is null");
            throw new IllegalArgumentException("Input data cannot be null");
        }
        StringBuilder missing = new StringBuilder();
        Arrays.stream(requiredKeys).forEach(k -> {
            if (input.get(k) == null || input.get(k).trim().isEmpty()) {
                if (missing.length() > 0) missing.append(", ");
                missing.append(k);
            }
        });
        if (missing.length() > 0) {
            log.error("Missing required test input keys: {}", missing);
            throw new IllegalArgumentException("Missing required test input keys: " + missing);
        }
        log.debug("All required keys present in input");
    }


    public void assertTextPresentExact(String exactText) {
        By xpath = By.xpath("//android.widget.TextView[@text=\"" + exactText + "\"]");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(xpath));
            Assert.assertTrue(el.isDisplayed(), "Expected text not visible: " + exactText);
            log.info("Found expected text: {}", maskForLog(exactText));
        } catch (TimeoutException e) {
            log.error("Expected text not found within timeout: {}", exactText);
            Assert.fail("Expected text not found: " + exactText);
        }
    }

    public void assertTextAbscentExtract(String exactText) {
        By xpath = By.xpath("//android.widget.TextView[@text=\"" + exactText + "\"]");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            boolean absent = wait.until(ExpectedConditions.invisibilityOfElementLocated(xpath));
            Assert.assertTrue(absent, "Expected text still present: " + exactText);
            log.info("Confirmed text absent: {}", maskForLog(exactText));
        } catch (TimeoutException e) {
            log.error("Expected text remained visible after timeout: {}", exactText);
            Assert.fail("Expected text still present: " + exactText);
        }
    }

    public String maskForLog(String s) {
        if (s == null) return "";
        if (s.length() <= 20) return s;
        return s.substring(0, 20) + "...";
    }

    public void dragAndDrop(WebElement source, WebElement target) {
        ((JavascriptExecutor) driver).executeScript("mobile: dragAndDrop", ImmutableMap.of(
                "sourceId", ((RemoteWebElement) source).getId(),
                "targetId", ((RemoteWebElement) target).getId()
        ));
        log.info("Dragged element from source to target.");


    }

    public void performDragAndDrop(AppiumDriver driver, WebElement sourceElement, WebElement destinationElement) {
        // Define a pointer for a touch event
        PointerInput finger = new PointerInput(TOUCH, "finger");

        // Create the sequence of actions
        Sequence dragAndDrop = new Sequence(finger, 0);

        // 1. Move to the source element location
        dragAndDrop.addAction(finger.createPointerMove(Duration.ofMillis(0), viewport(), sourceElement.getLocation().getX(), sourceElement.getLocation().getY()));
        // 2. Press down (long press is often needed to initiate a drag)
        dragAndDrop.addAction(finger.createPointerDown(LEFT.asArg()));
        // 3. Wait a bit to ensure the element is "grabbed"
        dragAndDrop.addAction(finger.createPointerMove(Duration.ofMillis(3000), viewport(), sourceElement.getLocation().getX(), sourceElement.getLocation().getY()));

        // 4. Move to the destination element location
        dragAndDrop.addAction(finger.createPointerMove(Duration.ofMillis(3000), viewport(), destinationElement.getLocation().getX(), destinationElement.getLocation().getY()));
        // 5. Release the press
        dragAndDrop.addAction(finger.createPointerUp(LEFT.asArg()));

        // Perform the combined action
        driver.perform(Arrays.asList(dragAndDrop));
    }

    public void performLongPressAndDragDrop(AndroidDriver driver, WebElement sourceElement, WebElement destinationElement) {
        try {
            // Create a new TouchAction instance, passing the driver
            TouchAction action = new TouchAction(driver);

            // Chain the actions:
            action.longPress(LongPressOptions.longPressOptions() // Use LongPressOptions for clarity and duration control
                            .withElement(ElementOption.element(sourceElement))
                            .withDuration(Duration.ofMillis(5000))) // Specify the hold duration (e.g., 2 seconds)
                    .moveTo(ElementOption.element(destinationElement)) // Move to the destination element
                    .release() // Lift the finger
                    .perform(); // Execute the entire action sequence

           // System.out.println("Successfully performed long press and drag to drop action.");
            log.info("Successfully performed long press and drag to drop action.");

        } catch (Exception e) {
            log.error("Failed to perform the gesture: {}", e.getMessage(), e);
            //System.err.println("Failed to perform the gesture: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void safeClear(WebElement el, String name) {
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

    public void safeSendKeys(WebElement el, String name, String value, boolean logVisible) {
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

    public void attachScreenshot(AppiumDriver driver, String screenshotName) {
        try {
            ExtentTest test = getTest();
            if (test != null && driver != null) {
                String base64Screenshot = getBase64Screenshot(driver);
                test.addScreenCaptureFromBase64String(base64Screenshot, screenshotName);
            }
        } catch (Exception e) {
            log.warn("Could not capture screenshot: {0}", e.getMessage());
            ExtentTest test = getTest();
            if (test != null) {
                test.warning("Could not capture screenshot: " + e.getMessage());
            }
        }
    }

     private ExtentTest getTest() {
        return TEST_THREAD.get();
    }

    public static WebElement waitForElementAttribute(
            AndroidDriver driver,
            String xpath,
            String attribute,
            String expectedValue,
            int timeoutSeconds) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

        return wait.until(driver1 -> {
            WebElement element = driver1.findElement(By.xpath(xpath));
            String actualValue = element.getAttribute(attribute);

            if (actualValue != null && actualValue.equals(expectedValue)) {
                return element;
            }
            return null;
        });
    }




}