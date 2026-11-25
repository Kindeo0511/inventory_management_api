package com.project1.backend.supplier.mapper;

import com.project1.backend.supplier.Supplier;
import com.project1.backend.supplier.dto.SupplierRequest;
import com.project1.backend.supplier.dto.SupplierResponse;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {
    public SupplierResponse toDto(Supplier supplier){
        return SupplierResponse.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .address(supplier.getAddress())
                .contactNumber(supplier.getContactNumber())
                .createdAt(supplier.getCreatedAt())
                .updatedAt(supplier.getUpdatedAt())
                .createdBy(supplier.getCreatedBy())
                .updatedBy(supplier.getUpdatedBy())
                .build();

    }
    public Supplier toEntity(SupplierRequest request){
        return Supplier.builder()
                .name(request.getName())
                .address(request.getAddress())
                .contactNumber(request.getContactNumber())
                .build();
    }

}
