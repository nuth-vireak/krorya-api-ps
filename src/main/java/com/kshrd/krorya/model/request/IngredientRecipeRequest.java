package com.kshrd.krorya.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IngredientRecipeRequest {
    private UUID ingredientId;
    private String quantity;
}
