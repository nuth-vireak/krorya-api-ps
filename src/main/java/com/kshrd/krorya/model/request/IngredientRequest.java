package com.kshrd.krorya.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class IngredientRequest {
    @NotNull(message = "Ingredient name should not be null")
    @NotBlank(message = "Ingredient name should not be blank")
    private String ingredientName;

//    @NotNull(message = "Ingredient icon should not be null")
//    private String ingredientIcon;
//
//    @NotNull(message = "Ingredient type should not be null")
//    @NotBlank(message = "Ingredient type should not be blank")
//    private String ingredientType;
}