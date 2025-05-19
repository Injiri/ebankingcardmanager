package com.injiri.customerservice.repository;

import com.injiri.customerservice.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query("SELECT c FROM Customer c WHERE CONCAT(c.firstName, ' ', c.lastName) LIKE %:name% AND c.createdAt BETWEEN :start AND :end")
    Page<Customer> findByNameContainingAndCreatedAtBetween(
            @Param("name") String name,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );


}
