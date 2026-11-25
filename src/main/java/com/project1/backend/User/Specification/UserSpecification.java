package com.project1.backend.User.Specification;

import com.project1.backend.User.Users;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class UserSpecification {


    public static Specification<Users> hasName(String name){
        return (root, query, criteriaBuilder) -> {
            if(name == null || name.isBlank()){
                return null;
            }
            List<Predicate> predicates = new ArrayList<>();
            for(String fullName : name.trim().toLowerCase().split("\\s+")){
                String like = "%" + fullName + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),like),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("middleName")),like),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),like)
                ));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    public static Specification<Users> hasRole(String role){

        return (root, query, criteriaBuilder) -> {
            if(role == null || role.isBlank()){
                return null;
            }
            return criteriaBuilder.equal(root.get("role"),role.trim().toUpperCase());
        };
    }
}
