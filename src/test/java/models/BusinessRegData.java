package models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BusinessRegData {
    User user;
    String businessType;
    String tradingName;
    String registeredName;
    String registrationNo;
    String sicGroup;
    String sicIndustry;
    String notifyEmail;
    String city;
    String street;
    String postalCode;
    String successMsg;
    String cardPin;
    String fundsAccount;
}

