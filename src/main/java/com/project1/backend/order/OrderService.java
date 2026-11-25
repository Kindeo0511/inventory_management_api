package com.project1.backend.order;

import com.project1.backend.order.dto.OrderRequest;
import com.project1.backend.order.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    Page<OrderResponse> getAllOrders(String name, Pageable pageable);
    OrderResponse addOrder(OrderRequest request);
}
