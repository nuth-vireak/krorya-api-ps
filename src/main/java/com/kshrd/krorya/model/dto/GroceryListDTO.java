package com.kshrd.krorya.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.Recipe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroceryListDTO {
    private UUID groceryListId;
    private String groceryListTitle;
    private int totalOfRecipes;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Recipe> recipes;

}


