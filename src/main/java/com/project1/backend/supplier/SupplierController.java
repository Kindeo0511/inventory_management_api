package com.project1.backend.supplier;

import com.project1.backend.payload.ApiResponseData;
import com.project1.backend.supplier.dto.SupplierRequest;
import com.project1.backend.supplier.dto.SupplierResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/suppliers")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get all suppliers",
            description = "Retrieves all suppliers"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all supplier",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            )

    })
    @GetMapping
    public ResponseEntity<ApiResponseData<Page<SupplierResponse>>> getAllSupplier(
            @RequestParam(required = false) String supplierName,
            @ParameterObject @PageableDefault(sort = "id", direction = Sort.Direction.DESC)Pageable pageable
            ){
        Page<SupplierResponse> suppliers = supplierService.getAllSuppliers(supplierName, pageable);

        ApiResponseData<Page<SupplierResponse>> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Suppliers fetched successfully",
                suppliers
        );
        return ResponseEntity.ok(response);


    }

    @Operation(
            summary = "Create supplier",
            description = "Create new supplier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Supplier created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data provided",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Supplier with this name or contact number already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class))
            ),

    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponseData<SupplierResponse>> addSupplier(@Valid @RequestBody SupplierRequest request ){

        SupplierResponse savedSupplier = supplierService.createSupplier(request);
        ApiResponseData<SupplierResponse> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Supplier created successfully",
                savedSupplier
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }


    @Operation(
            summary = "Update supplier",
            description = "Update a supplier using supplier id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Supplier updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data provided",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Supplier not found with the given id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Supplier with this name or contact number already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class))
            ),

    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponseData<SupplierResponse>> updateSupplier(@PathVariable Long id, @Valid @RequestBody SupplierRequest request){

        SupplierResponse updatedSupplier = supplierService.updateSupplier(request,id);
        ApiResponseData<SupplierResponse> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Supplier updated successfully",
                updatedSupplier
        );

        return ResponseEntity.ok(response);

    }

    @Operation(
            summary = "Delete supplier",
            description = "Delete a supplier using supplier id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Supplier deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Supplier not found with the given id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class))
            )


    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponseData<Object>> deleteSupplier(@PathVariable Long id){

        supplierService.deleteSupplier(id);
        ApiResponseData<Object> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Supplier deleted successfully",
                null
        );
        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Get supplier by id",
            description = "Retrieve supplier using supplier id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Supplier fetched successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Supplier not found with the given id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class))
            )


    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponseData<SupplierResponse>> getSupplierById(@PathVariable Long id){
        SupplierResponse supplier = supplierService.getSupplier(id);

        ApiResponseData<SupplierResponse> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Supplier fetched successfully",
                supplier
        );
        return ResponseEntity.ok(response);

    }
}
