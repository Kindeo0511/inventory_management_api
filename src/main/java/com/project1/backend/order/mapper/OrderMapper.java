package com.project1.backend.order.mapper;

import com.project1.backend.order.Order;
import com.project1.backend.order.dto.OrderResponse;
import com.project1.backend.order.helper.OrderHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderItemMapper orderItemMapper;
    public OrderResponse toDto(Order order){
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .processedBy(order.getProcessedBy())
                .createdAt(order.getCreatedAt())
                .orders(order.getOrderItems()
                        .stream()
                        .map(orderItemMapper::toItemDto)
                        .toList())
                .build();
    }
}
