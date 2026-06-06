package factory;

import models.BusinessRegData;
import models.User;

public class BusinessDataFactory {
    public static BusinessRegData validBusinessData() {
        return BusinessRegData.builder()
                .user(new User("Reo", "11111"))
                .businessType("Pty (Ltd)")
                .tradingName("Business Test")
                .registeredName("Business (Pty) Ltd\"")
                .registrationNo("2023/087553/07")
                .sicGroup("Construction")
                .sicIndustry("Civil engineering")
                .notifyEmail("testBusiness0@bankzero.co.za")
                .street("4 Georgian Crescent West")
                .city("Bryanston, Sandton")
                .postalCode("2191")
                .successMsg("Account and card successfully added.  You can access this account from your canvas.")
                .cardPin("11111")
                .fundsAccount("Card CHEQUE")
                .build();
    }
}

