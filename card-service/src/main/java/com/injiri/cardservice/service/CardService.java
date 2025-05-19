package com.injiri.cardservice.service;

import com.injiri.cardservice.dto.CardResponse;
import com.injiri.cardservice.model.Card;
import com.injiri.cardservice.repository.CardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {

    private final CardRepository repository;
    private final AccountServiceClient accountServiceClient;

    public CardService(CardRepository repository, AccountServiceClient accountServiceClient) {
        this.repository = repository;
        this.accountServiceClient = accountServiceClient;
    }

    // note: An account can only have one card of each type, with a maximum of 2 cards.
    public Card createCard(Card card) {
        //validate that accountId exists
        if(!accountServiceClient.doesAccountExist(card.getAccountId())) {
            throw new IllegalArgumentException("Account with ID " + card.getAccountId() + " does not exist.");
        }
        // one card per type per account
        boolean existsOfType = repository.existsByAccountIdAndCardType(card.getAccountId(), card.getCardType());
        if (existsOfType) {
            throw new IllegalArgumentException("A Card of this type already exists for the account.");
        }

        // Enforce max 2 cards per account
        long count = repository.countByAccountId(card.getAccountId());
        if (count >= 2) {
            throw new IllegalArgumentException("An account can only have up to 2 cards.");
        }


        return repository.save(card);
    }


    public List<CardResponse> getCardsByAccountId(Long accountId, boolean showSensitive) {
        return repository.findByAccountId(accountId).stream()
                .map(card -> toCardResponse(card, showSensitive))
                .collect(Collectors.toList());
    }

    private CardResponse toCardResponse(Card card, boolean showSensitive) {
        return CardResponse.builder()
                .id(card.getId())
                .cardAlias(card.getCardAlias())
                .accountId(card.getAccountId())
                .cardType(card.getCardType())
                .pan(showSensitive ? card.getPan() : "**** **** **** " + card.getPan().substring(card.getPan().length() - 4))
                .cvv(showSensitive ? card.getCvv() : "***")
                .build();
    }

    //   The retrieval APIs should return paginated lists with filters=> Cards: Card Alias, Type of card and PAN
    public Page<CardResponse> searchCards(String cardAlias, String cardType, String pan, boolean showSensitive, Pageable pageable) {
        return repository
                .findByCardAliasContainingIgnoreCaseAndCardTypeContainingIgnoreCaseAndPanContainingIgnoreCase(
                        cardAlias == null ? "" : cardAlias,
                        cardType == null ? "" : cardType,
                        pan == null ? "" : pan,
                        pageable
                )
                .map(card -> toCardResponse(card, showSensitive));
    }


}
