package com.injiri.cardservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Card ID (not editable)

    @Column(nullable = false)
    private String cardAlias; // Editable field

    @Column(nullable = false)
    private Long accountId; // Not editable, foreign key

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardType cardType; // Not editable

    @Column(nullable = false)
    private String pan; // Sensitive, masked by default

    @Column(nullable = false)
    private String cvv; // 3-digit CVV, sensitive, masked by default
}
