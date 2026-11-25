package com.project1.backend.Product.ProductHelper;

import com.project1.backend.Product.Product;
import com.project1.backend.Product.ProductRepository;
import com.project1.backend.Product.dto.ProductVariantRequest;
import com.project1.backend.category.Category;
import com.project1.backend.category.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Helper {

    private final ProductRepository productRepository;


    public String generateSku(Product product, ProductVariantRequest request) {
        return String.format("%s-%s-%s",
                product.getCode(),
                prefix(request.getColor(), 3),
                request.getSize().trim().toUpperCase());
    }

    public String generateProductCode(Category category) {
        String prefix = prefix(category.getSubName(), 3);
        long next = productRepository.countByCategory(category) + 1;
        return String.format("%s-%04d", prefix, next);
    }

    private String prefix(String value, int length) {
        String cleaned = value.replaceAll("[^A-Za-z]", "").toUpperCase();
        return cleaned.substring(0, Math.min(length, cleaned.length()));
    }
}
