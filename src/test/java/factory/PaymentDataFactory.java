package factory;

import models.PayManyData;

public class PaymentDataFactory {

    public static PayManyData validPayManyData() {
        return PayManyData.builder()
                .recipientName("Isagi Business")
                .group("Finance")
                .bank("Bank Zero Mutual Bank")
                .account("Current Account")
                .accountNo("82000086197")
                .popEmail("Thabang.monoane@gmail.com")
                .popPhone("0676336781")
                .amount("40")
                .build();
    }

    public static PayManyData validPayManyData2() {
        return PayManyData.builder()
                .recipientName("OM Payo")
                .group("Clubs")
                .bank("Om Bank Limited")
                .account("Current Account")
                .accountNo("57096931700")
                .popEmail("Thabang.monoane+2@gmail.com")
                .popPhone("0676336782")
                .amount("40")
                .build();
    }


}
