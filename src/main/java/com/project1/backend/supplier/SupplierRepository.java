package com.project1.backend.supplier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier,Long>,
        JpaSpecificationExecutor<Supplier> {

    boolean existsByName(String name);
    boolean existsByContactNumber(String number);
    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByContactNumberAndIdNot(String number, Long id);

}
