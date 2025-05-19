package com.injiri.customerservice.service;

import com.injiri.customerservice.model.Customer;
import com.injiri.customerservice.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository repository, CustomerRepository customerRepository) {
        this.repository = repository;
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(Customer customer) {

        //no exiting customer with the same id
        if (!customerRepository.existsById(customer.getId())) {
            throw new IllegalArgumentException("Customer does not exist");
        }

        return repository.save(customer);
    }

    public Optional<Customer> getCustomerById(String id) {
        return repository.findById(id);
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public Customer updateCustomer(Customer customer) {
        return repository.save(customer);
    }

    public void deleteCustomer(String id) {
        repository.deleteById(id);
    }

    //Retrieval API that return paginated lists with filters as => Customers: Name (full text search), date created range
    public Page<Customer> searchCustomers(String name, String startDate, String endDate, Pageable pageable) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        LocalDateTime start = (startDate != null && !startDate.isEmpty())
                ? LocalDateTime.parse(startDate, formatter)
                : LocalDateTime.MIN;

        LocalDateTime end = (endDate != null && !endDate.isEmpty())
                ? LocalDateTime.parse(endDate, formatter)
                : LocalDateTime.now();

        return repository.findByNameContainingAndCreatedAtBetween(
                name == null ? "" : name, start, end, pageable);
    }



}
