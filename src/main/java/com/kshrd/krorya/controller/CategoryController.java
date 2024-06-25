package com.kshrd.krorya.controller;

import com.kshrd.krorya.convert.CategoryDTOConvertor;
import com.kshrd.krorya.model.dto.CategoryDTO;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Category Controller", description = "Endpoint for managing categories")
public class CategoryController {
    private final CategoryService categoryService;
    @Operation(summary = "Get all categories")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> GetAllCategoriesById(){
        List<CategoryDTO> categoryDTOList = categoryService.getAllCategories();
        return ResponseEntity.ok(
                ApiResponse.<List<CategoryDTO>>builder()
                        .message("Get all categories successfully!")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(categoryDTOList)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }



}
