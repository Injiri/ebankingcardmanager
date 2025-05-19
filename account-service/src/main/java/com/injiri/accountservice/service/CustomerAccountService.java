package com.injiri.accountservice.service;

import com.injiri.accountservice.model.CustomerAccount;
import com.injiri.accountservice.repository.CustomerAccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerAccountService {

    private final CustomerAccountRepository repository;
    private final CustomerAccountRepository customerAccountRepository;
    private final CardServiceClient cardServiceClient;

    public CustomerAccountService(CustomerAccountRepository repository, CustomerAccountRepository customerAccountRepository, CardServiceClient cardServiceClient) {
        this.repository = repository;
        this.customerAccountRepository = customerAccountRepository;
        this.cardServiceClient = cardServiceClient;
    }

    public CustomerAccount create(CustomerAccount account) {
        //Each customer can have multiple accounts but accounts cannot belong to more than one customer.
        //no exiting account with the same customerId
        if (getAccountsByCustomerId(account.getCustomerId()).stream().anyMatch(existingAccount -> existingAccount.getIban().equals(account.getIban()))) {
            throw new IllegalArgumentException("Account with the same Iban already exists for this customer");
        }
        return repository.save(account);
    }

    public List<CustomerAccount> getAccountsByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId);
    }

    public List<CustomerAccount> getAllAccounts() {
        return repository.findAll();
    }

    /*if the third parameter is null or empty, it will not be used in the search
     but if you provide a non-existing cardAlias, it will return an empty page instead of throwing an error
    Retrieval API that return paginated lists with filters as => Accounts: Iban, Bicswift, Card Alias*/
    public Page<CustomerAccount> searchAccountsByCardIbanBicSwiftAlias(String iban, String bicSwift, String cardAlias, Pageable pageable) {
        if (cardAlias != null && !cardAlias.isEmpty()) {
            // 1. list accountIds with that cardAlias from the Card service API -  this is a call to the card service microservice/module via rest template
            List<Long> accountIds = cardServiceClient.getAccountIdsByCardAlias(cardAlias);
            if (accountIds.isEmpty()) {
                return Page.empty(pageable); //empty page if no accountIds found matching the cardAlias provided
            }
            // 2. query accounts by Iban, BicSwift and Account id
            return customerAccountRepository.findByIbanContainingIgnoreCaseAndBicSwiftContainingIgnoreCaseAndIdIn(iban == null ? "" : iban, bicSwift == null ? "" : bicSwift, accountIds, pageable);
        } else {
            // 3. query accounts by iban and bicSwift
            return customerAccountRepository.findByIbanAndBicSwift(iban == null ? "" : iban, bicSwift == null ? "" : bicSwift, pageable);
        }
    }


}
