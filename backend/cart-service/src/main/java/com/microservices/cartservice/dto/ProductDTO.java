package com.microservices.cartservice.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private Integer id;
    private String name;
    private Double price;
    private Integer stock;
}