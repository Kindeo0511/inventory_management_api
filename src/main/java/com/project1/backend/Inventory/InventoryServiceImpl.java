package com.project1.backend.Inventory;

import com.project1.backend.Inventory.dto.InventoryResponse;
import com.project1.backend.Inventory.mapper.InventoryMapper;
import com.project1.backend.Inventory.specification.InventorySpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public Page<InventoryResponse> getAllInventory(String productName, String categoryName, Pageable pageable) {

        log.info("Fetching all inventories");
        Specification<Inventory> specification = Specification.allOf(
                InventorySpecification.hasProductName(productName),
                InventorySpecification.hasCategoryName(categoryName)
        );
        Page<Inventory> inventories = inventoryRepository.findAll(specification,pageable);
        log.info("Successfully fetched {} inventories",inventories.getSize());
        return inventories.map(inventoryMapper::toDto);

    }
}
