package com.project1.backend.order;

import com.project1.backend.order.dto.OrderRequest;
import com.project1.backend.order.dto.OrderResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/orders")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


    @Operation(
            summary = "Get all orders",
            description = "Retrieves all orders"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all orders",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponseData<Page<OrderResponse>>> getAllOrders(
            @RequestParam(required = false) String name,
            @ParameterObject @PageableDefault(sort = "id", direction = Sort.Direction.DESC)Pageable pageable
            ){
        Page<OrderResponse> orders = orderService.getAllOrders(name, pageable);

        ApiResponseData<Page<OrderResponse>> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Orders fethced successfully",
                orders
        );

        return ResponseEntity.ok(response);

    }


    @Operation(
            summary = "Create order",
            description = "Create new order"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created new order",
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
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            )
    })
    @PreAuthorize("hasAnyRole('CASHIER','MANAGER')")
    @PostMapping
    public ResponseEntity<ApiResponseData<OrderResponse>> addOrder(@Valid @RequestBody OrderRequest orderRequest){
        OrderResponse savedOrder = orderService.addOrder(orderRequest);

        ApiResponseData<OrderResponse> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Orders created successfully",
                savedOrder
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }
}
