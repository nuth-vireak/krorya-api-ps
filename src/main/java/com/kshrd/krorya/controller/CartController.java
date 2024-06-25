package com.kshrd.krorya.controller;

import com.kshrd.krorya.model.dto.CartDTO;
import com.kshrd.krorya.model.dto.CartFoodDTO;
import com.kshrd.krorya.model.dto.CartWithFoodDTO;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.service.CartFoodService;
import com.kshrd.krorya.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Cart Controller", description = "Endpoint for managing carts")
public class CartController {
    private final CartService cartService;
    private final CartFoodService cartFoodService;

    @Operation(summary = "Get all carts", description = "Retrieve all carts")
    @GetMapping("/carts")
    public ResponseEntity<ApiResponse<List<CartWithFoodDTO>>> getAllCartByCurrentUserId(){

        List<CartWithFoodDTO> carts = cartService.getAllCartByCurrentUserId();
        return ResponseEntity.ok(
                ApiResponse.<List<CartWithFoodDTO>>builder()
                        .message("Get all carts by current user successfully!")
                        .status(HttpStatus.CREATED)
                        .code(201)
                        .payload(carts)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @Operation(summary = "Get cart by cart id", description = "Retrieve cart")
    @GetMapping("/carts/{cartId}")
    public ResponseEntity<ApiResponse<CartDTO>> getCartByCartId(
            @Parameter(description = "ID of cart", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID cartId
    ){
        CartDTO cart = cartService.getCartByCartId(cartId);
        return ResponseEntity.ok(
                ApiResponse.<CartDTO>builder()
                        .message("Get all carts by current user successfully!")
                        .status(HttpStatus.CREATED)
                        .code(201)
                        .payload(cart)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }


    private UUID getCurrentUserId() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getAppUser().getUserId();
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get Cart by Seller id", description = "Get carts by seller id")
    @GetMapping("/carts/seller/{sellerId}")
    public ResponseEntity<ApiResponse<List<CartWithFoodDTO>>> getAllCartBySellerId(
            @Parameter(description = "ID of sellerId", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID sellerId) {
        List<CartWithFoodDTO> cartDTOs = cartService.getAllCartBySellerId(sellerId);
        return ResponseEntity.ok(
                ApiResponse.<List<CartWithFoodDTO>>builder()
                        .message("Cart id = " + sellerId + " is retrieved successfully!")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(cartDTOs)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete Cart by id", description = "Delete a specific cart by its id")
    @DeleteMapping("/carts/{id}")
    public ResponseEntity<ApiResponse<CartDTO>> deleteCart(
            @Parameter(description = "ID of the cart to be deleted", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            @PathVariable UUID id) {
        CartDTO cartDTO = cartService.deleteCartByCartId(id);
        return ResponseEntity.ok(
                ApiResponse.<CartDTO>builder()
                        .message("Cart id = " + id + " is deleted successfully!")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(cartDTO)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }
}
