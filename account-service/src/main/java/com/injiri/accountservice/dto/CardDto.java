package com.injiri.accountservice.dto;

import lombok.Data;

@Data
public class CardDto {
    private Long id;
    private String cardAlias;
    private Long accountId;
    private String cardType;
    private String pan;
    private String cvv;
}
