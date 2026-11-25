package com.project1.backend.Inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long>,
        JpaSpecificationExecutor<Inventory>

{

    Optional <Inventory> findByProductVariantId(Long id);
}
