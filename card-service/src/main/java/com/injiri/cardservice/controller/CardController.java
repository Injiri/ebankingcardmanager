package com.injiri.cardservice.controller;

import com.injiri.cardservice.dto.CardResponse;
import com.injiri.cardservice.model.Card;
import com.injiri.cardservice.service.CardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService service;

    public CardController(CardService service) {
        this.service = service;
    }

    @PostMapping
    public Card createCard(@RequestBody Card card) {
        return service.createCard(card);
    }

    //search cards by accountId -- idally cards linked to an account
    @GetMapping("/account/{accountId}")
    public List<CardResponse> getCardsByAccountId(@PathVariable Long accountId,
                                                  @RequestParam(name = "showSensitive", defaultValue = "false") boolean showSensitive) {
        return service.getCardsByAccountId(accountId, showSensitive);
    }

    // paginated card search api
    @GetMapping("/search")
    public Page<CardResponse> searchCards(
            @RequestParam(required = false) String cardAlias,
            @RequestParam(required = false) String cardType,
            @RequestParam(required = false) String pan,
            @RequestParam(name = "showSensitive", defaultValue = "false") boolean showSensitive,
            Pageable pageable
    ) {
        return service.searchCards(cardAlias, cardType, pan, showSensitive, pageable);
    }
}
