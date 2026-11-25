package com.project1.backend.Product;

import com.project1.backend.Product.dto.ProductRequest;
import com.project1.backend.Product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ProductService {
    Page<ProductResponse> getAllProducts(String productName, String category, Pageable pageable);
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(ProductRequest request, Long id) ;
    ProductResponse getProduct(Long id);
    void deleteProduct(Long id) throws IOException;
    String uploadImage(MultipartFile image);

}
