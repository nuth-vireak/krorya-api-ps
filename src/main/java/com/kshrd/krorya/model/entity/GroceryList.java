package com.kshrd.krorya.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroceryList {
    private UUID groceryListId;
    private String groceryListTitle;
    private UUID userId;
    private List<Recipe> recipes;
    private int totalOfRecipes;
}
