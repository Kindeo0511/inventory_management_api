package com.project1.backend.Product.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantResponse {
    private Long id;
    private String sku;
    private String productName;
    private String size;
    private String color;
}
