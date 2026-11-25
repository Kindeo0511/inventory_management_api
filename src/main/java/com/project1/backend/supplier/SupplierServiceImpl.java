package com.project1.backend.supplier;

import com.project1.backend.User.UserRepository;
import com.project1.backend.User.Users;
import com.project1.backend.exception.DuplicateResourceException;
import com.project1.backend.exception.ResourceNotFoundException;
import com.project1.backend.supplier.dto.SupplierRequest;
import com.project1.backend.supplier.dto.SupplierResponse;
import com.project1.backend.supplier.mapper.SupplierMapper;
import com.project1.backend.supplier.specification.SupplierSpecification;
import com.project1.backend.util.SecurityUtils;
import jakarta.persistence.OptimisticLockException;
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
public class SupplierServiceImpl implements SupplierService{
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final SupplierMapper supplierMapper;

    @Override
    public Page<SupplierResponse> getAllSuppliers(String supplierName, Pageable pageable) {

        log.info("Fetching all suppliers");
        Specification<Supplier> specification = Specification.allOf(
                SupplierSpecification.hasSupplierName(supplierName)
        );

        Page<Supplier> suppliers = supplierRepository.findAll(specification, pageable);

        log.info("Successfully fetched {} supplier", suppliers.getSize());
        return suppliers.map(supplierMapper::toDto);

    }

    @Override
    public SupplierResponse createSupplier(SupplierRequest request) {

        log.info("Creating supplier");
        if(supplierRepository.existsByName(request.getName())){
            throw new DuplicateResourceException("Supplier name already exist.");
        }
        if(supplierRepository.existsByContactNumber(request.getContactNumber())){
            throw new DuplicateResourceException("Contact number already exist.");
        }

        Supplier supplier = supplierMapper.toEntity(request);

        Supplier savedSupplier = supplierRepository.save(supplier);
        log.info("Successfully created a supplier with id: {}",savedSupplier.getId());
        return supplierMapper.toDto(savedSupplier);
    }

    @Override
    public SupplierResponse updateSupplier(SupplierRequest request, Long id) {
        log.info("Updating supplier");
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        if(!existingSupplier.getVersion().equals(request.getVersion())){
            throw new OptimisticLockException("Supplier has been updated by another user");
        }

        if(supplierRepository.existsByNameAndIdNot(request.getName(),id)){
            throw new DuplicateResourceException("Supplier name already exist.");
        }

        if(supplierRepository.existsByContactNumberAndIdNot(request.getContactNumber(),id)){
            throw new DuplicateResourceException("Contact number already exist.");
        }


        existingSupplier.setName(request.getName());
        existingSupplier.setAddress(request.getAddress());
        existingSupplier.setContactNumber(request.getContactNumber());


        Supplier updatedSupplier = supplierRepository.save(existingSupplier);
        log.info("Successfully updated a supplier with id: {}",updatedSupplier.getId());
        return supplierMapper.toDto(updatedSupplier);
    }

    @Override
    public SupplierResponse getSupplier(Long id) {
        log.info("Fetching supplier");
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        log.info("Successfully fetched supplier with id: {}",id);
        return supplierMapper.toDto(supplier);


    }

    @Override
    public void deleteSupplier(Long id) {
        log.info("Deleting a supplier");
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        log.info("Successfully deleted a supplier with id: {}",id);
        supplier.setDeleted(true);
        supplierRepository.save(supplier);

    }
}
