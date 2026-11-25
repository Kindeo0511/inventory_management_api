package com.project1.backend.order.helper;

import com.project1.backend.order.Order;
import com.project1.backend.order.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderHelper {
    private final OrderRepository orderRepository;
    public String generateOrderNumber(){
        String prefix = "ORD";
        long next = orderRepository.count()+1;
        return String.format("%s-%04d",prefix,next);
    }
}
