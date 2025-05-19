package com.injiri.customerservice;

import com.injiri.customerservice.model.Customer;
import com.injiri.customerservice.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CustomerServiceApplicationTests {

    @Autowired
    private CustomerRepository repository;

    @Test
    void contextLoads() {
    }

    @Test
    void testCreateAndRetrieveCustomer() {
        Customer customer = new Customer();
        customer.setId("202232");
        customer.setFirstName("Simon");
        customer.setLastName("Injiri");

        repository.save(customer);

        Customer fetched = repository.findById("202232").orElse(null);
        assertThat(fetched).isNotNull();
        assertThat(fetched.getFirstName()).isEqualTo("Simon");
        assertThat(fetched.getLastName()).isEqualTo("Injiri");
    }
}
