package utils;

import com.aventstack.extentreports.ExtentTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import pageObjects.app.Registration.RegisterOTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class AppiumUtils {

    protected static AndroidDriver driver;
    private static final Logger log = LoggerFactory.getLogger(AppiumUtils.class);

    public double getFormattedAmount(String amount) {
        return Double.parseDouble(amount.substring(1));

    }

    public List<HashMap<String, String>> getJsonData(String jsonFilePath) throws IOException {
        //convert json file content to json string
        String jsonContent = FileUtils.readFileToString(
                //new File(System.getProperty("user.dir") + "//src//test//java//testData//eCommerce.json"),
                new File(jsonFilePath),
                StandardCharsets.UTF_8
        );
        // Convert JSON string to List<HashMap<String, String>>
        ObjectMapper mapper = new ObjectMapper();
        List<HashMap<String, String>> data = mapper.readValue(
                jsonContent,
                new TypeReference<List<HashMap<String, String>>>() {
                }
        );
        return data;
    }

    public static void waitForTextToAppear(By locator, String expectedText, AndroidDriver driver) {
        int retries = 0;
        while (retries < 3) {
            try {
                new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until((ExpectedCondition<Boolean>) d -> {
                            try {
                                WebElement element = d.findElement(locator);
                                return element != null && expectedText.equals(element.getText());
                            } catch (StaleElementReferenceException e) {
                                return false; // retry on stale
                            }
                        });
                System.out.println("✅ Text appeared as expected: " + expectedText);
                return;
            } catch (TimeoutException e) {
                System.out.println("⏳ Waiting for text '" + expectedText + "' to appear, attempt " + (retries + 1));
                retries++;
            }
        }
        throw new RuntimeException("❌ Failed to find text '" + expectedText + "' after retries.");
    }

    public static void waitForElementToAppear(WebElement element, String attr, String value, AppiumDriver driver) {
        int retries = 0;
        while (retries < 3) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.attributeContains(element, attr, value));
                log.info("✅ Element appeared with attribute '{}' containing value '{}'", attr, value);
                return;
            } catch (TimeoutException e) {
                log.warn("⏳ Waiting for element attribute '{}' to contain '{}', attempt {}", attr, value, retries + 1);
                retries++;
            } catch (StaleElementReferenceException e) {
                log.warn("♻️ Stale element encountered for attribute '{}', retrying... attempt {}", attr, retries + 1);
                retries++;
            }
        }
        log.error("❌ Failed to find element with attribute '{}' containing value '{}' after retries.", attr, value);
        throw new RuntimeException("Failed to find element with attribute '" + attr + "' containing value '" + value + "' after retries.");
    }

    public static void waitForElement(By locator, AppiumDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static String getOTP(String cellNumber, String altNumber) {
        log.info("🔄 Starting OTP retrieval process...");

        // Convert to international format
        if (cellNumber != null && cellNumber.trim().startsWith("0")) {
            cellNumber = "27" + cellNumber.trim().substring(1);
            log.info("📱 Converted cellNumber to international format: {}", cellNumber);
        } else {
            cellNumber = cellNumber.trim();
        }

        boolean hasAlt = altNumber != null && !altNumber.trim().isEmpty();
        if (hasAlt && altNumber.trim().startsWith("0")) {
            altNumber = "27" + altNumber.trim().substring(1);
            log.info("📞 Converted altNumber to international format: {}", altNumber);
        } else if (hasAlt) {
            altNumber = altNumber.trim();
        }

        String altFormattedNumber1 = cellNumber.startsWith("27") ? "+27" + cellNumber.substring(2) : cellNumber;
        String altFormattedNumber2 = hasAlt && altNumber.startsWith("27") ? "+27" + altNumber.substring(2) : "";

        try {
            String urlString = "https://api-qa.bankzerosa.co.za/phoenix-test/zero/otplatest";
            log.info("🌐 Connecting to URL: {}", urlString);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                log.error("❌ HTTP request failed with code: {}", responseCode);
                throw new RuntimeException("HTTP error code: " + responseCode);
            }
            log.info("✅ HTTP request successful.");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                responseBuilder.append(output);
            }
            conn.disconnect();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBuilder.toString());

            if (root.isArray()) {
                log.info("📦 JSON array response. Scanning for matching number...");
                for (JsonNode otpEntity : root) {
                    String phone = otpEntity.path("phoneNumber").asText();
                    if (phone.equals(cellNumber) || phone.equals(altFormattedNumber1) ||
                            (hasAlt && (phone.equals(altNumber) || phone.equals(altFormattedNumber2)))) {

                        String otpString = otpEntity.path("otp").asText();
                        if (otpString.length() == 4) {
                            otpString = "0" + otpString;
                            log.info("ℹ️ OTP was 4 digits. Padded to: {}", otpString);
                        }

                        log.info("✅ Found OTP for {}: {}", phone, otpString);
                        return otpString;
                    }
                }
            } else {
                log.info("📦 Single JSON object response. Checking...");
                String phone = root.path("phoneNumber").asText();
                if (phone.equals(cellNumber) || phone.equals(altFormattedNumber1) ||
                        (hasAlt && (phone.equals(altNumber) || phone.equals(altFormattedNumber2)))) {

                    String otpString = root.path("otp").asText();
                    if (otpString.length() == 4) {
                        otpString = "0" + otpString;
                        log.info("ℹ️ OTP was 4 digits. Padded to: {}", otpString);
                    }

                    log.info("✅ Found OTP for {}: {}", phone, otpString);
                    return otpString;
                }
            }

            log.warn("⚠️ No OTP found for: {}, {}", cellNumber, altNumber);
        } catch (Exception e) {
            log.error("❌ Exception occurred while retrieving OTP: ", e);
        }

        return null;
    }


    public static void optResend(String number, String alnumber) {
        RegisterOTP registerOTP = new RegisterOTP(driver);
        String otpError = registerOTP.getOtpErrorMessg();
        if (otpError != null && otpError.equalsIgnoreCase("Invalid OTP")) {
            System.out.println("❌ Invalid OTP received. Attempting resend...");

            registerOTP.clickResebdOtpButtn();

            // Wait for toolbar to confirm page is ready (adjust condition as needed)
            waitForElementToAppear(
                    driver.findElement(By.id("za.co.neolabs.bankzero:id/toolbar_title")),
                    "enabled", "false", driver
            );

            // Re-enter OTP after resend
            registerOTP.clearField();
            System.out.println("✅ Otp field cleared");
            registerOTP.enterOTP(number, alnumber);
        } else {
            System.out.println("✅ Correct OTP processed");
        }
    }

    public static void pushTestFiles(AndroidDriver driver) throws IOException {
        String[] fileNames = {
                "greenIDBook.jpeg",
                "ZA_Smart_ID_Front.png",
                "passpot.jpeg",
                "licenceFront.jpeg"
        };

        for (String fileName : fileNames) {
            String localPath = "src/test/java/resources/doc/" + fileName;
            String devicePath = "/sdcard/Download/" + fileName;

            if (!isFileAlreadyOnDevice(driver, fileName)) {
                File file = new File(localPath);
                System.out.println("📤 Pushing file to device: " + fileName);
                driver.pushFile(devicePath, file);
            } else {
                System.out.println("✅ File already exists on device: " + fileName);
            }
        }
    }

    private static boolean isFileAlreadyOnDevice(AndroidDriver driver, String fileName) {
        try {
            String output = driver.executeScript("mobile: shell", Map.of(
                    "command", "ls",
                    "args", List.of("/sdcard/Download/")
            )).toString();

            return output.contains(fileName);
        } catch (Exception e) {
            System.err.println("⚠️ Error checking file existence: " + e.getMessage());
            return false;
        }
    }

    public String getPageConfirmation(WebElement ele, String attr, String value, AppiumDriver driver) {
        AppiumUtils.waitForElementToAppear(ele, attr, value, driver);
        return ele.getText();
    }


    public String getScreenshotPath(String testCaseName, AppiumDriver driver) throws IOException {
        File source = driver.getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotDir = System.getProperty("user.dir") + File.separator + "reports" + File.separator + "screenshots";
        new File(screenshotDir).mkdirs(); // Create folder if it doesn't exist
        String destinationFile = screenshotDir + File.separator + testCaseName + "_" + timestamp + ".png";
        FileUtils.copyFile(source, new File(destinationFile));
        return destinationFile;

    }

    public void captureScreenshot(String testName, AppiumDriver driver, ExtentTest test) {
        try {
            String screenshotPath = getScreenshotPath(testName, driver);
            test.addScreenCaptureFromPath(screenshotPath, testName);
        } catch (IOException e) {
            log.error("Failed to capture screenshot for test: {}", testName, e);
        }
    }

    public String getBase64Screenshot(AppiumDriver driver) {
        return driver.getScreenshotAs(OutputType.BASE64);
    }

    public static void disableSafeMode(String sshUser, String sshHost, int sshPort, String sshPassword,
                                       String dbUser, String dbPassword, String dbName,
                                       String phoneNumber, String remoteHost, int remotePort, int localPort) {

        Session session = null;

        try {
            log.info("Starting SSH session to {}", sshHost);

            // Establish SSH tunnel
            JSch jsch = new JSch();
            session = jsch.getSession(sshUser, sshHost, sshPort);
            session.setPassword(sshPassword);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            session.setPortForwardingL(localPort, remoteHost, remotePort);
            log.info("SSH tunnel established: localhost:{} -> {}:{}", localPort, remoteHost, remotePort);

            // PostgreSQL connection
            String jdbcUrl = "jdbc:postgresql://localhost:" + localPort + "/" + dbName;
            Properties props = new Properties();
            props.setProperty("user", dbUser);
            props.setProperty("password", dbPassword);

            try (Connection conn = DriverManager.getConnection(jdbcUrl, props)) {
                log.info("Connected to PostgreSQL database: {}", dbName);

                String sql = """
                        UPDATE device
                        SET safemode = false
                        WHERE id IN (
                            SELECT id FROM device
                            WHERE phoneNumber = ?
                              AND active = true
                              AND safemode = true
                            ORDER BY createddate DESC
                            LIMIT 1
                        );
                        """;

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, phoneNumber);
                    int rows = stmt.executeUpdate();
                    log.info("Query executed. Rows updated: {}", rows);
                }
            }

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage(), e);
        } finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
                log.info("SSH session closed.");
            }
        }
    }

    public static String getFutureDateFormatted(int daysToAdd) {
        if (daysToAdd < 0) {
            throw new IllegalArgumentException("Days to add cannot be negative");
        }

        LocalDate futureDate = LocalDate.now().plusDays(daysToAdd);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return futureDate.format(formatter);
    }

    public static String getFutureDate(int daysToAdd)
    {
        String onceOffDate = getFutureDateFormatted(daysToAdd);

        // Parse it using the original formatter
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMM-yy", Locale.ENGLISH);

// Convert to LocalDate and then reformat
        LocalDate date = LocalDate.parse(onceOffDate, inputFormatter);
        return date.format(outputFormatter);

    }
}