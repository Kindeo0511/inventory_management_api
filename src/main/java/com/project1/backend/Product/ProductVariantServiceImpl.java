package com.project1.backend.Product;

import com.project1.backend.Inventory.Inventory;
import com.project1.backend.Inventory.Status;
import com.project1.backend.Product.ProductHelper.Helper;
import com.project1.backend.Product.dto.ProductVariantRequest;
import com.project1.backend.Product.dto.ProductVariantResponse;
import com.project1.backend.Product.entity.ProductVariant;
import com.project1.backend.Product.mapper.ProductVariantMapper;
import com.project1.backend.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService{
    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;
    private final ProductVariantMapper productVariantMapper;
    private final Helper helper;

    @Override
    public Page<ProductVariantResponse> getAllProductVariants(Pageable pageable) {
        log.debug("Fetching all product variants");
        Page<ProductVariant> page = productVariantRepository.findAll(pageable);
        log.info("Successfully fetched all product variants");
        return page.map(productVariantMapper::toDto);
    }

    @Override
    public ProductVariantResponse getProductVariantById(Long id) {
        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found with the id " + id));
        return productVariantMapper.toDto(productVariant);
    }

    @Transactional
    @Override
    public ProductVariantResponse createProductVariant(ProductVariantRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id "+ request.getProductId()));

        ProductVariant productVariant = productVariantMapper.toEntity(request);
        productVariant.setSku(helper.generateSku(product,request));
        productVariant.setProduct(product);
        Inventory inventory = Inventory.builder()
                .productVariant(productVariant)
                .stock(0)
                .status(Status.OUT_OF_STOCK)
                .build();
        productVariant.setInventory(inventory);
        ProductVariant savedProductVariant = productVariantRepository.save(productVariant);

        return productVariantMapper.toDto(savedProductVariant);
    }


    @Transactional
    @Override
    public ProductVariantResponse updateProductVariant(ProductVariantRequest request, Long id) {

        Product existingProduct = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id" + id));

        ProductVariant existingVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found with id" + id));


        existingVariant.setProduct(existingProduct);
        existingVariant.setSku(request.getSku());
        existingVariant.setSize(request.getSize());
        existingVariant.setColor(request.getColor());

        ProductVariant updatedVariant = productVariantRepository.save(existingVariant);

        return productVariantMapper.toDto(updatedVariant);

    }

    @Override
    public void deleteProductVariantById(Long id) {

        ProductVariant existingVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found with id" + id));
        existingVariant.setDeleted(true);
        productVariantRepository.save(existingVariant);
    }
}
