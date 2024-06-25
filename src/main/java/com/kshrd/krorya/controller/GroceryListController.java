package com.kshrd.krorya.controller;


import com.kshrd.krorya.model.dto.GroceryListDTO;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.request.GroceryListRequest;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.service.GroceryListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@RestController
@AllArgsConstructor
@RequestMapping("api/v1")
@Tag(name = "GroceryList Controller", description = "endpoints for managing grocery list")
public class GroceryListController {
    private static final Logger logger = LoggerFactory.getLogger(GroceryListController.class);
    private final GroceryListService groceryListService;

    //get current user by user id
    private UUID getCurrentUserId() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getAppUser().getUserId();
    }


    // get current user by username
    UUID getUsernameOfCurrentUser() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser = userDetails.getAppUser();
        System.out.println("current user is : " + userDetails);
        logger.info("current user: {}", userDetails);
        return appUser.getUserId();
    }


    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "get grocery list by id", description = " Retrieve grocery list by id ")
    @GetMapping("/groceryList/{id}")
    public ResponseEntity<ApiResponse<GroceryListDTO>> getGroceryListById(
            @Parameter(description = "id of grocery list ", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
           @PathVariable UUID groceryId
    ){
        logger.info("fetching all grocery list data");

        UUID currentUserId = getCurrentUserId();
        GroceryListDTO groceryListDTOS = groceryListService.getGroceryListById(groceryId, currentUserId);
        ApiResponse<GroceryListDTO>apiResponse = ApiResponse.<GroceryListDTO>builder()
                .status(HttpStatus.OK)
                .payload(groceryListDTOS)
                .localDateTime(LocalDateTime.now())
                .message("Get all grocery lists successfully")
                .code(200)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "get all grocery list ", description = " Retrieve all grocery")
    @GetMapping("/groceryList")
    public ResponseEntity<ApiResponse<List<GroceryListDTO>>> getAllGroceryList(
            @Parameter(description = "Page number, must be positive", example = "1") @Positive @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size, must be positive", example = "5") @Positive @RequestParam(defaultValue = "5") int size
    ){
        logger.info("fetching all grocery list data");

        UUID currentUserId = getCurrentUserId();
        List<GroceryListDTO> groceryListDTOS = groceryListService.getAllGroceryList(page, size , currentUserId);
        ApiResponse<List<GroceryListDTO>> apiResponse = ApiResponse.<List<GroceryListDTO>>builder()
                .status(HttpStatus.OK)
                .payload(groceryListDTOS)
                .localDateTime(LocalDateTime.now())
                .code(200)
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "post grocery list ", description = " create grocery list")
    @PostMapping("/groceryList")
    public ResponseEntity<ApiResponse<GroceryListDTO>> createGroceryList(
            @RequestBody GroceryListRequest groceryListRequest
    ){
        logger.info("creating grocery list data");
        UUID currentUserId = getCurrentUserId();
        GroceryListDTO groceryListDTOS = groceryListService.createGroceryList(groceryListRequest, currentUserId);
        ApiResponse<GroceryListDTO> apiResponse = ApiResponse.<GroceryListDTO>builder()
                .status(HttpStatus.CREATED)
                .payload(groceryListDTOS)
                .localDateTime(LocalDateTime.now())
                .message("Create grocery list successfully")
                .code(201)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "add to grocery list ", description = " add to grocery list")
    @PostMapping("/grocery-lists/grocery/{groceryId}/recipes/{recipeId}")
    public ResponseEntity<ApiResponse<GroceryListDTO>> addToGroceryList(
            @Parameter(description = "id of grocery list ", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID groceryId,
            @Parameter(description = "id of recipe that have to be added", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID recipeId
    ){
        logger.info("add recipe to grocery list ");
        UUID currentUserId = getCurrentUserId();
        groceryListService.addToGroceryList(groceryId, recipeId, currentUserId);
        ApiResponse<GroceryListDTO> apiResponse = ApiResponse.<GroceryListDTO>builder()
                .status(HttpStatus.OK)
                .localDateTime(LocalDateTime.now())
                .message(" recipe has add to grocery list successfully")
                .code(201)
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "update grocery list", description = "updated grocery  list by id")
    @PutMapping("/groceryList/{groceryId}")
    public ResponseEntity<?> updatedGroceryList(
            @Parameter(description = "id of grocery list that have to be updated", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID groceryId,
            @Valid @RequestBody GroceryListRequest groceryListRequest
    ) {

        UUID currentUserId = getCurrentUserId();
        GroceryListDTO  groceryList = groceryListService.updateGroceryList(groceryId, groceryListRequest, currentUserId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("recipe id : " + groceryId + "is updated successfully")
                        .payload(groceryList)
                        .status(HttpStatus.OK)
                        .code(200)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "delete grocery list", description = "delete grocery  list by id")
    @DeleteMapping("/groceryList/{id}")
    public ResponseEntity<?> deleteGroceryList(
            @Parameter(description = "id of grocery list that have to be deleted", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID id
    ) {
        UUID currentUserId = getCurrentUserId();
        groceryListService.deleteGroceryList(id, currentUserId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("grocery list id : " + id + " is deleted successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }


    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Change ingredient status", description = "Change ingredient status to bought")
    @PutMapping("/groceryList/ingredients/isBought/{groceryId}/{ingredientId}")
    public ResponseEntity<?> updateIngredientStatus(
            @Parameter(description = "ID of the grocery  list ", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID groceryId,
            @Parameter(description = "ID of the ingredients to be bought ", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID ingredientId,
            boolean isBought

    ) {
        UUID currentUserId = getCurrentUserId();
        groceryListService.updateIngredientStatus(ingredientId, groceryId, isBought, currentUserId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("Ingredient ID: " + ingredientId + " is bought successfully")
                        .status(HttpStatus.OK)
                        .code(200)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

}
