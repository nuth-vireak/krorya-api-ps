package com.kshrd.krorya.controller;

import com.kshrd.krorya.model.dto.CartFoodDTO;
import com.kshrd.krorya.model.dto.FoodDTO;
import com.kshrd.krorya.model.entity.Address;
import com.kshrd.krorya.model.request.AddressRequest;
import com.kshrd.krorya.model.request.CartFoodRequest;
import com.kshrd.krorya.model.request.OrderRequest;
import com.kshrd.krorya.model.request.ReportOtherUserRequest;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.service.CartFoodService;
import com.kshrd.krorya.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "CartFood Controller", description = "Endpoint for managing cartFoods")
public class CartFoodController {
    private final CartFoodService cartFoodService;

    @Operation(summary = "Insert food")
    @PostMapping("/cartFoods/{foodId}")
    public ResponseEntity<ApiResponse<CartFoodDTO>> insertCartFood(@Parameter(description = "ID of the food to be add", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
                                                                       @PathVariable UUID foodId){

        CartFoodDTO cart = cartFoodService.insertCartFood(foodId);
        return ResponseEntity.ok(
                ApiResponse.<CartFoodDTO>builder()
                        .message("Food added to cart successfully!")
                        .status(HttpStatus.CREATED)
                        .code(201)
                        .payload(cart)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @Operation(summary = "Get all cart food by cart id")
    @GetMapping("/cartFoods/{cartId}")
    public ResponseEntity<ApiResponse<List<CartFoodDTO>>> getAllFoodsByCategoryId(
            @Parameter(description = "ID of cart to get all foods", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID cartId) {

        List<CartFoodDTO> cartFoods = cartFoodService.getAllCartFoodByCartId(cartId);
        return ResponseEntity.ok(
                ApiResponse.<List<CartFoodDTO>>builder()
                        .message("Get All Cart Foods Successfully!")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(cartFoods)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete carts by cartFoodId", description = "Delete a specific food by its CartFoodId from cart")
    @DeleteMapping("/cartFoods/{id}")
    public ResponseEntity<ApiResponse<CartFoodDTO>> deleteFood(
            @Parameter(description = "ID of the cartFood to be deleted", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID id) {
        cartFoodService.deleteFoodFromCart(id);
        return ResponseEntity.ok(
                ApiResponse.<CartFoodDTO>builder()
                        .message("Food id = " + id + " is deleted successfully!")
                        .status(HttpStatus.OK)
                        .code(200)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @Operation(summary = "Update food qty")
    @PutMapping("/cartFoods/{cartFoodId}")
    public ResponseEntity<ApiResponse<CartFoodDTO>> updateAddressAddressId(
            @Parameter(description = "Cart Food ID to update qty", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID cartFoodId,
            @Valid @RequestBody CartFoodRequest cartFoodRequest){
        CartFoodDTO cartFoodDTO = cartFoodService.updateFoodQtyByCartFoodId(cartFoodId, cartFoodRequest);
        return ResponseEntity.ok(
                ApiResponse.<CartFoodDTO>builder()
                        .message("Qty of Cart Food with ID " + cartFoodId + " is updated successfully!")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(cartFoodDTO)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }
}
