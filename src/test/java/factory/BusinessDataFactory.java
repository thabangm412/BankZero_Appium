package factory;

import models.BusinessData;
import models.BusinessRegData;
import models.User;

public class BusinessDataFactory {
    public static BusinessRegData validBusinessRegData() {
        return BusinessRegData.builder()
                .user(new User("Reo", "11111"))
                .businessType("Pty (Ltd)")
                .tradingName("Business Test")
                .registeredName("Business (Pty) Ltd")
                .registrationNo("2023/087554/07")
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

    public static BusinessData validPayBusinessData() {
        return BusinessData.builder()
                .user(new User("Isagi", "33333"))
                .recipientName1("TymeBank3")
                .recipientName2("TymeBank2")
                .recipientName("OM Payo")
                .group("Business")
                .bank("Tyme Bank Limited")
                .account("Current Account")
                .accountNo1("51001379128")
                .accountNo2("987654321")
                .popEmail("Thabang.monoane+1@gmail.com")
                .popPhone("0676336782")
                .amount("65")
                .redoAmount("40")
                .ref("REF-123456")
                .updateRecipientName("Isagi Business")
                .updateGroup("Clubs")
                .updateAccNo("82000086197")
                .updateAcc("Current Account")
                .updateBank("Bank Zero Mutual Bank")
                .build();
    }

}

