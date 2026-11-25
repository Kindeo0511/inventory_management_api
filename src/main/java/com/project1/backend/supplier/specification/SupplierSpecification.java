package com.project1.backend.supplier.specification;

import com.project1.backend.supplier.Supplier;
import org.springframework.data.jpa.domain.Specification;

public class SupplierSpecification {
    public static Specification<Supplier> hasSupplierName(String supplierName){
        return (root, query, criteriaBuilder) -> {
            if(supplierName == null || supplierName.isBlank()){
                return null;
            }
            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("name")),supplierName.trim().toLowerCase());
        };
    }
}
