package com.project1.backend.supplier;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project1.backend.Delivery.Delivery;
import com.project1.backend.User.Users;
import com.project1.backend.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "supplier")
public class Supplier extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(unique = true, nullable = false)
    private String contactNumber;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Version
    private Long version;


    @OneToMany(mappedBy = "supplier")
    private List<Delivery> delivery;


}
