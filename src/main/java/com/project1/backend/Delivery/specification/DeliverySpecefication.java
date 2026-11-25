package com.project1.backend.Delivery.specification;

import com.project1.backend.Delivery.Delivery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;


public class DeliverySpecefication {

    public static Specification<Delivery> hasDate(LocalDate from, LocalDate to){
        return (root, query, criteriaBuilder) -> {
            if(from == null && to == null){
                return null;
            }


            if(to == null){
                return criteriaBuilder.greaterThanOrEqualTo(root.get("dateReceived"), from.atStartOfDay());
            }
            if(from == null){
                return criteriaBuilder.lessThan(root.get("dateReceived"),to.plusDays(1).atStartOfDay());
            }

            return criteriaBuilder.and(
                    criteriaBuilder.greaterThanOrEqualTo(root.get("dateReceived"),from.atStartOfDay()),
                    criteriaBuilder.lessThan(root.get("dateReceived"),to.plusDays(1).atStartOfDay()));


        };
    }
}
