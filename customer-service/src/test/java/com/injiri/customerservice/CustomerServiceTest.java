package com.injiri.customerservice;

import com.injiri.customerservice.model.Customer;
import com.injiri.customerservice.repository.CustomerRepository;
import com.injiri.customerservice.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerService customerService;

    private Customer sampleCustomer;

    @BeforeEach
    void setUp() {
        sampleCustomer = new Customer();
        sampleCustomer.setId("3242432123");
        sampleCustomer.setFirstName("Simon");
        sampleCustomer.setLastName("Otwero");
        sampleCustomer.setOtherName("Injiri");
        sampleCustomer.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createCustomer_shouldThrowIfCustomerNotExists() {
        when(repository.existsById(sampleCustomer.getId())).thenReturn(false);

        assertThatThrownBy(() -> customerService.createCustomer(sampleCustomer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Customer does not exist");
    }

    @Test
    void getCustomerById_shouldReturnCustomer() {
        when(repository.findById("3242432123")).thenReturn(Optional.of(sampleCustomer));

        Optional<Customer> found = customerService.getCustomerById("3242432123");

        assertThat(found).isPresent().contains(sampleCustomer);
    }

    @Test
    void getAllCustomers_shouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(sampleCustomer));

        List<Customer> customers = customerService.getAllCustomers();

        assertThat(customers).hasSize(1).contains(sampleCustomer);
    }

    @Test
    void updateCustomer_shouldSaveCustomer() {
        when(repository.save(sampleCustomer)).thenReturn(sampleCustomer);

        Customer updated = customerService.updateCustomer(sampleCustomer);

        assertThat(updated).isEqualTo(sampleCustomer);
    }

    @Test
    void deleteCustomer_shouldDeleteById() {
        customerService.deleteCustomer("3242432123");

        verify(repository).deleteById("3242432123");
    }

    @Test
    void searchCustomers_shouldReturnFilteredResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> page = new PageImpl<>(List.of(sampleCustomer));

        when(repository.findByNameContainingAndCreatedAtBetween(
                eq("Simon"), any(), any(), eq(pageable)))
                .thenReturn(page);

        Page<Customer> result = customerService.searchCustomers("Simon", null, null, pageable);

        assertThat(result).isNotEmpty();
    }
}
