package com.project1.backend.Inventory.specification;

import com.project1.backend.Inventory.Inventory;
import org.springframework.data.jpa.domain.Specification;

public class InventorySpecification {
    public static Specification<Inventory> hasProductName(String productName){
        return (root, query, criteriaBuilder) -> {
            if(productName == null || productName.isBlank()){
                return null;
            }

            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("name")),productName.trim().toLowerCase());
        };
    }

    public static Specification<Inventory> hasCategoryName(String categoryName){
        return (root, query, criteriaBuilder) -> {
            if(categoryName == null || categoryName.isBlank()){
                return null;
            }

            return criteriaBuilder.equal(root.join("product").join("category"),categoryName);
        };
    }
}
