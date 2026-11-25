package com.project1.backend.category;

import com.project1.backend.category.dto.CategoryResponse;
import com.project1.backend.category.mapper.CategoryMapper;
import com.project1.backend.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CategoryServiceIntergrationTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryService categoryService;


    @Test
    void shouldReturnCategoryById(){

        // Arrange
        Category mock_data = Category.builder()
                .name("Drinks")
                .build();
        Category savedCategory = categoryRepository.save(mock_data);

        // Act
        CategoryResponse response = categoryService.getCategory(savedCategory.getId());

        // Assert
        assertEquals(savedCategory.getId(),response.getId());
        assertEquals("Drinks",response.getName());
        assertFalse(response.isDeleted());
    }
    @Test
    void shouldThrowExceptionWhenCategoryNotFound(){
        assertThrows(
                ResourceNotFoundException.class,() ->
            categoryService.getCategory(999L)
        );
    }
}
