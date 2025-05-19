package com.injiri.cardservice.dto;

import com.injiri.cardservice.model.CardType;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardResponse {
    private Long id;
    private String cardAlias;
    private Long accountId;
    private CardType cardType;
    private String pan; // masked
    private String cvv; // masked
}
