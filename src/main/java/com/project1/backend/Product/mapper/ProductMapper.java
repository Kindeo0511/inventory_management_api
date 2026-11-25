package com.project1.backend.Product.mapper;

import com.project1.backend.Product.Product;
import com.project1.backend.Product.dto.ProductRequest;
import com.project1.backend.Product.dto.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toDto(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .brand(product.getBrand())
                .categoryId(product.getId())
                .categoryName(product.getCategory().getSubName())
                .sellingPrice(product.getSellingPrice())
                .productImage(product.getProductImage())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .createdBy(product.getCreatedBy())
                .updatedBy(product.getUpdatedBy())
                .build();
    }

    public Product toEntity(ProductRequest request){
        return Product.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .sellingPrice(request.getSellingPrice())
                .productImage(request.getImageFile())
                .build();

    }
}
