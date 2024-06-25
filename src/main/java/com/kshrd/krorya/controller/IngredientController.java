package com.kshrd.krorya.controller;

import com.kshrd.krorya.model.dto.IngredientDTO;
 
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.request.IngredientRequest;

import com.kshrd.krorya.model.dto.RecipeDTO;

import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/ingredients")
@AllArgsConstructor
@Tag(name = "Ingredient Controller", description = "Endpoints for managing ingredients")
public class IngredientController {

    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);
    private final IngredientService ingredientService;

    @Operation(summary = "Get all ingredients", description = "Retrieve all ingredients excluding those with user ID")
    @GetMapping
    public ResponseEntity<ApiResponse<List<IngredientDTO>>> getIngredients() {

        List<IngredientDTO> ingredients = ingredientService.getIngredients();
        System.out.println(ingredients);
        ApiResponse<List<IngredientDTO>> apiResponse = ApiResponse.<List<IngredientDTO>>builder()
                .status(HttpStatus.OK)
                .localDateTime(LocalDateTime.now())
                .message("Get all ingredients successfully")
                .payload(ingredients)
                .code(200)
                .build();
        return ResponseEntity.ok(apiResponse);
    }


//    @Operation(summary = "Get ingredient by name", description = "Retrieve ingredient by name")
//    @GetMapping
//    public ResponseEntity<ApiResponse<IngredientDTO>> getIngredientByName(
//            @Valid
//            @Parameter(description = "Name of the ingredient to be retrieved", example = "Salt")
//            @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Invalid input. Only text characters are allowed.")
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) UUID userId) {
//
//        logger.info("Fetching ingredient with name: {}", name);
//
//        if (userId == null) {
//            IngredientDTO ingredient = ingredientService.getIngredientByName(name);
//
//            ApiResponse<IngredientDTO> apiResponse = ApiResponse.<IngredientDTO>builder()
//                    .status(HttpStatus.OK)
//                    .localDateTime(LocalDateTime.now())
//                    .message("Get ingredient successfully")
//                    .payload(ingredient)
//                    .code(200)
//                    .build();
//            return ResponseEntity.ok(apiResponse);
//        }
//
//
//    }

//    @Operation(summary = "Get ingredient by name", description = "Retrieve ingredient by name")
//    @GetMapping
//    public ResponseEntity<ApiResponse<IngredientDTO>> getIngredientByName(
//            @Valid
//            @Parameter(description = "Name of the ingredient to be retrieved", example = "Salt")
//            @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Invalid input. Only text characters are allowed.")
//            @PathVariable String name) {
//        logger.info("Fetching ingredient with name: {}", name);
//
//        IngredientDTO ingredient;
//        try {
//            ingredient = ingredientService.getIngredientByName(name);
//        } catch (CustomNotFoundException e) {
//            logger.info("Ingredient not found, creating new ingredient for user: {}", "d1267640-4d60-4f27-9e4d-365285f0a0e9");
//            ingredient = ingredientService.createIngredientForUser(name, "d1267640-4d60-4f27-9e4d-365285f0a0e9");
//        }
//
//        ApiResponse<IngredientDTO> apiResponse = ApiResponse.<IngredientDTO>builder()
//                .status(HttpStatus.OK)
//                .localDateTime(LocalDateTime.now())
//                .message("Get ingredient successfully")
//                .payload(ingredient)
//                .code(200)
//                .build();
//        return ResponseEntity.ok(apiResponse);
//    }

//    UUID getUsernameOfCurrentUser() {
//        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        AppUser appUser = userDetails.getAppUser();
//        System.out.println("current user is : " + userDetails);
//        logger.info("current user: {}", userDetails);
//        return appUser.getUserId();
//    }
private UUID getCurrentUserId() {
    CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userDetails.getAppUser().getUserId();
}

//    @SecurityRequirement(name="bearerAuth")
//    @Operation(summary = "Get ingredient by name", description = "Retrieve an ingredient by name where user_id is NULL, or create a new ingredient if it exists with a non-NULL user_id")
//    @GetMapping("/by-name")
//    public ResponseEntity<ApiResponse<IngredientDTO>> getIngredientByName(
//            @Valid
//            @Parameter(description = "Name of the ingredient to be retrieved", example = "apple")
//            @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Invalid input. Only text characters are allowed.")
//            @RequestParam String name) {
//        IngredientDTO ingredient = ingredientService.getIngredientByNameOrCreate(name);
//        ApiResponse<IngredientDTO> apiResponse = ApiResponse.<IngredientDTO>builder()
//                .status(HttpStatus.OK)
//                .localDateTime(LocalDateTime.now())
//                .message("Get ingredient by name successfully")
//                .payload(ingredient)
//                .code(200)
//                .build();
//        return ResponseEntity.ok(apiResponse);
//    }

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Create new ingredient")
    @PostMapping
    public ResponseEntity<ApiResponse<IngredientDTO>> createNewIngredient(@Valid @RequestBody IngredientRequest ingredientRequest){
        UUID userId = getCurrentUserId();
        IngredientDTO ingredientDTO = ingredientService.createNewIngredient(ingredientRequest, userId);
        return ResponseEntity.ok(
                ApiResponse.<IngredientDTO>builder()
                        .message("A new ingredient is created successfully")
                        .status(HttpStatus.CREATED)
                        .code(201)
                        .payload(ingredientDTO)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }
}
