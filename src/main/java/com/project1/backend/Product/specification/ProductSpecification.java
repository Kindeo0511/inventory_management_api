package com.project1.backend.Product.specification;
import com.project1.backend.Product.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<Product> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return null;
            }
            return cb.equal(cb.lower(root.get("name")), name.trim().toLowerCase());
        };
    }

    public static Specification<Product> hasCategory(String categoryName){

        return (root, query, cb) -> {
            if(categoryName == null || categoryName.isBlank()){
                return null;
            }
            return cb.equal(cb.lower(root.join("category").get("name")),categoryName.toLowerCase());
        };


    }
}
