package com.project1.backend.category;

import com.project1.backend.category.dto.CategoryRequest;
import com.project1.backend.category.dto.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(CategoryRequest request, Long id);
    void deleteCategory(Long id);
    CategoryResponse getCategory(Long id);
    Page<CategoryResponse> getAllCategories(String categoryName, Pageable pageable);
}
