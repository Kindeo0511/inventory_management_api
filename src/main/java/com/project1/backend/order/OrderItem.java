package com.project1.backend.order;

import com.project1.backend.Product.Product;
import com.project1.backend.Product.entity.ProductVariant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int qty;

    @Column(nullable = false, precision = 10 ,scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 10 ,scale = 2)
    private BigDecimal totalAmount;

    @ManyToOne()
    @JoinColumn(name = "item_order_id")
    private Order order;

    @ManyToOne()
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;
}
