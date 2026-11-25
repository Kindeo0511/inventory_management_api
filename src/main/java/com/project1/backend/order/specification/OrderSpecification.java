package com.project1.backend.order.specification;

import com.project1.backend.order.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {
    public static Specification<Order> proccessedBy(String name){
        return (root, query, criteriaBuilder) -> {
            if(name == null || name.isBlank()){
                return null;
            }

            List<Predicate> predicates = new ArrayList<>();
            for(String fullName : name.trim().toLowerCase().split("\\s+")){
                String like = "%" + fullName + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.join("processedBy").get("firstName")),like),
                        criteriaBuilder.like(criteriaBuilder.lower(root.join("processedBy").get("middleName")),like),
                        criteriaBuilder.like(criteriaBuilder.lower(root.join("processedBy").get("lastName")),like)
                ));
            };
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
