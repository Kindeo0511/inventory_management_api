package com.project1.backend.Delivery;

import com.project1.backend.Delivery.dto.DeliveryRequest;
import com.project1.backend.Delivery.dto.DeliveryResponse;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/deliveries")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;


    @Operation(
            summary = "Get all deliveries",
            description = "Retrieves all product deliveries"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved list of deliveries",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DeliveryResponse.class)
            )
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponseData<Page<DeliveryResponse>>> getAllDelivery(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @ParameterObject @PageableDefault(sort = "id", direction = Sort.Direction.DESC)Pageable pageable
            ){

        Page<DeliveryResponse> deliveries = deliveryService.getAllDelivery(from, to, pageable);

        ApiResponseData<Page<DeliveryResponse>> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Deliveries fetched successfully",
                deliveries
        );

        return ResponseEntity.ok(response);


    }


    @Operation(
            summary = "Create delivery",
            description = "Create new product deliveries"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created new delivery",
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
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class))
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponseData<DeliveryResponse>> addDelivery(@RequestBody DeliveryRequest request){
        DeliveryResponse savedDelivery = deliveryService.createDelivery(request);

        ApiResponseData<DeliveryResponse> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Delivery created successfully",
                savedDelivery
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }


    @Operation(
            summary = "Get delivery by id",
            description = "Retrieve a delivery using delivery id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched delivery",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Delivery not found with the given id ",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponseData<DeliveryResponse>> getDelivery(@PathVariable Long id){
        DeliveryResponse delivery = deliveryService.getDelivery(id);
        ApiResponseData<DeliveryResponse> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Delivery fetched successfully",
                delivery
        );
        return ResponseEntity.ok(response);


    }

}
