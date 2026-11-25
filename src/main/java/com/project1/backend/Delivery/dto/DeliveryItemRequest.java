package com.project1.backend.Delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryItemRequest {
    private Long productVariantId;
    private int qty;
    private BigDecimal price;

}
