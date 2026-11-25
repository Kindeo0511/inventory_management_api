package com.project1.backend.order;

import com.project1.backend.Inventory.Inventory;
import com.project1.backend.Inventory.InventoryRepository;
import com.project1.backend.Inventory.Status;
import com.project1.backend.Product.Product;
import com.project1.backend.Product.ProductRepository;
import com.project1.backend.Product.ProductVariantRepository;
import com.project1.backend.Product.entity.ProductVariant;
import com.project1.backend.User.UserRepository;
import com.project1.backend.User.Users;
import com.project1.backend.exception.BadRequestException;
import com.project1.backend.exception.ResourceNotFoundException;
import com.project1.backend.order.dto.OrderItemRequest;
import com.project1.backend.order.dto.OrderRequest;
import com.project1.backend.order.dto.OrderResponse;
import com.project1.backend.order.helper.OrderHelper;
import com.project1.backend.order.mapper.OrderMapper;
import com.project1.backend.order.specification.OrderSpecification;
import com.project1.backend.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductVariantRepository productVariantRepository;
    private final OrderHelper orderHelper;
    @Override
    public Page<OrderResponse> getAllOrders(String name, Pageable pageable) {
        log.info("Fetching all orders");
        Specification<Order> specification = Specification.allOf(
                OrderSpecification.proccessedBy(name)
        );

        Page<Order> orders = orderRepository.findAll(specification, pageable);
        log.info("Successfully fetched {} orders",orders.getSize());
        return orders.map(orderMapper::toDto);
    }

    @Transactional
    @Override
    public OrderResponse addOrder(OrderRequest request) {
        log.info("Creating order");
        List<OrderItem> orderItems = new ArrayList<>();

        Order order = Order.builder()
                .orderNumber(orderHelper.generateOrderNumber())
                .build();

        Order savedOrder = orderRepository.save(order);

        for(OrderItemRequest items : request.getOrderItems()){
            ProductVariant productVariant = productVariantRepository.findById(items.getProductVariantId())
                    .orElseThrow(() -> {
                        log.warn("Product not found with id: {}",items.getProductVariantId());
                        return new ResourceNotFoundException("Product not found");
                    });

            Inventory inventory = inventoryRepository.findByProductVariantId(items.getProductVariantId())
                    .orElseThrow(() ->
                    {
                        log.warn("Product in inventory not found with id: {}",items.getProductVariantId());
                        return new ResourceNotFoundException("Product not found in inventory");
                    });

            if(items.getQty() > inventory.getStock()){
                String productName = productVariant.getProduct().getName();
                log.warn("Insufficient stock for product '{}': requested={}, available={}",
                        productName, items.getQty(), inventory.getStock());
                throw new BadRequestException("Insufficient stock for product: " + productName);
            }

            BigDecimal totalAmount = items.getAmount().multiply(BigDecimal.valueOf(items.getQty()));

            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .productVariant(productVariant)
                    .qty(items.getQty())
                    .amount(items.getAmount())
                    .totalAmount(totalAmount)
                    .build();

            orderItems.add(orderItem);

            inventory.setStock(inventory.getStock() - items.getQty());

            if(inventory.getStock() == 0){
                inventory.setStatus(Status.UNAVAILABLE);
            }
            log.info("Successfully created {} order",orderItems.size());
            inventoryRepository.save(inventory);
            orderItemRepository.save(orderItem);

        }

        order.setOrderItems(orderItems);

        return orderMapper.toDto(savedOrder);

    }
}
