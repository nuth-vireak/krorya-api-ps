package com.kshrd.krorya.controller;

import com.kshrd.krorya.model.dto.CuisinesDTO;
import com.kshrd.krorya.model.dto.RecipeDTO;
import com.kshrd.krorya.model.entity.Cuisines;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.service.CuisineService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/cuisines")
@AllArgsConstructor
@Tag(name = "Cuisine Controller", description = "Endpoints for managing Cuisines")
public class CuisineController {
    private final CuisineService cuisineService;

    @GetMapping()
    public ResponseEntity<?> getCuisines() {
        ApiResponse<List<CuisinesDTO>> apiResponse = ApiResponse.<List<CuisinesDTO>>builder()
                .status(HttpStatus.OK)
                .payload(cuisineService.getCuisines())
                .localDateTime(LocalDateTime.now())
                .message("Get all cuisines successfully")
                .code(201)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
