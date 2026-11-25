package com.project1.backend.Delivery;

import com.project1.backend.Delivery.dto.DeliveryItemRequest;
import com.project1.backend.Delivery.dto.DeliveryRequest;
import com.project1.backend.Delivery.dto.DeliveryResponse;
import com.project1.backend.Delivery.mapper.DeliveryMapper;
import com.project1.backend.Delivery.specification.DeliverySpecefication;
import com.project1.backend.Inventory.Inventory;
import com.project1.backend.Inventory.InventoryRepository;
import com.project1.backend.Inventory.Status;
import com.project1.backend.Product.Product;
import com.project1.backend.Product.ProductRepository;
import com.project1.backend.Product.ProductVariantRepository;
import com.project1.backend.Product.entity.ProductVariant;
import com.project1.backend.exception.ResourceNotFoundException;
import com.project1.backend.supplier.Supplier;
import com.project1.backend.supplier.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService{
    private final DeliveryRepository deliveryRepository;
    private final DeliveryItemRepository deliveryItemRepository;
    private final SupplierRepository supplierRepository;
    private final ProductVariantRepository productVariantRepository;
    private final InventoryRepository inventoryRepository;
    private final DeliveryMapper deliveryMapper;

    @Override
    public Page<DeliveryResponse> getAllDelivery(LocalDate from, LocalDate to, Pageable pageable) {

        log.info("Fetching all deliveries");
        Specification<Delivery> specification = Specification.allOf(
                DeliverySpecefication.hasDate(from,to)
        );

        Page<Delivery> deliveries = deliveryRepository.findAll(specification, pageable);

        log.info("Successfully fetched {} deliveries", deliveries.getSize());
        return deliveries.map(deliveryMapper::toDto);
    }

    @Override
    public DeliveryResponse getDelivery(Long id) {
        log.info("Fetching delivery with id: {}",id);
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Supplier not found."));

        log.info("Successfully fetched delivery with id: {}",id);
        return deliveryMapper.toDto(delivery);
    }

    @Transactional
    @Override
    public DeliveryResponse createDelivery(DeliveryRequest request) {

        log.info("Creating delivery");
        Delivery delivery = new Delivery();
        List<DeliveryItem> items = new ArrayList<>();

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(()-> {
                    log.warn("Supplier not found with id: {}",request.getSupplierId());
                    return new ResourceNotFoundException("Supplier not found.");
                });


        delivery.setSupplier(supplier);


        Delivery savedDelivery = deliveryRepository.save(delivery);

        for (DeliveryItemRequest itemRequest : request.getItems()){

            ProductVariant productVariant = productVariantRepository.findById(itemRequest.getProductVariantId())
                    .orElseThrow(()->
                                {
                                    log.warn("Product variant not found with id: {}",itemRequest.getProductVariantId());
                                    return new ResourceNotFoundException("Product variant not found.");
                                }
                            );

            Inventory inventory = inventoryRepository.findByProductVariantId(productVariant.getId())
                    .orElseThrow(()->
                            {
                                log.warn("Product variant in inventory not found with id: {}",productVariant.getId());
                                return new ResourceNotFoundException("The product varaint in inventory not found.");
                            }
                    );


            inventory.setStock(inventory.getStock() + itemRequest.getQty());
            inventory.setStatus(Status.AVAILABLE);

            BigDecimal totalPrice = itemRequest.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQty()));
            DeliveryItem item = DeliveryItem.builder()
                    .productVariant(productVariant)
                    .delivery(savedDelivery)
                    .qty(itemRequest.getQty())
                    .price(itemRequest.getPrice())
                    .totalPrice(totalPrice)
                    .build();


            deliveryItemRepository.save((item));
            inventoryRepository.save(inventory);
            items.add(item);
        }
        delivery.setDeliveryItem(items);
        log.info("Successfully created delivery with id: {}",delivery.getId());
        return deliveryMapper.toDto(savedDelivery);

    }
}
