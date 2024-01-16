package com.atm.inet.service;

import com.atm.inet.entity.Customer;
import com.atm.inet.model.common.CustomerSearch;
import com.atm.inet.model.request.CustomerRequest;
import com.atm.inet.model.response.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface CustomerService {

    Customer addCustomer(Customer customer);

    CustomerResponse updateCustomer(CustomerRequest customer);

    CustomerResponse authenticationCustomer(Authentication authentication);

    String findEmailById(String id);

    CustomerResponse findById(String id);
    Customer findByEmail(String email);

    Page<CustomerResponse> getCustomerPerPage(Pageable pageable, CustomerSearch customer);

    String deleteCustomer(String id);

}
