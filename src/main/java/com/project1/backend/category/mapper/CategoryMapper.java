package com.project1.backend.category.mapper;

import com.project1.backend.category.Category;
import com.project1.backend.category.dto.CategoryRequest;
import com.project1.backend.category.dto.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request){
        return Category.builder()
                .name(request.getName())
                .subName(request.getSubName())
                .build();

    }
    public CategoryResponse toDto(Category category){
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .subName(category.getSubName())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .createdBy(category.getCreatedBy())
                .updatedBy(category.getUpdatedBy())
                .isDeleted(category.isDeleted())
                .build();

    }
}
