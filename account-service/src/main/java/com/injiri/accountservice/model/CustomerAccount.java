package com.injiri.accountservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String iban;

    @Column(nullable = false)
    private String bicSwift;

    @Column(nullable = false)
    private Long customerId;
}
