package com.atm.inet.controller;

import com.atm.inet.service.OrderDetailService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/midtrans")
public class WebhookController {

    private final OrderDetailService orderDetailService;

    @PostMapping()
    public ResponseEntity<String> midtransWebhook(@RequestBody String requestBody) {
        String transactionId = extractTransactionIdFromRequestBody(requestBody);
        log.warn("INFO FROM CONTROLLER: {}", requestBody);



            String updatedTransactionStatus = orderDetailService.updateStatus(transactionId);
            return ResponseEntity.ok(updatedTransactionStatus);
    }

    private String extractTransactionIdFromRequestBody(String requestBody) {
        try {
            JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
            if (jsonObject.has("order_id")) {
                return jsonObject.get("order_id").getAsString();
            } else {
                throw new IllegalArgumentException("Field 'order_id' not found in the request body");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to extract transaction ID from the request body", e);
        }
    }

}
