package com.kshrd.krorya.convert;

import com.kshrd.krorya.model.entity.Recipe;
import com.kshrd.krorya.model.dto.RecipeDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RecipeDTOConvertor {
    private ModelMapper modelMapper;

    public RecipeDTO convertRecipeToRecipeDTO(Recipe recipe) {
        return modelMapper.map(recipe, RecipeDTO.class);
    }

    public Recipe convertRecipeDTOToRecipe(RecipeDTO recipeDTO) {
        return modelMapper.map(recipeDTO, Recipe.class);
    }

    public List<RecipeDTO> convertRecipeListToRecipeDTOList(List<Recipe> recipeList) {
        return recipeList.stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDTO.class))
                .collect(Collectors.toList());
    }

    public List<Recipe> convertRecipeDTOListToRecipeList(List<RecipeDTO> recipeDTOList) {
        return recipeDTOList.stream()
                .map(recipeDTO -> modelMapper.map(recipeDTO, Recipe.class))
                .collect(Collectors.toList());
    }

    public RecipeDTO toDto(Recipe recipe) {
        return modelMapper.map(recipe, RecipeDTO.class);
    }

    public List<RecipeDTO> toListDTO(List<Recipe> recipes) {
        return recipes.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
