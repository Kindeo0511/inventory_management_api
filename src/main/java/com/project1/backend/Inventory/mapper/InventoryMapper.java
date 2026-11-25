package com.project1.backend.Inventory.mapper;

import com.project1.backend.Inventory.Inventory;
import com.project1.backend.Inventory.dto.InventoryResponse;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {
    public InventoryResponse toDto(Inventory inventory){
        return InventoryResponse.builder()
                .sku(inventory.getProductVariant().getSku())
                .productName(inventory.getProductVariant().getProduct().getName())
                .subName(inventory.getProductVariant().getProduct().getCategory().getName())
                .brand(inventory.getProductVariant().getProduct().getBrand())
                .sellingPrice(inventory.getProductVariant().getProduct().getSellingPrice())
                .stock(inventory.getStock())
                .status(inventory.getStatus())
                .build();
    }
}
