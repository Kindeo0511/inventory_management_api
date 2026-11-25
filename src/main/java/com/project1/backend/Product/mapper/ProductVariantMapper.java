package com.project1.backend.Product.mapper;

import com.project1.backend.Product.ProductHelper.Helper;
import com.project1.backend.Product.dto.ProductVariantRequest;
import com.project1.backend.Product.dto.ProductVariantResponse;
import com.project1.backend.Product.entity.ProductVariant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class ProductVariantMapper {

    public ProductVariantResponse toDto(ProductVariant productVariant){
        return ProductVariantResponse.builder()
                .id(productVariant.getId())
                .sku(productVariant.getSku())
                .productName(productVariant.getProduct().getName())
                .size(productVariant.getSize())
                .color(productVariant.getColor())
                .build();
    }

    public ProductVariant toEntity(ProductVariantRequest request){
        return ProductVariant.builder()
                .size(request.getSize())
                .color(request.getColor())
                .build();
    }
}
