package com.kshrd.krorya.model.dto;

import com.kshrd.krorya.model.entity.Cart;
import com.kshrd.krorya.model.entity.Food;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartFoodDTO {
    private UUID cartFoodId;
    private UUID cartId;
    private FoodDTO food;
    private Integer qty;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
