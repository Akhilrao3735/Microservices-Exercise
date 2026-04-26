package com.microservices.cartservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartEventDTO {
    private Integer cartId;
    private Integer productId;
    private Integer quantity;
}