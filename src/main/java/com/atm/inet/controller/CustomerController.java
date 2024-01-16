package com.atm.inet.controller;

import com.atm.inet.model.common.CommonResponse;
import com.atm.inet.model.common.CustomerSearch;
import com.atm.inet.model.common.PagingResponse;
import com.atm.inet.model.request.CustomerRequest;
import com.atm.inet.model.response.CustomerResponse;
import com.atm.inet.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(name = "size", defaultValue = "5", required = false) Integer size,
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName){
        CustomerSearch dataCustomer = CustomerSearch.builder()
                .customerFirstName(firstName)
                .customerLastName(lastName)
                .build();
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<CustomerResponse> pageableResponse = customerService.getCustomerPerPage(pageable, dataCustomer);

        PagingResponse pagingResponse = PagingResponse.builder()
                .count(pageableResponse.getTotalElements())
                .totalPages(pageableResponse.getTotalPages())
                .page(pageNumber)
                .size(size)
                .build();

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully Get Data Per Page!")
                        .data(pageableResponse.getContent())
                        .pagingResponse(pagingResponse)
                        .build()
        );
    }


    @GetMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<CustomerResponse>> findById(@PathVariable String id){
        CustomerResponse customer = customerService.findById(id);
        return ResponseEntity.ok(
                CommonResponse.<CustomerResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully Get Customer")
                        .data(customer)
                        .build()
        );
    }

    @PutMapping()
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CommonResponse<CustomerResponse>> updateCustomer(@RequestBody CustomerRequest request){
        CustomerResponse customerResponse = customerService.updateCustomer(request);
        return ResponseEntity.ok(
                CommonResponse.<CustomerResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully update data!")
                        .data(customerResponse)
                        .build()
        );
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<?>> deleteCustomer(@PathVariable String id){
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(
                CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully delete Data!")
                        .build()
        );
    }

}
