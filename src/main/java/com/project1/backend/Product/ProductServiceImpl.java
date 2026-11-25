package com.project1.backend.Product;

import com.project1.backend.Inventory.Inventory;
import com.project1.backend.Inventory.InventoryRepository;
import com.project1.backend.Inventory.Status;
import com.project1.backend.Product.ProductHelper.Helper;
import com.project1.backend.Product.dto.ProductRequest;
import com.project1.backend.Product.dto.ProductResponse;
import com.project1.backend.Product.mapper.ProductMapper;
import com.project1.backend.Product.specification.ProductSpecification;
import com.project1.backend.User.UserRepository;
import com.project1.backend.User.Users;
import com.project1.backend.category.Category;
import com.project1.backend.category.CategoryRepository;
import com.project1.backend.exception.BadRequestException;
import com.project1.backend.exception.DuplicateResourceException;
import com.project1.backend.exception.ResourceNotFoundException;
import com.project1.backend.util.SecurityUtils;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;
    private final FileStorageRepository fileStorageRepository;
    private final Helper helper;

    @Override
    public Page<ProductResponse> getAllProducts(String productName, String category, Pageable pageable) {

        log.debug("Fetching all products");
        Specification<Product> specification = Specification.allOf(
                ProductSpecification.hasName(productName),
                ProductSpecification.hasCategory(category)
                );

        Page<Product> products = productRepository.findAll(specification, pageable);
        log.info("Successfully fetched {} products", products.getSize());
        return products
                .map(productMapper::toDto);
    }


    @Transactional
    @Override
    public ProductResponse createProduct(ProductRequest request) {
        log.debug("Creating product");
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->{
                    log.warn("Category not found with id: {}",request.getCategoryId());
                    return new ResourceNotFoundException("Category not found.");

                });

        if(productRepository.existsByName(request.getName())){
            log.warn("Product already exists with name: {}",request.getName());
            throw new DuplicateResourceException(
                    "Product already exists."
            );
        }

        Product product = Product.builder()
                        .code(helper.generateProductCode(category))
                        .name(request.getName())
                        .brand(request.getBrand())
                        .sellingPrice(request.getSellingPrice())
                        .category(category)
                        .productImage(request.getImageFile())
                        .build();

        Product savedProduct = productRepository.save(product);
        log.info("Successfully created product with id: {}",savedProduct.getId());
        return productMapper.toDto(savedProduct);
    }


    @Override
    public ProductResponse updateProduct(ProductRequest request, Long id){
        log.debug("Updating a product with id: {}",id);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        if(!existingProduct.getVersion().equals(request.getVersion())){
            throw new OptimisticLockException("Product is already being updated by another user");
        }

        if (productRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new DuplicateResourceException("Product already exists.");
        }



        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));


        existingProduct.setName(request.getName());
        existingProduct.setName(request.getBrand());
        existingProduct.setSellingPrice(request.getSellingPrice());
        existingProduct.setCategory(category);
        existingProduct.setProductImage(request.getImageFile());
        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Successfully updated a product with id: {}",id);

        return productMapper.toDto(updatedProduct);



    }

    @Override
    public ProductResponse getProduct(Long id) {
        log.debug("Fetching product");
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
        log.info("Successfully fetched product with id: {}",id);
        return productMapper.toDto(product);
    }

    @Override
    public void deleteProduct(Long id) throws IOException {
        log.debug("Deleting a product");
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        product.setDeleted(true);
//        String image = product.getProductImage();
        log.info("Successfully deleted a product with id: {}",id);
        productRepository.save(product);
//        fileStorageRepository.deleteFile(image);
    }

    @Override
    public String uploadImage(MultipartFile image) {
        log.debug("Uploading image");
        if(image.isEmpty()){
            log.warn("Product image cannot be empty");
            throw new BadRequestException("Product image cannot be empty");
        }

        String contentType = image.getContentType();

        if(!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)){
            log.warn("Only JPG and PNG images are allowed");
            throw new BadRequestException("Only JPG and PNG images are allowed");
        }

        if(image.getSize() > 5*1024*1024){
            log.warn("File size must be less then 5 MB");
            throw new BadRequestException("File size must be less then 5 MB");
        }
            try {
                log.info("Successfully uploaded image");
            return fileStorageRepository.storeFile(image);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store product image",e);
        }
    }
}
