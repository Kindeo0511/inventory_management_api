package com.project1.backend.supplier;

import com.project1.backend.supplier.dto.SupplierRequest;
import com.project1.backend.supplier.dto.SupplierResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupplierService {
    Page<SupplierResponse> getAllSuppliers(String supplierName, Pageable pageable);
    SupplierResponse createSupplier(SupplierRequest request);
    SupplierResponse updateSupplier(SupplierRequest request, Long id);
    SupplierResponse getSupplier(Long id);
    void deleteSupplier(Long id);
}
