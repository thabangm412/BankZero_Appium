package models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TransferData{

    User user;
    String accountName;
    String amount;
    String ref;
}