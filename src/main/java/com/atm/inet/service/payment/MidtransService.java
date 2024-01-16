package com.atm.inet.service.payment;

import com.atm.inet.model.response.OrderDetailResponse;
import com.atm.inet.model.response.PaymentResponse;
import com.midtrans.Midtrans;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MidtransService {

    @Value("${icafe.midtrans.server-key}")
    private String serverKey;


    @Value("${icafe.midtrans.client-key}")
    private String clientKey;

    private final RestTemplate restTemplate;

    @Transactional(rollbackOn = Exception.class)
    public PaymentResponse requestTransaction(OrderDetailResponse respose) {

        Midtrans.serverKey = serverKey;

        String apiUrl = "https://app.sandbox.midtrans.com/snap/v1/transactions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((serverKey + ":").getBytes()));

        Map<String, Object> transRequest = requestTransactionObj(respose);

        HttpEntity<Map<String, Object>> requestOrder = new HttpEntity<>(transRequest, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestOrder, String.class);

        if(responseEntity.getStatusCode().is2xxSuccessful()){
            String responseBody = responseEntity.getBody();

           JSONObject body = new JSONObject(responseBody);


           log.info("JSON RESPONSE DARI MIDTRANS SERVICE : {}", body);

            return PaymentResponse.builder()
                    .token(body.getString("token"))
                    .redirectUrl(body.getString("redirect_url"))
                    .build();

        } else {
            throw new RuntimeException("Failed to create transaction with Midtrans: " + responseEntity.getStatusCode());
        }

    }
    public String getTransactionById(String id){

        String apiUrl = String.format("https://api.sandbox.midtrans.com/v2/%s/status", id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((serverKey + ":").getBytes()));

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET,requestEntity,String.class);

        return responseEntity.getBody();
    }

    private Map<String, Object> requestTransactionObj(OrderDetailResponse respose) {
        Map<String, Object> transDetail = new HashMap<>();
        transDetail.put("order_id", respose.getOrderId());
        transDetail.put("gross_amount", respose.getPrice());

        Map<String, Object> items = new HashMap<>();
        items.put("id", respose.getComputerCode());
        items.put("name", respose.getComputerName());
        items.put("quantity", 1);
        items.put("category", respose.getType());
        items.put("price", respose.getPrice());

        Map<String, Object> customer = new HashMap<>();
        customer.put("first_name", respose.getCustomerFirstName());
        customer.put("last_name", respose.getCustomerLastName());
        customer.put("email", respose.getCustomerEmail());
        customer.put("phone", respose.getCustomerPhoneNumber());

        Map<String, Object> expiration = new HashMap<>();
        expiration.put("duration", 2);
        expiration.put("unit", "minute");

        Map<String, Object> callBack = new HashMap<>();
        callBack.put("finish", "https://e3dd-103-78-25-190.ngrok-free.app/home/");

        Map<String, Object> transRequest = new HashMap<>();
        transRequest.put("transaction_details", transDetail);
        transRequest.put("item_details", items);
        transRequest.put("customer_details", customer);
        transRequest.put("expiry", expiration);
        transRequest.put("callbacks", callBack);
        return transRequest;
    }

}
