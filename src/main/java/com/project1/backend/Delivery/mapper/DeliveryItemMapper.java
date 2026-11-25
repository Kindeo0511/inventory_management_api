package com.project1.backend.Delivery.mapper;

import com.project1.backend.Delivery.DeliveryItem;
import com.project1.backend.Delivery.dto.DeliveryItemRequest;
import org.springframework.stereotype.Component;

@Component
public class DeliveryItemMapper {

    public DeliveryItemRequest toDto(DeliveryItem item){
        return DeliveryItemRequest.builder()
                .qty(item.getQty())
                .price(item.getPrice())
                .productVariantId(item.getProductVariant().getId())
                .build();
    }
    public DeliveryItem toEntity(DeliveryItemRequest dto){

        return DeliveryItem.builder()
                .qty(dto.getQty())
                .price(dto.getPrice())
                .build();
    }
}
