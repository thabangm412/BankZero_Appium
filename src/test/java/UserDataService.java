import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserDataService {

    private static final Logger log = LoggerFactory.getLogger(UserDataService.class);

    public static void main(String[] args) {
        // Example cell number to test
        String testCellNumber = "0867047389";
        UserDataService service = new UserDataService();
        service.clearPhoneData(testCellNumber);
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
}
