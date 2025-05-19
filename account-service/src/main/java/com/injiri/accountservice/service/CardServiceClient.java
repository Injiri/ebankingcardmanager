package com.injiri.accountservice.service;

import com.injiri.accountservice.dto.CardDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class CardServiceClient {

    private final RestTemplate restTemplate;

    public CardServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Long> getAccountIdsByCardAlias(String cardAlias) {
        String url = "http://localhost:8080/cards/search?cardAlias=" + cardAlias;
        //mapping the response to CardDto template class within the account-service module/microservice
        CardDto[] cards = restTemplate.getForObject(url, CardDto[].class);
        return Arrays.stream(cards)
                .map(CardDto::getAccountId)
                .toList();
    }
}
