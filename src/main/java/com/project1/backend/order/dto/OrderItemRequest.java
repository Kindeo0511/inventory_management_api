package com.project1.backend.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemRequest {

    private int qty;
    private BigDecimal amount;
    private BigDecimal totalAmount;
    private Long productVariantId;

}
