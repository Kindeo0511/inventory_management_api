package com.project1.backend.supplier.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierRequest {
    @NotBlank(message = "Supplier name is required.")
    private String name;
    @NotBlank(message = "Address is required.")
    private String address;
    @NotBlank(message = "Contact number is required.")
    private String contactNumber;

    private Long version;
}
