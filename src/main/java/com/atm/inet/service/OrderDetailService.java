package com.atm.inet.service;

import com.atm.inet.entity.OrderDetail;
import com.atm.inet.model.request.OrderDetailRequest;
import com.atm.inet.model.response.OrderDetailResponse;
import com.atm.inet.model.response.PaymentResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface OrderDetailService {

      PaymentResponse create(OrderDetailRequest request);

      String updateStatus(String id);

      OrderDetail findById(String id);

      List<OrderDetailResponse> getAll(Authentication authentication);

      List<OrderDetailResponse> getAll();

}
