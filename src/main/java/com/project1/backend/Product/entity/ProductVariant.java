package com.project1.backend.Product.entity;

import com.project1.backend.Delivery.DeliveryItem;
import com.project1.backend.Inventory.Inventory;
import com.project1.backend.Product.Product;
import com.project1.backend.order.OrderItem;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_variants")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "productVariant")
    private List<DeliveryItem> deliveryItem;

    @OneToMany(mappedBy = "productVariant")
    private List<OrderItem> orderItems;

    @OneToOne(mappedBy = "productVariant", cascade = CascadeType.ALL)
    private Inventory inventory;
}
