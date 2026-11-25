package com.project1.backend.Product.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
public class ProductResponse {
    private long id;
    private String code;
    private String name;
    private String brand;
    private BigDecimal sellingPrice;
    private Long categoryId;
    private String categoryName;
    private String productImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;
}
