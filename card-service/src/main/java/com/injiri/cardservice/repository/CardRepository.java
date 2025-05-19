package com.injiri.cardservice.repository;

import com.injiri.cardservice.model.Card;
import com.injiri.cardservice.model.CardType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByAccountId(Long accountId);

    Page<Card> findByCardAliasContainingIgnoreCaseAndCardTypeContainingIgnoreCaseAndPanContainingIgnoreCase(
            String cardAlias,
            String cardType,
            String pan,
            Pageable pageable
    );

    boolean existsByAccountIdAndCardType(Long accountId, CardType cardType);

    //accounts linked to a card
    long countByAccountId(Long accountId);

}
