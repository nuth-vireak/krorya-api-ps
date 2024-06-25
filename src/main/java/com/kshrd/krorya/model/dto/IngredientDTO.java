package com.kshrd.krorya.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngredientDTO {
    private UUID ingredientId;
    private UUID userId;
    private String ingredientName;
    private String ingredientIcon;
    private String ingredientType;
}
