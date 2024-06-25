package com.kshrd.krorya.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CookingStepsDTO {
    private UUID cookingStepId;
    private UUID recipeId;
    private String stepNumber;
    private String image;
    private String description;
}
