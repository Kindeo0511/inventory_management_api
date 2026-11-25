package com.project1.backend.Inventory;

import com.project1.backend.Inventory.dto.InventoryResponse;
import com.project1.backend.payload.ApiResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/inventory")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @Operation(
            summary = "Get all inventory product",
            description = "Retrieves all product in inventory"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all products inventory",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponseData<Page<InventoryResponse>>> showAllInventory(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String categoryName,
            @ParameterObject @PageableDefault(sort = "id", direction = Sort.Direction.DESC)Pageable pageable
            ){
        Page<InventoryResponse> inventories = inventoryService.getAllInventory(productName, categoryName, pageable);
        ApiResponseData<Page<InventoryResponse>> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Inventories fetched successfully",
                inventories
        );
        return ResponseEntity.ok(response);

    }
}
