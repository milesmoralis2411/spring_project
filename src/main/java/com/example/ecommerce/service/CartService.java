package com.example.ecommerce.service;

import com.example.ecommerce.dto.AddToCartRequest;
import com.example.ecommerce.dto.CartItemResponse;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartItem addToCart(AddToCartRequest request) {

        productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return cartRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId())
                .map(existingItem -> {
                    existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
                    return cartRepository.save(existingItem);
                })
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setUserId(request.getUserId());
                    newItem.setProductId(request.getProductId());
                    newItem.setQuantity(request.getQuantity());
                    return cartRepository.save(newItem);
                });
    }

    public List<CartItemResponse> getCart(String userId) {
        return cartRepository.findByUserId(userId).stream()
                .map(item -> {
                    Product product = productRepository.findById(item.getProductId()).orElse(null);
                    return CartItemResponse.builder()
                            .id(item.getId())
                            .productId(item.getProductId())
                            .quantity(item.getQuantity())
                            .product(product)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public void clearCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }
}
