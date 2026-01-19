package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class PaymentWebhookRequest {
    private String event;
    private Payload payload;

    @Data
    public static class Payload {
        private PaymentDetails payment;
    }

    @Data
    public static class PaymentDetails {
        private String id;
        private String order_id;
        private String status;
    }
}
