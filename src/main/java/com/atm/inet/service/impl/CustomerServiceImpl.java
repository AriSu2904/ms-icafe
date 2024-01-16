package com.atm.inet.service.impl;

import com.atm.inet.entity.Customer;
import com.atm.inet.entity.UserDetailsImpl;
import com.atm.inet.model.common.CustomerSearch;
import com.atm.inet.model.request.CustomerRequest;
import com.atm.inet.model.response.CustomerResponse;
import com.atm.inet.repository.CustomerRepository;
import com.atm.inet.service.CustomerService;
import com.atm.inet.utils.specification.CustomerSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse authenticationCustomer(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Customer customer = customerRepository.findFirstByUserCredential_Email(userDetails.getEmail()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        return responseGenerator(customer);
    }

    @Override
    public Customer addCustomer(Customer customer) {
        try {
            return customerRepository.save(customer);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email Already Exist!");
        }
    }

    @Override
    public CustomerResponse updateCustomer(CustomerRequest customer) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomerResponse customerResponse = authenticationCustomer(authentication);
        if (customerResponse.getIsDeleted())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account is already deleted!");

        Optional<Customer> optionalCustomer = customerRepository.findByPhoneNumber(customer.getPhoneNumber());
        if (optionalCustomer.isPresent()) {
            Customer existCustomer = optionalCustomer.get();
            if (existCustomer.getPhoneNumber().equals(customer.getPhoneNumber()) && !existCustomer.getId().equals(customer.getId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already in use");
            }
        }


        if (customerResponse.getId().equals(customer.getId())) {

            Customer foundCustomer = getCustomerById(customer.getId());
            foundCustomer.setFirstName(customer.getFirstName());
            foundCustomer.setLastName(customer.getLastName());
            foundCustomer.setPhoneNumber(customer.getPhoneNumber());
            foundCustomer.setIsMember(customer.getIsMember());
            foundCustomer.setIsDeleted(false);
            customerRepository.save(foundCustomer);

            return responseGenerator(foundCustomer);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You're not allowed to access this resource!");
    }

    @Override
    public CustomerResponse findById(String id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        return responseGenerator(customer);
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findFirstByUserCredential_Email(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    }

    @Override
    public String findEmailById(String id) {
        Customer customer = customerRepository.findFirstByUserCredential_Email(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        return customer.getUserCredential().getEmail();
    }

    @Override
    public Page<CustomerResponse> getCustomerPerPage(Pageable pageable, CustomerSearch customerDto) {
        Specification<Customer> customerSpecification = CustomerSpecification.getSpecification(customerDto);
        Page<Customer> customers = customerRepository.findAll(customerSpecification, pageable);
        return customers.map(this::responseGenerator);
    }

    @Override
    public String deleteCustomer(String id) {
        Customer customer = getCustomerById(id);
        customer.setIsDeleted(true);
        customer.getUserCredential().setIsActive(false);
        customerRepository.save(customer);
        return id;
    }

    private Customer getCustomerById(String id) {
        return customerRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Service: User not found!"));
    }

    private CustomerResponse responseGenerator(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .isMember(customer.getIsMember())
                .isDeleted(customer.getIsDeleted())
                .role(customer.getUserCredential().getRole().getRole().name())
                .build();
    }

}
