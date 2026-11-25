package com.project1.backend.category;

import com.project1.backend.category.dto.CategoryRequest;
import com.project1.backend.category.dto.CategoryResponse;
import com.project1.backend.payload.ApiResponseData;
import com.project1.backend.payload.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/categories")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Category ")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;


    @Operation(
            summary = "Get all categories",
            description = "Retrieves all categories."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved list of categories.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseData.class)
            )
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponseData<Page<CategoryResponse>>>  getAllCategories(
            @RequestParam(required = false) String categoryName,
            @ParameterObject @PageableDefault(sort = "id")Pageable pageable
            ){
        Page<CategoryResponse> categories = categoryService.getAllCategories(categoryName, pageable);
        ApiResponseData<Page<CategoryResponse>> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Categories fetched successfully",
                 categories
        );
        return ResponseEntity.ok(response);

    }

    @Operation(
            summary = "Create category",
            description = "Create new category"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Category created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data provided",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Category with this name already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class))
            ),

    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity <ApiResponseData<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest request){

        CategoryResponse savedCategory = categoryService.createCategory(request);
        ApiResponseData<CategoryResponse> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Category created successfully",
                savedCategory
        );


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @Operation(
            summary = "Update category",
            description = "Update a category using category id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully updated a category",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data provided",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Category with this name already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponseData<CategoryResponse>> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request){
        CategoryResponse updatedCategory = categoryService.updateCategory(request, id);
        ApiResponseData<CategoryResponse> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Category updated successfully",
                updatedCategory
        );
        return ResponseEntity.ok(response);


    }

    @Operation(
            summary = "Get category by id",
            description = "Retrieves a category using the category id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched category",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data provided",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found with the given id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity <ApiResponseData<CategoryResponse>> getCategory(@Parameter(description = "Category ID",
    required = true) @PathVariable Long id){
        CategoryResponse category = categoryService.getCategory(id);
        ApiResponseData<CategoryResponse> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Category fetched successfully",
                category
        );
        return ResponseEntity.ok(response);


    }

    @Operation(
            summary = "Delete category by id",
            description = "Delete category using category id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully deleted category",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found with the given id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseData.class)
                    )
            ),
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponseData<Object>> deleteCategoryById(@PathVariable Long id){
        categoryService.deleteCategory(id);
        ApiResponseData<Object> response = new ApiResponseData<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Category deleted successfully",
                null
        );
        return ResponseEntity.ok(response);
    }
}
