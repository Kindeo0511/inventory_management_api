package com.project1.backend.Delivery.mapper;

import com.project1.backend.Delivery.Delivery;
import com.project1.backend.Delivery.dto.DeliveryRequest;
import com.project1.backend.Delivery.dto.DeliveryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryMapper {

    private final DeliveryItemMapper deliveryItemMapper;


    public DeliveryResponse toDto(Delivery delivery){
        return DeliveryResponse.builder()
                .id(delivery.getId())
                .dateReceived(delivery.getDateReceived())
                .supplierId(delivery.getSupplier().getId())
                .supplierName(delivery.getSupplier().getName())
                .items(delivery.getDeliveryItem()
                        .stream()
                        .map(deliveryItemMapper::toDto)
                        .toList())
                .receivedBy(delivery.getReceivedBy())

                .build();
    }


}
