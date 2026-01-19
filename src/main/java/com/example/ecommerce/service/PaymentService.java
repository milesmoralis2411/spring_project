package com.example.ecommerce.service;

import com.example.ecommerce.dto.PaymentRequest;
import com.example.ecommerce.dto.PaymentWebhookRequest;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Payment;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public Payment createPayment(PaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!"CREATED".equals(order.getStatus())) {
            throw new RuntimeException("Order already paid or cancelled");
        }

        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setStatus("PENDING");
        payment.setPaymentId("pay_mock_" + UUID.randomUUID().toString().substring(0, 8));

        Payment savedPayment = paymentRepository.save(payment);

        simulatePaymentSuccess(savedPayment.getPaymentId(), request.getOrderId());

        return savedPayment;
    }

    @Async
    public void simulatePaymentSuccess(String paymentId, String orderId) {
        try {
            Thread.sleep(3000);
            System.out.println("Simulating Webhook for Payment: " + paymentId);

            processWebhook(paymentId, orderId, "captured");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void processWebhook(String paymentId, String orderId, String status) {
        if ("captured".equals(status)) {
            Payment payment = paymentRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new RuntimeException("Payment not found for order"));

            payment.setStatus("SUCCESS");
            paymentRepository.save(payment);

            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            order.setStatus("PAID");
            orderRepository.save(order);

            System.out.println("Order " + orderId + " updated to PAID");
        }
    }
}
