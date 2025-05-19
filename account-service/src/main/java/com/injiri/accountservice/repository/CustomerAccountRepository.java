package com.injiri.accountservice.repository;

import com.injiri.accountservice.model.CustomerAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Long> {
    List<CustomerAccount> findByCustomerId(Long customerId);

    //find customers account by iban swift code ( ignore case)
    Page<CustomerAccount> findByIbanAndBicSwift(String iban, String bicSwift, Pageable pageable);

    // Ability to retrieve Accounts: Iban, Bicswift, Card Alias ignoring case
    Page<CustomerAccount> findByIbanContainingIgnoreCaseAndBicSwiftContainingIgnoreCaseAndIdIn(String s, String s1, List<Long> accountIds, Pageable pageable);
}
