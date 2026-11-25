package com.project1.backend.Delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequest {
    private Long supplierId;
    private List<DeliveryItemRequest> items;
}
