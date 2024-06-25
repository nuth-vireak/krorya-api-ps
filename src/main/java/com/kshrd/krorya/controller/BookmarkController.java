package com.kshrd.krorya.controller;

import com.kshrd.krorya.model.dto.FoodDTO;
import com.kshrd.krorya.model.dto.RecipeDTO;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.request.FoodUpdateRequest;
import com.kshrd.krorya.model.request.RecipeUpdateRequest;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.service.FoodService;
import com.kshrd.krorya.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("api/v1/bookmarks")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Bookmark Controller", description = "Endpoints for managing bookmarks")
public class BookmarkController {
    private final FoodService foodService;
    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);
    private final RecipeService recipeService;

    @Operation(summary = "Get bookmarked foods", description = "Retrieve foods bookmarked by the current user")
    @GetMapping("/foods")
    public ResponseEntity<ApiResponse<List<FoodDTO>>> getFoodByBookmarked() {
        UUID currentUser = getUsernameOfCurrentUser();
        List<FoodDTO> food = foodService.getFoodByBookmarked(currentUser);
        ApiResponse<List<FoodDTO>> apiResponse = ApiResponse.<List<FoodDTO>>builder()
                .status(HttpStatus.OK)
                .payload(food)
                .localDateTime(LocalDateTime.now())
                .message("Get bookmarked of food successfully")
                .code(200)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Update bookmarked status for a food", description = "Update the bookmarked status for a food by its ID")
    @PutMapping("/foods")
    public ResponseEntity<ApiResponse<FoodDTO>> updateBookmarkedByFoodId(
            @Parameter(description = "Id of food to update bookmarked status", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")

            @Valid @RequestBody FoodUpdateRequest request
    ) {

        UUID currentUser = getUsernameOfCurrentUser();

        FoodDTO foodDTO = foodService.updateFoodBookmarked(request.getFoodId(), request.isBookmarked(), currentUser);
        return ResponseEntity.ok(
                ApiResponse.<FoodDTO>builder()
                        .message("bookmarked has change status successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .localDateTime(LocalDateTime.now())
                        .payload(foodDTO)
                        .build()
        );
    }

    @Operation(summary = "update bookmarked", description = "update bookmarked by recipe id")
    @PutMapping("/recipes")
    public ResponseEntity<ApiResponse<RecipeDTO>> updateBookmarkedByRecipeId(
            @Parameter(description = "Id of recipe to update bookmarked status", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")

            @Valid @RequestBody RecipeUpdateRequest request
    ) {

        UUID currentUser = getUsernameOfCurrentUser();

        RecipeDTO recipeDTO = recipeService.updateRecipeBookmarked(request.getRecipeId(), request.isBookmarked(), currentUser);
        return ResponseEntity.ok(
                ApiResponse.<RecipeDTO>builder()
                        .message("bookmarked has change status successfully")
                        .payload(recipeDTO)
                        .status(HttpStatus.OK)
                        .code(200)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @Operation(summary = "Get current user recipes by bookmarked", description = "Retrieve current recipes by bookmarked")
    @GetMapping("/recipes")
    public ResponseEntity<ApiResponse<List<RecipeDTO>>> getRecipesByBookmarked() {
        logger.info("Fetching recipes which is bookmarked");

        UUID currentUser = getUsernameOfCurrentUser();
        List<RecipeDTO> recipes = recipeService.getRecipesByBookmarked(currentUser);
        logger.info(recipes.toString());
        ApiResponse<List<RecipeDTO>> apiResponse = ApiResponse.<List<RecipeDTO>>builder()
                .status(HttpStatus.OK)
                .payload(recipes)
                .localDateTime(LocalDateTime.now())
                .message("Get bookmarked recipes successfully")
                .code(200)
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    UUID getUsernameOfCurrentUser() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser = userDetails.getAppUser();
        return appUser.getUserId();
    }
}
