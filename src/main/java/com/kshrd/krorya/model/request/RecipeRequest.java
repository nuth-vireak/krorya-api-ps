package com.kshrd.krorya.model.request;

import com.kshrd.krorya.model.dto.CookingStepsDTO;
import com.kshrd.krorya.model.dto.IngredientDTO;
import com.kshrd.krorya.model.entity.CookingSteps;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRequest {

    @NotBlank(message = "Recipe title cannot blank !! ")
    private String recipeTitle;

    @NotBlank(message = "Description field cannot be blank !! ")
    private String description;

    @NotNull(message = "Draft status cannot be null !! ")
    private Boolean isDraft;

    @NotNull(message = "Visibility status can not be null !!")
    private Boolean isPublic;

    @NotNull(message = "Serving sizes cannot be null !! ")
    @Min(value = 1, message = "Serving sizes must be at least 1")
    private Integer servingSizes;

    @NotBlank(message = "cookingLevel cannot be blank")
    @Pattern(regexp = "^(ពិបាក|ងាយស្រួល|ធម្មតា)$", message = "cookingLevel must be ងាយស្រួល, ពិបាក or ធម្មតា")
    private String cookingLevel;

    @NotNull(message = "cookingTime cannot be null")
    @Min(value = 1, message = "cookingTime must be greater than zero")
    private Integer cookingTime;

    @NotNull(message = "cuisine id cannot be null ")
    private UUID cuisineId;

    @NotBlank(message = "Recipe image cannot be blank !!")
    private String recipeImage;


    private List<IngredientRecipeRequest> ingredientRecipeRequests;
    private List<CookingStepRequest> cookingStepRequests;
    private List<UUID> tagId;

}
