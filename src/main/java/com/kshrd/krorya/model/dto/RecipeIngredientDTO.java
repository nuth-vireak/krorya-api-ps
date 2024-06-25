package com.kshrd.krorya.model.dto;

import com.kshrd.krorya.model.entity.CookingSteps;
import com.kshrd.krorya.model.entity.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data

public class RecipeIngredientDTO {

    private UUID recipeId;
    private String recipeTitle;
    private String creatorName;
    private Boolean isPublic;
    private Boolean isBookmarked;
    private String cookingLevel;
    private Integer cookingTime;
    private String recipeImage;
    private List<Ingredient> ingredients;
}
