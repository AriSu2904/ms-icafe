package com.atm.inet.repository;

import com.atm.inet.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findFirstByUserCredential_Email(String email);


    Optional<Customer> findByPhoneNumber(String phoneNumber);
}
