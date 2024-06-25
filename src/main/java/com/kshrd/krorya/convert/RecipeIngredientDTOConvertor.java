package com.kshrd.krorya.convert;

import com.kshrd.krorya.model.dto.RecipeDTO;
import com.kshrd.krorya.model.dto.RecipeIngredientDTO;
import com.kshrd.krorya.model.entity.Recipe;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor

public class RecipeIngredientDTOConvertor {
    private ModelMapper modelMapper;

    public RecipeIngredientDTO convertRecipeIngredientDTO(Recipe recipe) {
        return modelMapper.map(recipe, RecipeIngredientDTO.class);
    }

    public Recipe convertRecipeIngredientDTOToRecipe(RecipeIngredientDTO recipeIngredientDTO) {
        return modelMapper.map(recipeIngredientDTO, Recipe.class);
    }

    public List<RecipeIngredientDTO> convertRecipeListToRecipeDTOList(List<Recipe> recipeList) {
        return recipeList.stream()
                .map(recipe -> modelMapper.map(recipe, RecipeIngredientDTO.class))
                .collect(Collectors.toList());
    }

    public List<Recipe> convertRecipeIngredientsDTOListToRecipeList(List<RecipeIngredientDTO> recipeIngredientDTOList) {
        return recipeIngredientDTOList.stream()
                .map(recipeIngredientDTO -> modelMapper.map(recipeIngredientDTOList, Recipe.class))
                .collect(Collectors.toList());
    }
}
