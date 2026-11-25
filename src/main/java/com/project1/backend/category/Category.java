package com.project1.backend.category;


import com.project1.backend.Product.Product;
//import com.project1.backend.base.BaseEntity;
import com.project1.backend.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "categories")
@Builder
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String subName;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted= false;

    @OneToMany(mappedBy = "category", fetch= FetchType.LAZY)
    private List<Product> productList;

    @Version
    private Long version;

}
