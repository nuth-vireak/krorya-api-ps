package com.kshrd.krorya.service.serviceImplementation;

import com.kshrd.krorya.convert.RecipeDTOConvertor;
import com.kshrd.krorya.convert.RecipeIngredientDTOConvertor;
import com.kshrd.krorya.exception.CustomBadRequestException;
import com.kshrd.krorya.exception.CustomNotFoundException;
import com.kshrd.krorya.exception.ForbiddenException;
import com.kshrd.krorya.model.dto.RecipeIngredientDTO;
import com.kshrd.krorya.model.entity.Cuisines;
import com.kshrd.krorya.model.entity.Recipe;
import com.kshrd.krorya.model.dto.RecipeDTO;
import com.kshrd.krorya.model.enumeration.CookingLevelEnum;
import com.kshrd.krorya.model.enumeration.CookingTimeEnum;
import com.kshrd.krorya.model.enumeration.CuisineEnum;
import com.kshrd.krorya.model.enumeration.IngredientsEnum;
import com.kshrd.krorya.model.request.CookingStepRequest;
import com.kshrd.krorya.model.request.IngredientRecipeRequest;
import com.kshrd.krorya.model.request.RecipeRequest;
import com.kshrd.krorya.repository.*;
import com.kshrd.krorya.service.RecipeService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private static final Logger log = LoggerFactory.getLogger(RecipeServiceImpl.class);
    private final RecipeDTOConvertor recipeDTOConvertor;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final CookingStepRepository cookingStepRepository;
    private final RecipeIngredientDTOConvertor recipeIngredientDTOConvertor;
    private final TagRepository tagRepository;
    private final CuisineRepository cuisineRepository;

    @Override
    public List<RecipeDTO> getRecipes() {
        List<RecipeDTO> recipeDTOList = recipeDTOConvertor.convertRecipeListToRecipeDTOList(recipeRepository.getRecipes());
        if (recipeDTOList == null) {
            throw new CustomNotFoundException("Recipe is empty!!!!");
        }
        return recipeDTOList;
    }


    @Override
    @Transactional
    public RecipeDTO addRecipe(RecipeRequest recipeRequest, UUID userId) {

        // check if cuisine is empty

        Cuisines cuisineById = cuisineRepository.getCuisineById(recipeRequest.getCuisineId());
        if(cuisineById==null){
            throw new CustomNotFoundException("cuisine id doesn't exist");
        }


        // check if the ingredients exist
        List<IngredientRecipeRequest> ingredients = recipeRequest.getIngredientRecipeRequests();
        for(IngredientRecipeRequest ingredient : ingredients){
            if(ingredientRepository.getIngredientByid(ingredient.getIngredientId())==null){
                throw new CustomNotFoundException("ingredient id : "+ ingredient.getIngredientId() + " doesn't exist");
            }
        }

        //check if tag id is exist
        List<UUID> tagIds = recipeRequest.getTagId();
        for (UUID tagId : tagIds) {
            if (tagRepository.getTagById(tagId) == null) {
                throw new CustomNotFoundException("Tag id : " + tagId + " doesn't exist");
            }
        }


        UUID recipeId = recipeRepository.addRecipe(recipeRequest, userId);
        addRecipeIngredients(recipeId, recipeRequest.getIngredientRecipeRequests());
        addRecipeTags(recipeId, recipeRequest.getTagId());
        addCookingStep(recipeId, recipeRequest.getCookingStepRequests());
        return recipeDTOConvertor.convertRecipeToRecipeDTO(recipeRepository.getRecipeById(recipeId));
    }


    private void addCookingStep(UUID recipeId, List<CookingStepRequest> cookingStepRequests) {
        if (cookingStepRequests != null) {
            for (CookingStepRequest cookingStepRequest : cookingStepRequests) {
                cookingStepRepository.saveCookingStepRequest(recipeId, cookingStepRequest);
            }
        }
    }

    private void addRecipeIngredients(UUID recipeId, List<IngredientRecipeRequest> ingredients) {
        if (ingredients != null) {
            for(IngredientRecipeRequest ingredient : ingredients){
                UUID ingredientId = ingredient.getIngredientId();
                if (ingredientId.equals(ingredientRepository.getIngredientIdByRecipeIdAndIngredientId(recipeId, ingredientId))) {
                    recipeRepository.deleteRecipeById(recipeId);
                    throw new CustomBadRequestException("Ingredient id : " + ingredientId + " already exists in the recipe");
                }
                if (ingredientRepository.getIngredientByid(ingredientId) == null) {
                    recipeRepository.deleteRecipeById(recipeId);
                    throw new CustomBadRequestException("Ingredient id : " + ingredientId + " doesn't exist.");
                }
                recipeRepository.addRecipeIngredient(recipeId, ingredientId, ingredient.getQuantity());
            }
        }
    }

    private void addRecipeTags(UUID recipeId, List<UUID> tagIds) {
        if (tagIds != null) {
            for (UUID tagId : tagIds) {
                if (tagId.equals(tagRepository.getTagIdByRecipeIdAndTagId(recipeId, tagId))) {
                    recipeRepository.deleteRecipeById(recipeId);
                    throw new CustomBadRequestException("Tag id : " + tagId + " already exists in the recipe");
                }
                if (tagRepository.getTagById(tagId) == null){
                    recipeRepository.deleteRecipeById(recipeId);
                    throw new CustomBadRequestException("Tag id : " + tagId + " doesn't exist.");
                }
                recipeRepository.addRecipeTag(recipeId, tagId);
            }
        }
    }
    @Transactional
    @Override
    public Recipe updateRecipe(UUID id, RecipeRequest recipeRequest, UUID currentUserID) {
        Recipe recipeToUpdate = recipeRepository.getRecipeById(id);
        if(recipeToUpdate == null){
            throw new CustomNotFoundException("Recipe id : "+id+" not found");
        }

        // Check if the current user is the owner of the recipe
        UUID recipeOwnerId = recipeRepository.getRecipeOwnerId(id);
        log.info("Recipe Owner ID: {}", recipeOwnerId);
        log.info("Current User ID: {}", currentUserID);
        if (recipeOwnerId == null || !recipeOwnerId.equals(currentUserID)) {
            throw new ForbiddenException("Current user is not the owner of the recipe and cannot update it");
        }

        recipeToUpdate = recipeRepository.updateRecipe(id, recipeRequest);
        ingredientRepository.deleteRecipeIngredientsByRecipeId(id);
        addRecipeIngredients(id, recipeRequest.getIngredientRecipeRequests());
        tagRepository.deleteRecipeTagsByRecipeId(id);
        addRecipeTags(id, recipeRequest.getTagId());
        cookingStepRepository.deleteCookingStepByRecipeId(id);
        addCookingStep(id, recipeRequest.getCookingStepRequests());
        return recipeToUpdate;
    }

    @Override
    public void deleteRecipe(UUID id, UUID userId) {

        // Check if the current user is the owner of the recipe
        UUID recipeOwnerId = recipeRepository.getRecipeOwnerId(id);
        if (recipeOwnerId == null || !recipeOwnerId.equals(userId)) {
            throw new ForbiddenException("Current user is not the owner of the recipe and cannot delete it");
        }

        recipeRepository.deleteRecipeById(id);
    }

    @Override
    public RecipeDTO getRecipeById(UUID id) {
        Recipe recipeToFind = recipeRepository.getRecipeById(id);
        log.info("recipeToFind {}", recipeToFind);
        if(recipeToFind == null){
            throw new CustomNotFoundException("Recipe with id : "+ id + " doesn't exist");
        }
        return recipeDTOConvertor.convertRecipeToRecipeDTO(recipeRepository.getRecipeById(id));
    }


    @Override
    public List<RecipeDTO> getRecipeByName(String name) {
        List<Recipe> recipes = recipeRepository.getRecipeByName(name);
        if(recipes == null){
            throw new CustomNotFoundException("Recipe with name "+name+" doesn't exists");
        }
        return recipeDTOConvertor.convertRecipeListToRecipeDTOList(recipes);
    }

    @Override
    public List<RecipeDTO> getRecipesByRandom() {
        return recipeDTOConvertor.convertRecipeListToRecipeDTOList(recipeRepository.getRecipesByRandom());
    }

    @Override
    public List<RecipeDTO> getRecipeByUserId(UUID userId) {

        List<Recipe> recipes = recipeRepository.findRecipesByUserId(userId);
        if (recipes.isEmpty()) {
            throw new CustomNotFoundException("No recipes found for user ID " + userId);
        }
        return recipes.stream()
                .map(recipeDTOConvertor::convertRecipeToRecipeDTO)//convert recipe to recipeDTO
                .collect(Collectors.toList()); //gather element to new lists
    }

    @Override
    public List<RecipeDTO> getRecipesByBookmarked(UUID currentUser) {
        System.out.println("currentUser: " + currentUser);
        List<Recipe> bookmarkedRecipes = recipeRepository.findRecipeByBookmarked(currentUser);
        log.info("bookmarkedRecipes {}", bookmarkedRecipes);
        if(bookmarkedRecipes.isEmpty()){
            throw new CustomNotFoundException("Your bookmarked list is empty");
        }
        log.info(recipeDTOConvertor.toListDTO(bookmarkedRecipes).toString());
        return  recipeDTOConvertor.toListDTO(bookmarkedRecipes);
//        return recipeDTOConvertor.convertRecipeListToRecipeDTOList(recipeRepository.getRecipeByBookmarked(currentUser));
    }

    @Override
    public List<RecipeIngredientDTO > getRecipeByIngredients(List<String> ingredients) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ingredients", ingredients);

        List<Recipe> recipes = recipeRepository.getRecipeByIngredients(parameters);

        // if get recipe by ingredients is empty
        if(recipes.isEmpty()){
            throw new CustomNotFoundException("Recipe with ingredients "+ingredients+" doesn't exist");
        }

        return recipeIngredientDTOConvertor.convertRecipeListToRecipeDTOList(recipes);
    }

    @Override
    public List<RecipeDTO> findRecipeByCookingLevel(CookingLevelEnum cookingLevelEnum) {
        List<Recipe> recipes = recipeRepository.getRecipeByCookingLevel(cookingLevelEnum.name());
        return recipes.stream()
                .map(recipeDTOConvertor::convertRecipeToRecipeDTO)//convert recipe to recipeDTO
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> findRecipeByCuisine(CuisineEnum cuisineEnum) {
        List<Recipe> recipes = recipeRepository.getRecipeByCuisine(cuisineEnum.name());

        return recipes.stream()
                .map(recipeDTOConvertor::convertRecipeToRecipeDTO)//convert recipe to recipeDTO
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> findRecipeByCookingTime(CookingTimeEnum cookingTimeEnum) {
        List<Recipe> recipes = recipeRepository.getRecipeByCookingTime(CookingTimeEnum.valueOf(cookingTimeEnum.name()));
        return recipes.stream()
                .map(recipeDTOConvertor::convertRecipeToRecipeDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> getRecipesByPrivateOrPublic( UUID currentUser) {
        List<Recipe> visibility = recipeRepository.findRecipeByPublicOrPrivate(currentUser);
        if(visibility.isEmpty()){
            throw new CustomNotFoundException("Your list doesn't have recipe");
        }
        return recipeDTOConvertor.convertRecipeListToRecipeDTOList(recipeRepository.findRecipeByPublicOrPrivate(currentUser)) ;
    }

    @Override
    public List<RecipeDTO> findRecipeByNumberOfIngredients(IngredientsEnum ingredientsEnum) {
        List<Recipe> recipes = recipeRepository.getRecipeByNumberOfIngredients(ingredientsEnum.valueOf(ingredientsEnum.name()));
        return recipes.stream()
                .map(recipeDTOConvertor::convertRecipeToRecipeDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> findRecipeByMultiTags(CookingLevelEnum cookingLevelEnum, CookingTimeEnum cookingTimeEnum, IngredientsEnum ingredientsEnum, CuisineEnum cuisineEnum) {
        List<Recipe> recipes = recipeRepository.getRecipeByMultiTags(cookingLevelEnum, cookingTimeEnum, ingredientsEnum, cuisineEnum.name());
        return recipes.stream()
                .map(recipeDTOConvertor::convertRecipeToRecipeDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> getDrafts(UUID currentUser) {
        List<Recipe> drafts = recipeRepository.getAllDrafts(currentUser);
        if(drafts.isEmpty()){
            throw new CustomNotFoundException("your drafts is empty !!! ");
        }
        return recipeDTOConvertor.convertRecipeListToRecipeDTOList(recipeRepository.getAllDrafts(currentUser));
    }


    @Override
    public RecipeDTO getDraftByRecipeId(UUID recipeId, UUID currentUser) {
        Recipe drafts = recipeRepository.findDraftsByRecipeId(recipeId, currentUser);
        if(drafts == null){
            throw new CustomNotFoundException("drafts is not found for the given ID");
        }
        return recipeDTOConvertor.convertRecipeToRecipeDTO(recipeRepository.findDraftsByRecipeId(recipeId, currentUser));
    }

    @Override
    public List<RecipeDTO> getRecipeByCurrentUserId(UUID currentUser) {

        List<Recipe> recipes = recipeRepository.getRecipeByCurrentUserId(currentUser);

        if (recipes.isEmpty()) {
            throw new CustomNotFoundException("No recipes found for user ID " + currentUser);
        }

        return recipes.stream()
                .map(recipeDTOConvertor::convertRecipeToRecipeDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDTO updateRecipeVisibility(UUID recipeId, boolean isPublic) {
        Recipe recipe = recipeRepository.getRecipeById(recipeId);
        if(recipe == null){
            throw new CustomNotFoundException("recipe with id : "+recipeId+" doesn't exist");
        }
        return recipeDTOConvertor.convertRecipeToRecipeDTO(recipeRepository.updateRecipeVisibility(recipeId, isPublic));
    }



    @Override
    public List<RecipeDTO> getRecipeByBookmarked(UUID currentUser) {
        List<Recipe> recipes = recipeRepository.findRecipeByBookmarked(currentUser);
        if (recipes.isEmpty()) {
            throw new IllegalArgumentException("No bookmarked recipes found for the user.");
        }
        return recipeDTOConvertor.convertRecipeListToRecipeDTOList(recipes);
    }

    @Override
    public RecipeDTO updateRecipeBookmarked(UUID recipeId, boolean bookmarked, UUID currentUser) {
        boolean isBookmarked = recipeRepository.isBookmarked(currentUser, recipeId);

        if (bookmarked && isBookmarked) {
            throw new IllegalArgumentException("Recipe is already bookmarked by the user.");
        } else if (!bookmarked && !isBookmarked) {
            throw new IllegalArgumentException("Recipe is not bookmarked by the user.");
        }

        if (bookmarked) {
            recipeRepository.addBookmark(recipeId, currentUser);
        } else {
            recipeRepository.removeBookmark(recipeId, currentUser);
        }

//        Recipe updatedRecipe = recipeRepository.findRecipeById(recipeId);
        Recipe updatedRecipe = recipeRepository.findRecipeById(recipeId);
        recipeRepository.updateRecipeBookmarked(recipeId, bookmarked);
        return recipeDTOConvertor.convertRecipeToRecipeDTO(updatedRecipe);
    }

}
