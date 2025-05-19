package com.injiri.customerservice;

import com.injiri.customerservice.controller.CustomerController;
import com.injiri.customerservice.model.Customer;
import com.injiri.customerservice.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    private CustomerService service;
    private CustomerService customerService;
    private CustomerController controller;

    @BeforeEach
    void setUp() {
        service = mock(CustomerService.class);
        customerService = mock(CustomerService.class);
        controller = new CustomerController(service, customerService);
    }

    @Test
    void testCreateCustomer() {
        Customer customer = new Customer();
        when(service.createCustomer(any(Customer.class))).thenReturn(customer);

        ResponseEntity<Customer> response = controller.createCustomer(customer);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(service).createCustomer(customer);
    }

    @Test
    void testGetCustomerFound() {
        Customer customer = new Customer();
        when(service.getCustomerById("3242432123")).thenReturn(Optional.of(customer));

        ResponseEntity<Customer> response = controller.getCustomer("3242432123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customer, response.getBody());
    }

    @Test
    void testGetCustomerNotFound() {
        when(service.getCustomerById("3242432123")).thenReturn(Optional.empty());

        ResponseEntity<Customer> response = controller.getCustomer("3242432123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllCustomers() {
        List<Customer> list = Arrays.asList(new Customer(), new Customer());
        when(service.getAllCustomers()).thenReturn(list);

        ResponseEntity<List<Customer>> response = controller.getAllCustomers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testUpdateCustomerFound() {
        Customer customer = new Customer();
        customer.setId("3242432123");

        when(service.getCustomerById("3242432123")).thenReturn(Optional.of(customer));
        when(service.updateCustomer(any(Customer.class))).thenReturn(customer);

        ResponseEntity<Customer> response = controller.updateCustomer("3242432123", customer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).updateCustomer(customer);
    }

    @Test
    void testUpdateCustomerNotFound() {
        Customer customer = new Customer();
        when(service.getCustomerById("3242432123")).thenReturn(Optional.empty());

        ResponseEntity<Customer> response = controller.updateCustomer("3242432123", customer);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteCustomerFound() {
        when(service.getCustomerById("3242432123")).thenReturn(Optional.of(new Customer()));

        ResponseEntity<Void> response = controller.deleteCustomer("3242432123");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).deleteCustomer("3242432123");
    }

    @Test
    void testDeleteCustomerNotFound() {
        when(service.getCustomerById("3242432123")).thenReturn(Optional.empty());

        ResponseEntity<Void> response = controller.deleteCustomer("3242432123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSearchCustomers() {
        Page<Customer> page = new PageImpl<>(List.of(new Customer()));
        when(customerService.searchCustomers(any(), any(), any(), any(Pageable.class)))
                .thenReturn(page);

        Page<Customer> result = controller.searchCustomers("Simon", null, null, Pageable.ofSize(10));

        assertEquals(1, result.getContent().size());
    }
}
