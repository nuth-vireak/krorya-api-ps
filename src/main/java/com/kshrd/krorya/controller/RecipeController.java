package com.kshrd.krorya.controller;

import com.kshrd.krorya.model.dto.RecipeDTO;
import com.kshrd.krorya.model.dto.RecipeIngredientDTO;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.entity.Recipe;
import com.kshrd.krorya.model.enumeration.CookingLevelEnum;
import com.kshrd.krorya.model.enumeration.CookingTimeEnum;
import com.kshrd.krorya.model.enumeration.CuisineEnum;
import com.kshrd.krorya.model.enumeration.IngredientsEnum;
import com.kshrd.krorya.model.request.RecipeRequest;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@Tag(name = "Recipe Controller", description = "Endpoints for managing recipes")
public class RecipeController {
    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);
    private final RecipeService recipeService;

    @Operation(summary = "Get all recipes", description = "Retrieve all recipes")
    @GetMapping("/recipes")
    public ResponseEntity<ApiResponse<List<RecipeDTO>>> getRecipes() {
        logger.info("Fetching all recipes");
        List<RecipeDTO> recipes = recipeService.getRecipes();
        ApiResponse<List<RecipeDTO>> apiResponse = ApiResponse.<List<RecipeDTO>>builder()
                .status(HttpStatus.OK)
                .payload(recipes)
                .localDateTime(LocalDateTime.now())
                .message("Get all recipes successfully")
                .code(200)
                .build();
        return ResponseEntity.ok(apiResponse);

    }


    @Operation(summary = "Get recipes by random", description = "Retrieve recipes by random")
    @GetMapping("/recipes/random")
    public ResponseEntity<ApiResponse<List<RecipeDTO>>> getRecipesByRandom() {
        logger.info("Fetching random recipes");

        List<RecipeDTO> recipes = recipeService.getRecipesByRandom();
        ApiResponse<List<RecipeDTO>> apiResponse = ApiResponse.<List<RecipeDTO>>builder()
                .status(HttpStatus.OK)
                .payload(recipes)
                .localDateTime(LocalDateTime.now())
                .message("Get random recipes successfully")
                .code(200)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "get recipes by user id", description = "Retrieve recipes by user id")
    @GetMapping("/recipes/user/{userId}")
    public ResponseEntity<ApiResponse<List<RecipeDTO>>> getRecipeByUserId(
            @Valid @Parameter(description = "User Id to get recipes", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID userId
    ) {
        logger.info("fetching recipe by user id");

        List<RecipeDTO> recipes = recipeService.getRecipeByUserId(userId);
        ApiResponse<List<RecipeDTO>> apiResponse = ApiResponse.<List<RecipeDTO>>builder()
                .status(HttpStatus.OK)
                .payload(recipes)
                .localDateTime(LocalDateTime.now())
                .message("get recipes by user id successfully")
                .code(200)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "get recipes by current user id", description = "Retrieve recipes by current user id")
    @GetMapping("/users/recipes")
    public ResponseEntity<ApiResponse<List<RecipeDTO>>> getRecipeByCurrentUserId() {
        logger.info("fetching recipe by user id");

        UUID currentUser = getCurrentUserId();

        List<RecipeDTO> recipes = recipeService.getRecipeByCurrentUserId(currentUser);
        ApiResponse<List<RecipeDTO>> apiResponse = ApiResponse.<List<RecipeDTO>>builder()
                .status(HttpStatus.OK)
                .payload(recipes)
                .localDateTime(LocalDateTime.now())
                .message("get recipes by user id successfully")
                .code(200)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Get recipe by ID", description = "Retrieve a recipe by its ID, ")
    @GetMapping("/recipes/{id}")
    public ResponseEntity<ApiResponse<RecipeDTO>> getRecipeById(@Valid @PathVariable UUID id) {
        logger.info("Fetching recipe with ID: {}", id);
        RecipeDTO recipe = recipeService.getRecipeById(id);
        ApiResponse<RecipeDTO> apiResponse = ApiResponse.<RecipeDTO>builder()
                .status(HttpStatus.OK)
                .localDateTime(LocalDateTime.now())
                .message("Get recipe successfully")
                .payload(recipe)
                .code(200)
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    @Operation(summary = "get recipes by name", description = "Retrieve a recipe by its name")
    @GetMapping("/recipes/name/")
    public ResponseEntity<ApiResponse<List<RecipeDTO>>> getRecipeByName(@Valid @Parameter(description = "name of recipe to be retrieved", example = "somlor khmer")
//                                                                  @Pattern(regexp = "^[a-zA-Z\\s]+$", message ="invalid input. Only text characters are allowed")
                                                                        @RequestParam String name
    ) {
        List<RecipeDTO> recipeDTO = recipeService.getRecipeByName(name);
        ApiResponse<List<RecipeDTO>> apiResponse = ApiResponse.<List<RecipeDTO>>builder()
                .status(HttpStatus.OK)
                .localDateTime(LocalDateTime.now())
                .message("Get Recipe Successfully")
                .payload(recipeDTO)
                .code(200)
                .build();
        return ResponseEntity.ok(apiResponse);

    }


    @Operation(summary = "get recipes by multiple ingredients", description = "retrieve recipes by multiple ingredients")
    @GetMapping("/recipes/multiIngredients")
    public ResponseEntity<?> getRecipeByIngredients(
            @Parameter(description = "name of ingredients to get recipes")
            @RequestParam List<String> ingredients
    ) {
        List<RecipeIngredientDTO> recipes = recipeService.getRecipeByIngredients(ingredients);
        ApiResponse<List<RecipeIngredientDTO>> apiResponse = ApiResponse.<List<RecipeIngredientDTO>>builder()
                .status(HttpStatus.OK)
                .localDateTime(LocalDateTime.now())
                .message("Get recipes by ingredients successfully")
                .payload(recipes)
                .code(200)
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a new recipe", description = "Create a new recipe")
    @PostMapping("/recipes")
    public ResponseEntity<ApiResponse<RecipeDTO>> createRecipe(@Valid @RequestBody RecipeRequest recipeRequest) {

        UUID userId = getUsernameOfCurrentUser();
        RecipeDTO recipeDTO = recipeService.addRecipe(recipeRequest, userId);

        ApiResponse<RecipeDTO> apiResponse = ApiResponse.<RecipeDTO>builder()
                .localDateTime(LocalDateTime.now())
                .message("Recipe successfully created")
                .payload(recipeDTO)
                .code(201)
                .status(HttpStatus.CREATED)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "update recipe", description = "updated recipe by id")
    @PutMapping("/recipes/{id}")
    public ResponseEntity<?> updatedRecipe(
            @Parameter(description = "id of food that have to be updated", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID id,
            @Valid @RequestBody RecipeRequest recipeRequest
    ) {

        UUID currentUserId = getCurrentUserId();
        Recipe recipeToUpdate = recipeService.updateRecipe(id, recipeRequest, currentUserId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("recipe id : " + id + "is updated successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(recipeToUpdate)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    private UUID getCurrentUserId() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getAppUser().getUserId();
    }


    UUID getUsernameOfCurrentUser() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser = userDetails.getAppUser();
        System.out.println("current user is : " + userDetails);
        logger.info("current user: {}", userDetails);
        return appUser.getUserId();
    }


    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete recipe ", description = "delete recipe by id")
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<?> deleteRecipe(
            @Parameter(description = "ID of the recipe to be deleted", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID id) {
        UUID currentUserId = getCurrentUserId();
        recipeService.deleteRecipe(id, currentUserId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("recipe is deleted successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @Operation(summary = "filter recipe by multi-tags", description = "retreive recipe by multi-tags")
    @GetMapping("/recipes/multi-tags")
    public ResponseEntity<ApiResponse<List<RecipeDTO>>> filterRecipeByMultiTags(
            @RequestParam  CookingLevelEnum cookingLevelEnum,
            @RequestParam  CookingTimeEnum cookingTimeEnum,
            @RequestParam IngredientsEnum ingredientsEnum,
            @RequestParam CuisineEnum cuisineEnum
    ) {
        List<RecipeDTO> recipeDTOS = recipeService.findRecipeByMultiTags(cookingLevelEnum, cookingTimeEnum, ingredientsEnum, cuisineEnum);
        ApiResponse<List<RecipeDTO>> apiResponse = ApiResponse.<List<RecipeDTO>>builder()
                .localDateTime(LocalDateTime.now())
                .message("Recipe has been find successfully")
                .payload(recipeDTOS)
                .code(201)
                .status(HttpStatus.OK)
                .build();

        return ResponseEntity.ok(apiResponse);
    }



    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "find recipe which is private", description = "retrieve recipe by private")
    @GetMapping("/users/recipes/private")
    public ResponseEntity<ApiResponse<List<RecipeDTO>>> getRecipeByPublicOrPrivate(

    ) {
        logger.info("Fetching recipes which is public or private");

        UUID currentUser = getCurrentUserId();
        List<RecipeDTO> recipes = recipeService.getRecipesByPrivateOrPublic(currentUser);
        ApiResponse<List<RecipeDTO>> apiResponse = ApiResponse.<List<RecipeDTO>>builder()
                .status(HttpStatus.OK)
                .payload(recipes)
                .localDateTime(LocalDateTime.now())
                .message("Get recipe successfully")
                .code(200)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get all recipes which is draft", description = "Retrieve all recipes which is draft")
    @GetMapping("/users/recipes/drafts")
    public ResponseEntity<ApiResponse<List<RecipeDTO>>> getDrafts() {
        logger.info("Fetching all drafts");
        UUID currentUser = getCurrentUserId();
        List<RecipeDTO> recipes = recipeService.getDrafts(currentUser);
        ApiResponse<List<RecipeDTO>> apiResponse = ApiResponse.<List<RecipeDTO>>builder()
                .status(HttpStatus.OK)
                .payload(recipes)
                .localDateTime(LocalDateTime.now())
                .message("Get all drafts successfully")
                .code(200)
                .build();
        return ResponseEntity.ok(apiResponse);

    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "get draft by recipe id", description = "retrive drafts")
    @GetMapping("/users/recipes/drafts/{recipeId}")
    public ResponseEntity<ApiResponse<RecipeDTO>> getDraftByRecipeId(@Valid @PathVariable UUID recipeId) {

        UUID currentUser = getCurrentUserId();
        RecipeDTO recipe = recipeService.getDraftByRecipeId(recipeId, currentUser);
        ApiResponse<RecipeDTO> apiResponse = ApiResponse.<RecipeDTO>builder()
                .status(HttpStatus.OK)
                .localDateTime(LocalDateTime.now())
                .message("Get drafts successfully")
                .payload(recipe)
                .code(200)
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Switch to public / private ", description = "update visibility of recipe")
    @PutMapping("/recipes/{recipeId}/visibility")
    public ResponseEntity<?> updateBookmarkedByRecipeId(
            @Parameter(description = "Id of recipe to update status", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID recipeId,
            @RequestParam boolean isPublic
    ) {
        recipeService.updateRecipeVisibility(recipeId, isPublic);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("status has change status successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

}
