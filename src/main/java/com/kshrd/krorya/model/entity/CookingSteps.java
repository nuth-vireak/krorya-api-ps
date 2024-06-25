package com.kshrd.krorya.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CookingSteps {
    private UUID cookingStepId;
    private String stepNumber;
    private String image;
    private String description;
}
