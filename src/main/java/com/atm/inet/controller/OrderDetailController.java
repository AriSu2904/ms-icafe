package com.atm.inet.controller;


import com.atm.inet.entity.OrderDetail;
import com.atm.inet.model.common.CommonResponse;
import com.atm.inet.model.request.OrderDetailRequest;
import com.atm.inet.model.response.OrderDetailResponse;
import com.atm.inet.model.response.PaymentResponse;
import com.atm.inet.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/orders")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<CommonResponse<PaymentResponse>> order(@RequestBody OrderDetailRequest request){
        PaymentResponse paymentResponse = orderDetailService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        CommonResponse.<PaymentResponse>builder()
                                .statusCode(HttpStatus.CREATED.value())
                                .message("Successfully Create Order!")
                                .data(paymentResponse)
                                .build()
                );
    }
    
    
    @GetMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<OrderDetail>> getById(@PathVariable String id){
        OrderDetail orderDetail = orderDetailService.findById(id);
        return ResponseEntity.ok(
                CommonResponse.<OrderDetail>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get data")
                        .data(orderDetail)
                        .build()
        );
    }
    @GetMapping(path = "/my-list")
    public ResponseEntity<CommonResponse<List<OrderDetailResponse>>> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<OrderDetailResponse> orderDetails = orderDetailService.getAll(authentication);

        return ResponseEntity.ok(
                CommonResponse.<List<OrderDetailResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get data")
                        .data(orderDetails)
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CommonResponse<List<OrderDetailResponse>>> getAllByAdmin() {
        List<OrderDetailResponse> orderDetails = orderDetailService.getAll();

        return ResponseEntity.ok(
                CommonResponse.<List<OrderDetailResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get data")
                        .data(orderDetails)
                        .build()
        );
    }


}
