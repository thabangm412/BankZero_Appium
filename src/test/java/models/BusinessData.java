package models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BusinessData {
    User user;
    String recipientName1;
    String recipientName2;
    String recipientName;
    String group;
    String bank;
    String account;
    String accountNo1;
    String accountNo2;
    String popEmail;
    String popPhone;
    String amount;
    String redoAmount;
    String ref;
    String updateRecipientName;
    String updateGroup;
    String updateAccNo;
    String updateAcc;
    String updateBank;

}
