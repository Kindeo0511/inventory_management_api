package com.project1.backend.Product.controller;

import com.project1.backend.Product.ProductService;
import com.project1.backend.Product.ProductVariantService;
import com.project1.backend.Product.dto.ProductResponse;
import com.project1.backend.Product.dto.ProductVariantRequest;
import com.project1.backend.Product.dto.ProductVariantResponse;
import com.project1.backend.payload.ApiResponseData;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@AllArgsConstructor
@RequestMapping("/api/product-variant")
@PreAuthorize("hasRole('ADMIN')")
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    @GetMapping
    public ResponseEntity<ApiResponseData<Page<ProductVariantResponse>>> getAllProductVariants(
            @ParameterObject @PageableDefault(sort = "id", direction = Sort.Direction.DESC)Pageable pageable
            ){
        Page<ProductVariantResponse> productVariants = productVariantService.getAllProductVariants(pageable);

        ApiResponseData<Page<ProductVariantResponse>>  response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Product variants fetched successfully",
                productVariants
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponseData<ProductVariantResponse>> createProductVariant(
            @Valid
            @RequestBody
            ProductVariantRequest request){
        ProductVariantResponse savedVariant = productVariantService.createProductVariant(request);

        ApiResponseData<ProductVariantResponse> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Product variant created successfully",
                savedVariant
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponseData<ProductVariantResponse>> updateProductVariant(
            @Valid
            @RequestBody
            ProductVariantRequest request,
            @PathVariable Long id){

        ProductVariantResponse updatedVariant = productVariantService.updateProductVariant(request,id);

        ApiResponseData<ProductVariantResponse> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Product variant updated successfully",
                updatedVariant
        );

        return ResponseEntity.ok(response);

    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponseData<ProductVariantResponse>> getProductVariantById(@PathVariable Long id){

        ProductVariantResponse variant = productVariantService.getProductVariantById(id);

        ApiResponseData<ProductVariantResponse> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Product variant fetched successfully",
                variant
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponseData<ProductVariantResponse>> deleteProductVariantById(@PathVariable Long id){
        productVariantService.deleteProductVariantById(id);
        ApiResponseData<ProductVariantResponse> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Product variant deleted successfully",
                null
        );

        return ResponseEntity.ok(response);
    }



}
