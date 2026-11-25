package com.project1.backend.Product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message = "Product name is required.")
    private String name;
    @NotBlank(message = "Brand name is required.")
    private String brand;
    @NotNull(message = "Selling price is required.")
    private BigDecimal sellingPrice;
    @NotNull(message = "Product image is required.")
    private String imageFile;
    @NotNull(message = "Category is required.")
    private Long categoryId;
    private Long version;
}
