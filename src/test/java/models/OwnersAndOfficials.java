package models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OwnersAndOfficials {

    User user;
    String role1;
    String role2;
    String role3;
    String role4;
    String role5;
    String nationality;
    String cellNumber;
    String ownerName;
    String levelA_amount;
    String levelB_amount;
    String levelC_amount;

}
