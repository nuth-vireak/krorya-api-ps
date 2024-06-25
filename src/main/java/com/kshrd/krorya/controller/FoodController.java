package com.kshrd.krorya.controller;

import com.kshrd.krorya.model.dto.FoodDTO;
import com.kshrd.krorya.model.dto.RecipeDTO;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.entity.Food;
import com.kshrd.krorya.model.request.FoodRequest;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.service.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@Tag(name = "Food Controller", description = "Endpoints for managing foods")
public class FoodController {

    private final FoodService foodService;

    UUID getUsernameOfCurrentUser() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser = userDetails.getAppUser();
        System.out.println("current user is : " + userDetails);
        return appUser.getUserId();
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get all foods", description = "Get a paginated list of all foods")
    @GetMapping("/foods")
    public ResponseEntity<ApiResponse<List<FoodDTO>>> getAllFoods(
            @Parameter(description = "Page number, must be positive", example = "1") @Positive @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size, must be positive", example = "5") @Positive @RequestParam(defaultValue = "5") int size,
            Principal principal
    ) {

        UUID currentUser = null;
        if (principal != null) {
            currentUser = getUsernameOfCurrentUser();
        }
        System.out.println("Current user id in FoodController : " + currentUser);

        List<FoodDTO> foods = foodService.getAllFoods(page, size, currentUser);
        return ResponseEntity.ok(
                ApiResponse.<List<FoodDTO>>builder()
                        .message("Get All Foods Successfully!")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(foods)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

/*    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "update bookmarked", description = "update bookmarked by food id")
    @PutMapping("/{foodId}/bookmark")
    public ResponseEntity<?> updateBookmarkedByFoodId(
            @Parameter(description = "Id of food to update bookmarked status", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID foodId,
            @RequestParam boolean bookmarked
    ){

        foodService.updateFoodBookmarked(foodId, bookmarked);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("bookmarked has change status successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }*/

//    @SecurityRequirement(name="bearerAuth")
//    @Operation(summary = "Get food by bookmarked", description = "Retrieve food(owner) by bookmarked")
//    @GetMapping("/bookmarked")
//    public ResponseEntity<ApiResponse<List<FoodDTO>>> getFoodByBookmarked() {
////    @SecurityRequirement(name="bearerAuth")
////    @Operation(summary = "update bookmarked", description = "update bookmarked by food id")
////    @PutMapping("/{foodId}/bookmark")
////    public ResponseEntity<?> updateBookmarkedByFoodId(
////            @Parameter(description = "Id of food to update bookmarked status", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
////            @PathVariable UUID foodId,
////            @RequestParam boolean bookmarked
////    ){
////
////        foodService.updateFoodBookmarked(foodId, bookmarked);
////        return ResponseEntity.ok(
////                ApiResponse.builder()
////                        .message("bookmarked has change status successfully")
////                        .status(HttpStatus.OK)
////                        .code(200)
////                        .localDateTime(LocalDateTime.now())
////                        .build()
////        );
////    }
//
//
//        UUID currentUser = getCurrentUserId();
//        List<FoodDTO> food = foodService.getFoodByBookmarked(currentUser);
//        ApiResponse<List<FoodDTO>> apiResponse = ApiResponse.<List<FoodDTO>>builder()
//                .status(HttpStatus.OK)
//                .payload(food)
//                .localDateTime(LocalDateTime.now())
//                .message("Get bookmarked of food successfully")
//                .code(200)
//                .build();
//
//        return ResponseEntity.ok(apiResponse);
//    }

    //get current user by user id
    private UUID getCurrentUserId() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getAppUser().getUserId();
    }


    @Operation(summary = "Get top rated foods")
    @GetMapping("/foods/top-rated-food")
    public ResponseEntity<ApiResponse<List<FoodDTO>>> getTopRatedFoods() {
        List<FoodDTO> foods = foodService.getTopRatedFoods();
        return ResponseEntity.ok(
                ApiResponse.<List<FoodDTO>>builder()
                        .message("Get All Foods Successfully!")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(foods)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @Operation(summary = "Get foods by user id")
    @GetMapping("/foods/user/{userId}")
    public ResponseEntity<ApiResponse<List<FoodDTO>>> getLatestFoodsByUserId(
            @Parameter(description = "ID of user to get all foods", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID userId) {
        List<FoodDTO> foods = foodService.getCurrentFoodsByUserId(userId);
        return ResponseEntity.ok(
                ApiResponse.<List<FoodDTO>>builder()
                        .message("Get foods by user id = " + userId + " successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(foods)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @Operation(summary = "Get all foods by category id", description = "Get a paginated list of all foods by category id")
    @GetMapping("/foods/categories/{categoryId}")
    public ResponseEntity<ApiResponse<List<FoodDTO>>> getAllFoodsByCategoryId(
            @Parameter(description = "Page number, must be positive", example = "1") @Positive @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size, must be positive", example = "5") @Positive @RequestParam(defaultValue = "5") int size,
            @Parameter(description = "ID of category to get all foods", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID categoryId) {

        List<FoodDTO> foods = foodService.getAllFoodsByCategoryId(page, size, categoryId);
        return ResponseEntity.ok(
                ApiResponse.<List<FoodDTO>>builder()
                        .message("Get All Foods Successfully!")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(foods)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @Operation(summary = "Get food by ID", description = "Get a specific food by its ID")
    @GetMapping("/foods/{foodId}")
    public ResponseEntity<ApiResponse<FoodDTO>> getFoodById(
            @Parameter(description = "ID of the food to be retrieved", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID foodId) {
        FoodDTO food = foodService.getFoodById(foodId);
        ApiResponse<FoodDTO> apiResponse = ApiResponse.<FoodDTO>builder()
                .message("Get Food id = " + foodId + " successfully!")
                .status(HttpStatus.OK)
                .code(200)
                .payload(food)
                .localDateTime(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Insert new food", description = "Insert a new food item")
    @PostMapping("/foods")
    public ResponseEntity<ApiResponse<FoodDTO>> insertFood(@Valid @RequestBody FoodRequest foodRequest) {
        UUID userId = getUsernameOfCurrentUser();
        FoodDTO food = foodService.insertFood(foodRequest, userId);
        return ResponseEntity.ok(
                ApiResponse.<FoodDTO>builder()
                        .message("Insert Food Successfully!")
                        .status(HttpStatus.CREATED)
                        .code(201)
                        .payload(food)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update food by food ID", description = "Update a specific food by its ID")
    @PutMapping("/foods/{foodId}")
    public ResponseEntity<ApiResponse<FoodDTO>> updateFoodByFoodId(
            @Parameter(description = "ID of the food to be updated", example = "d290f1ee-6c54-4b01-90e6-d701748f0851") @PathVariable("foodId") UUID id,
            @Valid @RequestBody FoodRequest foodRequest) {
        FoodDTO foodToUpdate = foodService.updateFoodById(id, foodRequest, getCurrentUserId());
        return ResponseEntity.ok(
                ApiResponse.<FoodDTO>builder()
                        .message("Food id = " + id + " is updated successfully!")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(foodToUpdate)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete food by ID", description = "Delete a specific food by its ID")
    @DeleteMapping("/foods/{id}")
    public ResponseEntity<ApiResponse<FoodDTO>> deleteFood(
            @Parameter(description = "ID of the food to be deleted", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID id) {
        FoodDTO foodToDelete = foodService.deleteFood(id, getCurrentUserId());
        return ResponseEntity.ok(
                ApiResponse.<FoodDTO>builder()
                        .message("Food id = " + id + " is deleted successfully!")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(foodToDelete)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }
}
