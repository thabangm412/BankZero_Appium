import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public static void main(String[] args) {
    int otp = getOTP("06862961777 "); // Pass the full number with international format
    if (otp != -1) {
        System.out.println("🔐 OTP is: " + otp);
    } else {
        System.out.println("❌ Could not retrieve OTP.");
    }
}

public static int getOTP(String cellNumber) {
    try {
        String urlString = "https://api-qa.bankzerosa.co.za/phoenix-test/zero/otplatest";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("❌ Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder responseBuilder = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            responseBuilder.append(output);
        }
        conn.disconnect();

        // Parse the JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseBuilder.toString());

        // Check if the response is an array or object
        if (root.isArray()) {
            for (JsonNode otpEntity : root) {
                String phone = otpEntity.path("phoneNumber").asText();
                if (phone.equals(cellNumber)) {
                    int otp = otpEntity.path("otp").asInt();
                    System.out.println("✅ Found OTP for " + cellNumber + ": " + otp);
                    return otp;
                }
            }
        } else {
            // fallback if it's a single object
            String phone = root.path("phoneNumber").asText();
            if (phone.equals(cellNumber)) {
                int otp = root.path("otp").asInt();
                System.out.println("✅ Found OTP for " + cellNumber + ": " + otp);
                return otp;
            }
        }

        System.out.println("❌ No matching OTP found for " + cellNumber);
    } catch (Exception e) {
        System.err.println("❌ Failed to extract OTP: " + e.getMessage());
    }

    return -1;
}
