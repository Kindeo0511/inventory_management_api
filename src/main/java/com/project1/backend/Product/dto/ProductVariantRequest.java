package com.project1.backend.Product.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVariantRequest {
    private Long productId;
    private String sku;
    private String size;
    private String color;
}
