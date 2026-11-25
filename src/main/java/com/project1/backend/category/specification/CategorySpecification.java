package com.project1.backend.category.specification;

import com.project1.backend.category.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {
    public static Specification<Category> hasName(String name){

        return(root, query, criteriaBuilder) -> {
            if(name == null || name.isBlank()){
                return null;
            }
            return criteriaBuilder.equal(root.get("name"),name);
        };


    }
}
