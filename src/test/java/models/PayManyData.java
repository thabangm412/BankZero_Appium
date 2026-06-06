package models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PayManyData {


    String recipientName;
     String group;
     String bank;
     String account;
     String accountNo;
     String popEmail;
     String popPhone;
     String amount;

}
