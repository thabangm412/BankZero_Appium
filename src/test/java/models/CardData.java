package models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CardData
{
      String cardPin;
      String cardNumber;
}
