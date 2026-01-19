package com.example.ecommerce.dto;

import com.example.ecommerce.model.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponse {
    private String id;
    private String productId;
    private Integer quantity;
    private Product product;
}
