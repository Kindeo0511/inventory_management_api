package com.project1.backend.Delivery;

import com.project1.backend.Delivery.dto.DeliveryRequest;
import com.project1.backend.Delivery.dto.DeliveryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface DeliveryService {
    Page<DeliveryResponse> getAllDelivery(LocalDate from, LocalDate to, Pageable pageable);
    DeliveryResponse getDelivery(Long id);
    DeliveryResponse createDelivery(DeliveryRequest request);
}
