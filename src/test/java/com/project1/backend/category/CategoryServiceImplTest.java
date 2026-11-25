package com.project1.backend.category;

import com.project1.backend.category.dto.CategoryRequest;
import com.project1.backend.category.dto.CategoryResponse;
import com.project1.backend.category.mapper.CategoryMapper;
import com.project1.backend.exception.DuplicateResourceException;
import com.project1.backend.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void createCategory_shouldSaveAndReturnCategory(){

//    Arrange
        CategoryRequest request = CategoryRequest.builder()
                .name("Drinks")
                .build();

        Category entity = Category.builder()
                .name("Drinks")
                .build();

        Category savedCategory = Category.builder()
                .id(1L)
                .name("Drinks")
                .isDeleted(false)
                .version(0L)
                .build();

        CategoryResponse dto  = CategoryResponse.builder()
                .id(1L)
                .name("Drinks")
                .isDeleted(false)
                .build();

        when(categoryRepository.existsByName("Drinks")).thenReturn(false);
        when(categoryMapper.toEntity(request)).thenReturn(entity);
        when(categoryRepository.save(entity)).thenReturn(savedCategory);
        when(categoryMapper.toDto(savedCategory)).thenReturn(dto);

//        Act
        CategoryResponse response = categoryService.createCategory(request);
//        Assert
        assertNotNull(response);
        assertEquals(1L,response.getId());
        assertEquals("Drinks",response.getName());
        assertFalse(response.isDeleted());
        verify(categoryRepository,times(1)).existsByName("Drinks");
        verify(categoryRepository,times(1)).save(entity);

    }

    @Test
    void createCategory_shouldThrowExceptionWhenCategoryAlreadyExists(){
        // Arrange
        Category existingCategory = Category.builder()
                .id(1L)
                .name("Drinks")
                .isDeleted(false)
                .version(0L)
                .build();

        CategoryRequest request = CategoryRequest.builder()
                .name("Drinks")
                .build();


        when(categoryRepository.existsByName("Drinks")).thenReturn(true);

        // Act
        assertThrows(
                DuplicateResourceException.class, () ->
                        categoryService.createCategory(request)
        );

        // Assert

        verify(categoryRepository,never()).save(any());
    }

    @Test
    void updateCategory_shouldUpdateAndReturnCategory(){
//        Arrange
        Category existingCategory = Category.builder()
                .id(1L)
                .name("Drinks")
                .isDeleted(false)
                .version(0L)
                .build();

        CategoryRequest request = CategoryRequest.builder()
                .name("Snacks")
                .version(0L)
                .build();

        Category updatedCategory = Category.builder()
                .id(1L)
                .name("Snacks")
                .isDeleted(false)
                .version(1L)
                .build();

        CategoryResponse dto = CategoryResponse.builder()
                .id(1L)
                .name("Snacks")
                .isDeleted(false)
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByNameAndIdNot("Snacks",1L)).thenReturn(false);
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(dto);
//        Act
        CategoryResponse response = categoryService.updateCategory(request,1L);
//        Assert

        assertNotNull(response);
        assertEquals("Snacks",existingCategory.getName());
        verify(categoryRepository,times(1)).save(existingCategory);
    }

    @Test
    void updateCategory_shouldThrowExceptionWhenCategoryNotFound(){
        // Arrange
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
        // Act

        assertThrows(
                ResourceNotFoundException.class, () ->
                        categoryService.getCategory(999L)
        );
        // Assert
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void updateCategory_shouldThrowExceptionWhenCategoryAndIdAlreadyExists(){
        // Arrange
        Category existingCategory = Category.builder()
                .id(1L)
                .name("Drinks")
                .isDeleted(false)
                .version(0L)
                .build();

        CategoryRequest request = CategoryRequest.builder()
                .name("Drinks")
                .version(0L)
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByNameAndIdNot("Drinks",1L)).thenReturn(true);

        // Act
        assertThrows(
                DuplicateResourceException.class, () ->
                        categoryService.updateCategory(request,1L)
        );

        // Assert
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void getAllCategories_shouldReturnAllCategories(){

        //Arrange
        Pageable pageable = PageRequest.of(0, 10);

        Category category_1 = new Category();
        category_1.setId(1L);
        category_1.setName("Drinks");

        Category category_2 = new Category();
        category_2.setId(2L);
        category_2.setName("Snacks");

        CategoryResponse dto_1 = new CategoryResponse();
        dto_1.setId(1L);
        dto_1.setName("Drinks");

        CategoryResponse dto_2 = new CategoryResponse();
        dto_2.setId(1L);
        dto_2.setName("Snacks");


        Page<Category> page = new PageImpl<>(List.of(category_1,category_2),pageable,2);

        when(categoryRepository.findAll(any(Specification.class),eq(pageable)))
                .thenReturn(page);
        when(categoryMapper.toDto(category_1)).thenReturn(dto_1);
        when(categoryMapper.toDto(category_2)).thenReturn(dto_2);
        //Act
        Page<CategoryResponse> response = categoryService.getAllCategories(null,pageable);

        //Assert
        assertEquals(2,response.getTotalElements());
        assertEquals(2,response.getContent().size());
        assertEquals("Drinks",response.getContent().getFirst().getName());
        assertEquals("Snacks",response.getContent().getLast().getName());
        verify(categoryRepository).findAll(any(Specification.class),eq(pageable));
    }

    @Test
    void getCategoryById_shouldReturnCategoryIfExists(){

        //Arrange
        Category category = new Category();
        category.setId(1L);
        category.setName("Drinks");

        CategoryResponse dto = new CategoryResponse();
        dto.setId(1L);
        dto.setName("Drinks");


        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category))
                .thenReturn(dto);
        //Act
        CategoryResponse response = categoryService.getCategory(1L);

        //Assert
        assertEquals(1L,response.getId());
        assertEquals("Drinks",response.getName());
        verify(categoryRepository,times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound(){

        //Arrange
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,() ->
                        categoryService.getCategory(999L)
        );
    }
    @Test
    void deleteCategoryById_shouldDeleteCategory(){
//        Arrange
        Category existingCategory = Category.builder()
                .id(1L)
                .name("Drinks")
                .isDeleted(false)
                .version(0L)
                .build();

        Category deletedCategory = Category.builder()
                .id(1L)
                .name("Drinks")
                .isDeleted(true)
                .version(0L)
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(deletedCategory);
//        Act
        categoryService.deleteCategory(1L);
//        Assert
        assertTrue(existingCategory.isDeleted());
        verify(categoryRepository,times(1)).save(deletedCategory);


    }

    @Test
    void deleteCategoryById_shouldThrowExceptionWhenCategoryNotFound(){
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        // Act
        assertThrows(
                ResourceNotFoundException.class, () ->
                        categoryService.deleteCategory(1L)
        );
        // Assert
        verify(categoryRepository, never()).deleteById(any());
    }


}
