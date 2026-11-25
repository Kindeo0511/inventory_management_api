package com.project1.backend.product;

import com.project1.backend.Inventory.Inventory;
import com.project1.backend.Inventory.Status;
import com.project1.backend.Product.Product;
import com.project1.backend.Product.ProductRepository;
import com.project1.backend.Product.ProductService;
import com.project1.backend.Product.ProductServiceImpl;
import com.project1.backend.Product.dto.ProductRequest;
import com.project1.backend.Product.dto.ProductResponse;
import com.project1.backend.Product.mapper.ProductMapper;
import com.project1.backend.category.Category;
import com.project1.backend.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void getAllProduct_shouldReturnAllProduct(){
        // Arrange
        Pageable pageable = PageRequest.of(0,10);

        Category category = Category.builder()
                .id(1L)
                .name("Drinks")
                .build();

        Product product1 = Product.builder()
                .id(1L)
                .name("Coke")
                .sellingPrice(BigDecimal.valueOf(20))
                .productImage("test_coke.jpg")
                .category(category)
                .build();

        Product product2 = Product.builder()
                .id(1L)
                .name("Royal")
                .sellingPrice(BigDecimal.valueOf(20))
                .productImage("test_royal.jpg")
                .category(category)
                .build();

        ProductResponse dto1 = ProductResponse.builder()
                .id(1L)
                .name("Coke")
                .categoryName("Drinks")
                .productImage("test_coke.jpg")
                .categoryId(1L)
                .sellingPrice(BigDecimal.valueOf(20))
                .build();

        ProductResponse dto2 = ProductResponse.builder()
                .id(2L)
                .name("Royal")
                .categoryName("Drinks")
                .productImage("royal_coke.jpg")
                .categoryId(1L)
                .sellingPrice(BigDecimal.valueOf(20))
                .build();

        Page<Product> page = new PageImpl<>(List.of(product1,product2));

        when(productRepository.findAll(any(Specification.class),eq(pageable)))
                .thenReturn(page);
        when(productMapper.toDto(product1)).thenReturn(dto1);
        when(productMapper.toDto(product2)).thenReturn(dto2);

        // Act

        Page<ProductResponse> response = productService.getAllProducts(null,null,pageable);

        // Assert
        assertNotNull(response);

        assertEquals("Coke",response.getContent().getFirst().getName());
        assertEquals("Drinks",response.getContent().getFirst().getCategoryName());
        assertEquals("Drinks",response.getContent().getFirst().getCategoryName());
        assertEquals(BigDecimal.valueOf(20),response.getContent().getFirst().getSellingPrice());

        assertEquals("Royal",response.getContent().getLast().getName());
        assertEquals("Drinks",response.getContent().getLast().getCategoryName());
        assertEquals(BigDecimal.valueOf(20),response.getContent().getLast().getSellingPrice());

        verify(productRepository,times(1)).findAll(any(Specification.class),eq(pageable));
    }

    @Test
    void createProduct_shouldSaveAndReturnProduct(){
        // Arrange
        Category category = Category.builder()
                .id(1L)
                .name("Drinks")
                .build();

        MockMultipartFile imageFile = new MockMultipartFile(
                "productImage",
                "coke.png",
                "image/png",
                "fake-image-bytes".getBytes()
        );

        ProductRequest request = ProductRequest.builder()
                .name("Coke")
                .categoryId(1L)
                .sellingPrice(BigDecimal.valueOf(20))
                .build();

        Product savedProduct = Product.builder()
                .id(1L)
                .name("Coke")
                .category(category)
                .sellingPrice(BigDecimal.valueOf(20))
                .productImage("coke.png")
                .build();

        Inventory inventory = Inventory.builder()
                .id(1L)
                .product(savedProduct)
                .stock(0)
                .status(Status.OUT_OF_STOCK)
                .build();





    }
}
