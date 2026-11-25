package com.project1.backend.Product;

import com.project1.backend.Product.dto.ProductVariantRequest;
import com.project1.backend.Product.dto.ProductVariantResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductVariantService {
    Page<ProductVariantResponse> getAllProductVariants(Pageable pageable);
    ProductVariantResponse getProductVariantById(Long id);
    ProductVariantResponse createProductVariant(ProductVariantRequest request);
    ProductVariantResponse updateProductVariant(ProductVariantRequest request, Long id);
    void deleteProductVariantById(Long id);

}
