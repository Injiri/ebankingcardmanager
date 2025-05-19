package com.injiri.accountservice.controller;

import com.injiri.accountservice.model.CustomerAccount;
import com.injiri.accountservice.service.CustomerAccountService;
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
@RequestMapping("/api/accounts")
public class CustomerAccountController {

    private final CustomerAccountService service;

    public CustomerAccountController(CustomerAccountService service) {
        this.service = service;
    }

    @PostMapping
    public CustomerAccount createAccount(@RequestBody CustomerAccount account) {
        return service.create(account);
    }

    @GetMapping("/customer/{customerId}")
    public List<CustomerAccount> getAccountsByCustomer(@PathVariable Long customerId) {
        return service.getAccountsByCustomerId(customerId);
    }


    //search accounts by (iban and bicSwift) together with support for attaching a cardAlias
    @GetMapping
    public Page<CustomerAccount> searchAccounts(
            @RequestParam(required = false) String iban,
            @RequestParam(required = false) String bicSwift,
            @RequestParam(required = false) String cardAlias,
            Pageable pageable) {
        return service.searchAccountsByCardIbanBicSwiftAlias(iban, bicSwift, cardAlias, pageable);
    }
}
