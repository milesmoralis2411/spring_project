package com.example.ecommerce.controller;

import com.example.ecommerce.dto.PaymentWebhookRequest;
import com.example.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
public class PaymentWebhookController {
    private final PaymentService paymentService;

    @PostMapping("/payment")
    public void handlePaymentWebhook(@RequestBody PaymentWebhookRequest request) {
        if (request.getPayload() != null && request.getPayload().getPayment() != null) {

            String paymentId = request.getPayload().getPayment().getId();
            String orderId = request.getPayload().getPayment().getOrder_id();
            String status = request.getPayload().getPayment().getStatus();

            paymentService.processWebhook(paymentId, orderId, status);
        }
    }
}
