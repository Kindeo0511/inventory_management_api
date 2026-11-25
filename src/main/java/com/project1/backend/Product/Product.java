package com.project1.backend.Product;


import com.project1.backend.Delivery.DeliveryItem;
import com.project1.backend.Inventory.Inventory;

import com.project1.backend.Product.entity.ProductVariant;
import com.project1.backend.base.BaseEntity;
import com.project1.backend.category.Category;
import com.project1.backend.order.OrderItem;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(name = "selling_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "product_image", nullable = false)
    private String productImage;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Version
    private Long version;

    @OneToMany(mappedBy = "product")
    private List<ProductVariant> productVariants;


}
