package com.injiri.customerservice.controller;

import com.injiri.customerservice.model.Customer;
import com.injiri.customerservice.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController

@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService service;
    private final CustomerService customerService;

    public CustomerController(CustomerService service, CustomerService customerService) {
        this.service = service;
        this.customerService = customerService;
    }


    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        Customer saved = service.createCustomer(customer);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable String id) {
        return service.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(service.getAllCustomers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String id, @Valid @RequestBody Customer customer) {
        if (!service.getCustomerById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        customer.setId(id);
        Customer updated = service.updateCustomer(customer);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
        if (!service.getCustomerById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    //Customers: Name (full text search), date created range.
    @GetMapping("/customers/search")
    public Page<Customer> searchCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return customerService.searchCustomers(name, startDate, endDate, pageable);
    }

}
