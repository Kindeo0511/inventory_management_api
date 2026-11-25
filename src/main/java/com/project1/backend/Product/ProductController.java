package com.project1.backend.Product;

import com.project1.backend.Product.dto.ProductRequest;
import com.project1.backend.Product.dto.ProductResponse;
import com.project1.backend.payload.ApiResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/products")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;


    @Operation(
            summary = "Get all products",
            description = "Retrieves all products"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all products",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<ApiResponseData<Page<ProductResponse>>> getAllProduct(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String category,
            @ParameterObject @PageableDefault(sort = "id", direction = Sort.Direction.DESC)Pageable pageable
            ){

        Page<ProductResponse> products = productService.getAllProducts(productName, category, pageable);

        ApiResponseData<Page<ProductResponse>> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Products fetched successfully",
                products
        );

        return ResponseEntity.ok(response);

    }
    @Operation(
            summary = "Upload product image",
            description = "Upload a product image and returned the image file path"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product image uploaded successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid image provided",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            )

    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image-upload")
    public ResponseEntity<ApiResponseData<String>> uploadImage(MultipartFile image){
       String uploadedImage = productService.uploadImage(image);
        ApiResponseData<String> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Image uploaded successfully",
                uploadedImage
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }
    @Operation(
            summary = "Create product",
            description = "Create new product"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Product created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data provided",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Product with this name already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<ApiResponseData<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse savedProduct = productService.createProduct(request);

        ApiResponseData<ProductResponse> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Product created successfully",
                savedProduct
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @Operation(
            summary = "Update product",
            description = "Update a product using product id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data provided",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found with the given id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Product with this name already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponseData<ProductResponse>> updateProduct(@PathVariable Long id,
                                                                          @Valid @RequestBody ProductRequest request)  {

        ProductResponse updatedProduct = productService.updateProduct(request, id);
        ApiResponseData<ProductResponse> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Product updated successfully",
                updatedProduct
        );

        return ResponseEntity.ok(response);

    }

    @Operation(
            summary = "Delete product",
            description = "Delete a product using product id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found with the given id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponseData<Object>> deleteProductById(@PathVariable Long id) throws IOException {
        productService.deleteProduct(id);
        ApiResponseData<Object> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Product deleted successfully",
                null
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get product by id",
            description = "Retrieves a product using a product id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product fetched successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found with the given id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            )

    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponseData<ProductResponse>> getProductById(@PathVariable Long id){
        ProductResponse product= productService.getProduct(id);
        ApiResponseData<ProductResponse> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Product fetched successfully",
                product
        );
        return ResponseEntity.ok(response);

    }



}
