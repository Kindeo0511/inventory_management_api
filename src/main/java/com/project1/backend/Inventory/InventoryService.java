package com.project1.backend.Inventory;

import com.project1.backend.Inventory.dto.InventoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryService {
    Page<InventoryResponse> getAllInventory(String productName, String categoryName, Pageable pageable);
}
