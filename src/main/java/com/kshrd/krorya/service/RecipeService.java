package com.kshrd.krorya.service;

import com.kshrd.krorya.model.dto.RecipeIngredientDTO;
import com.kshrd.krorya.model.entity.Recipe;
import com.kshrd.krorya.model.dto.RecipeDTO;
import com.kshrd.krorya.model.enumeration.CookingLevelEnum;
import com.kshrd.krorya.model.enumeration.CookingTimeEnum;
import com.kshrd.krorya.model.enumeration.CuisineEnum;
import com.kshrd.krorya.model.enumeration.IngredientsEnum;
import com.kshrd.krorya.model.request.RecipeRequest;

import java.util.List;
import java.util.UUID;

public interface RecipeService {
    List<RecipeDTO> getRecipes();

    RecipeDTO addRecipe(RecipeRequest recipeRequest, UUID userId);

    Recipe updateRecipe(UUID id, RecipeRequest recipeRequest, UUID currentUserID);

    void deleteRecipe(UUID id, UUID userId);

    RecipeDTO getRecipeById(UUID id);


    List<RecipeDTO> getRecipeByName(String name);

    List<RecipeDTO> getRecipesByRandom();

    List<RecipeDTO> getRecipeByUserId(UUID userId);

    List<RecipeDTO> getRecipesByBookmarked(UUID currentUser);

    List<RecipeIngredientDTO> getRecipeByIngredients(List<String> ingredients);

    List<RecipeDTO> getRecipeByBookmarked(UUID currentUser);

    RecipeDTO updateRecipeBookmarked(UUID recipeId, boolean bookmarked, UUID currentUser);

    List<RecipeDTO> findRecipeByCookingLevel(CookingLevelEnum cookingLevelEnum);

    List<RecipeDTO> findRecipeByCuisine(CuisineEnum cuisineEnum);

    List<RecipeDTO> findRecipeByCookingTime(CookingTimeEnum cookingTimeEnum);

    List<RecipeDTO> getRecipesByPrivateOrPublic(UUID userId);

    List<RecipeDTO> findRecipeByNumberOfIngredients(IngredientsEnum ingredientsEnum);

    List<RecipeDTO> getDrafts(UUID currentUser);

    RecipeDTO getDraftByRecipeId(UUID recipeId, UUID currentUser);

    List<RecipeDTO> getRecipeByCurrentUserId(UUID currentUser);

    RecipeDTO updateRecipeVisibility(UUID recipeId, boolean isPublic);

    List<RecipeDTO> findRecipeByMultiTags(CookingLevelEnum cookingLevelEnum, CookingTimeEnum cookingTimeEnum, IngredientsEnum ingredientsEnum, CuisineEnum cuisineEnum);
}
