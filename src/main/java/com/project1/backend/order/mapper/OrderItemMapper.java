package com.project1.backend.order.mapper;

import com.project1.backend.order.OrderItem;
import com.project1.backend.order.dto.OrderItemResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public OrderItemResponse toItemDto(OrderItem orderItem){
        return OrderItemResponse.builder()
                .productName(orderItem.getProductVariant().getProduct().getName())
                .qty(orderItem.getQty())
                .amount(orderItem.getAmount())
                .totalAmount(orderItem.getTotalAmount())
                .build();

    }
}
