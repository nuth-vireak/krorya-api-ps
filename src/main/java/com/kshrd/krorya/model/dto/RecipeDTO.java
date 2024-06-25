package com.kshrd.krorya.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kshrd.krorya.model.entity.CookingSteps;
import com.kshrd.krorya.model.entity.Cuisines;
import com.kshrd.krorya.model.entity.Ingredient;
import com.kshrd.krorya.model.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeDTO {
    private UUID recipeId;
    private String recipeTitle;
    private String description;
    private SimpleAppUserDTO creatorInfo;
    private Boolean isDraft;
    private Boolean isPublic;
    private Boolean isBookmarked;
    private Integer servingSizes;
    private String cookingLevel;
    private Integer cookingTime;
    private String cuisineName;
    private String recipeImage;
    private List<Ingredient> ingredients;
    private List<CookingSteps> cookingSteps;
    private List<String> tagName;


}
