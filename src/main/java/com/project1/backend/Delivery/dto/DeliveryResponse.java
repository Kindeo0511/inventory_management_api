package com.project1.backend.Delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponse {
    private Long id;
    private Long supplierId;
    private String supplierName;
    private LocalDateTime dateReceived;

    private List<DeliveryItemRequest> items;

    private String receivedBy;
}
