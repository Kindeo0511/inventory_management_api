package com.project1.backend.Inventory.dto;

import com.project1.backend.Inventory.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponse {

    private String sku;
    private String productName;
    private String subName;
    private String brand;
    private BigDecimal sellingPrice;
    private int stock;
    private Status status;
}
