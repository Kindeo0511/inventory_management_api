package com.project1.backend.category;

import com.project1.backend.User.UserRepository;
import com.project1.backend.category.dto.CategoryRequest;
import com.project1.backend.category.dto.CategoryResponse;
import com.project1.backend.category.mapper.CategoryMapper;
import com.project1.backend.category.specification.CategorySpecification;
import com.project1.backend.exception.DuplicateResourceException;
import com.project1.backend.exception.ResourceNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements  CategoryService{
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    @Override
    public CategoryResponse createCategory(CategoryRequest request) {

        log.debug("Creating category");

        if (categoryRepository.existsByName(request.getSubName())){
            log.warn("Category already exists with name: {}",request.getName());
            throw new DuplicateResourceException(
                    "Category already exists."
            );
        }


        Category newCategory = categoryMapper.toEntity(request);

        Category savedCategory = categoryRepository.save(newCategory);

        log.info("Category created successfully");
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(CategoryRequest request, Long id) {

        log.debug("Updating category");
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() ->{
                    log.warn("Category not found with id: {}",id);
                    return new ResourceNotFoundException("Category not found.");
                    }
                );

        if(!existingCategory.getVersion().equals(request.getVersion())){
            throw new OptimisticLockException("Category is already being updated by another user");
        }

        if(categoryRepository.existsByNameAndIdNot(request.getName(), id)){
            log.warn("Category already exists with id: {}",id);
            throw new DuplicateResourceException(
                    "Category already exists."
            );
        }


        existingCategory.setName(request.getName());

        Category updatedCategory = categoryRepository.save(existingCategory);
        log.info("Category updated successfully");
        return categoryMapper.toDto(updatedCategory);
    }

    @Transactional
    @Override
    public void deleteCategory(Long id) {
        log.debug("Deleting category");
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                {
                    log.warn("Category not found with id: {}",id);
                    return new ResourceNotFoundException("Category not found.");
                }
                );
        category.setDeleted(true);
        categoryRepository.save(category);
        log.info("Category deleted successfully");

    }

    @Override
    public CategoryResponse getCategory(Long id) {
        log.debug("Fetching category with id: {}",id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        {
                            log.warn("Category not found with id: {}",id);
                            return new ResourceNotFoundException("Category not found");
                        }
                );
        log.info("Category fetched successfully with id: {}",id);
        return  categoryMapper.toDto(category);

    }

    @Override
    public Page<CategoryResponse> getAllCategories(String categoryName, Pageable pageable) {

        log.debug("Fetching all categories");
        Specification<Category> specification = Specification.allOf(
                CategorySpecification.hasName(categoryName)
        );

        Page<Category> categories = categoryRepository.findAll(specification,pageable);
        log.info("Successfully fetched {} categories",categories.getSize());
        return categories.map(categoryMapper::toDto);

    }
}
