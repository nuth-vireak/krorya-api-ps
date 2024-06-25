package com.kshrd.krorya.model.entity;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroceryIngredient {

    private UUID groceryId;

    private UUID ingredientId;

    private boolean isBought;
}
