package com.kshrd.krorya.service.serviceImplementation;

import com.kshrd.krorya.convert.GroceryListDTOConvertor;
import com.kshrd.krorya.exception.CustomNotFoundException;
import com.kshrd.krorya.model.dto.GroceryListDTO;
import com.kshrd.krorya.model.entity.GroceryList;
import com.kshrd.krorya.model.entity.Ingredient;
import com.kshrd.krorya.model.entity.Recipe;
import com.kshrd.krorya.model.request.GroceryIngredientRequest;
import com.kshrd.krorya.model.request.GroceryListRequest;
import com.kshrd.krorya.repository.GroceryListRepository;
import com.kshrd.krorya.repository.RecipeRepository;
import com.kshrd.krorya.service.GroceryListService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GroceryListServiceImpl implements GroceryListService {

    private final static Logger log = LoggerFactory.getLogger(GroceryListServiceImpl.class);
    private final GroceryListDTOConvertor groceryListDTOConvertor;
    private final GroceryListRepository groceryListRepository;
    private final RecipeRepository recipeRepository;

    @Override
    public List<GroceryListDTO> getAllGroceryList(int page, int size, UUID currentUserId) {

        List<GroceryListDTO> groceryListDTOS = groceryListDTOConvertor.convertListOfGroceryListToGroceryListDTOList(groceryListRepository.getALLGroceryList(page, size, currentUserId));
        if (groceryListDTOS.isEmpty()) {
            throw new CustomNotFoundException("Your Grocery List is empty...");
        }
        return groceryListDTOS;
    }

    @Override
    public GroceryListDTO createGroceryList(GroceryListRequest groceryListRequest, UUID currentUserId) {
        return groceryListDTOConvertor.convertGroceryListToGroceryListDTO(groceryListRepository.createGroceryList(groceryListRequest, currentUserId));
    }

    @Override
    public GroceryListDTO updateGroceryList(UUID groceryId, GroceryListRequest groceryListRequest, UUID currentUserId) {

        GroceryList groceryList  = groceryListRepository.getGroceryListById(groceryId);
        if(groceryList ==  null){
            throw new CustomNotFoundException("grocery list id: "+ groceryId +" doesn't exit ");
        }
        return groceryListDTOConvertor.convertGroceryListToGroceryListDTO(groceryListRepository.updateGroceryList(groceryId, groceryListRequest, currentUserId));
    }

    @Override
    public void deleteGroceryList(UUID id, UUID currentUserId) {
        GroceryList groceryList = groceryListRepository.getGroceryListById(id);
        if(groceryList == null){
            throw new CustomNotFoundException("grocery list id: "+ id +" doesn't exit ");
        }
        groceryListRepository.deleteGroceryList(id, currentUserId);
    }

    @Override
    public GroceryListDTO getGroceryListById(UUID groceryId, UUID currentUserId) {
        GroceryList groceryList = groceryListRepository.checkUserOfGroceryList(groceryId, currentUserId);
        if (groceryList == null) {
            throw new CustomNotFoundException("You are not the owner of this grocery list");
        }

        if(groceryListRepository.getGroceryListById(groceryId) == null){
                    throw new CustomNotFoundException("grocery list id: "+ groceryId +" doesn't exit ");
                }
        return groceryListDTOConvertor.convertGroceryListToGroceryListDTO(groceryList);
    }

    @Override
    public GroceryListDTO addToGroceryList(UUID groceryId, UUID recipeId, UUID currentUserId) {

        GroceryList groceryList = groceryListRepository.checkUserOfGroceryList(groceryId, currentUserId);
        if (groceryList == null) {
            throw new CustomNotFoundException("You are not the owner of this grocery list");
        }

        // if grocery list id is not exist
          GroceryList  groceryListId = groceryListRepository.getGroceryListById(groceryId);
          if(groceryListId == null){
              throw new CustomNotFoundException("Grocery list id: "+ groceryId +" doesn't exit ");
          }

          // if the recipe doesn't exist
        Recipe recipe = recipeRepository.getRecipeById(recipeId);
        if(recipe == null){
            throw new CustomNotFoundException("Recipe id: "+ recipeId +" doesn't exit ");
        }

        // add recipe to grocery list
        GroceryList updatedGroceryList = groceryListRepository.addToGroceryList(groceryId, recipeId);

        // find all ingredients in the by recipe id
        recipe.getIngredients().forEach(ingredient -> {
            groceryListRepository.addIngredientToGroceryList(groceryId, ingredient.getIngredientId());
        });

        return  groceryListDTOConvertor.convertGroceryListToGroceryListDTO(updatedGroceryList);
    }

    @Override
    public void updateIngredientStatus(UUID ingredientId, UUID groceryId, boolean isBought, UUID currentUserId) {

        // check if ingredient is existing in the right grocery

        if(groceryListRepository.findIngredientInGroceryIngredient(groceryId, ingredientId) == null){
            throw new CustomNotFoundException("Ingredient id: "+ ingredientId +" doesn't exit in grocery id: "+ groceryId);
        }

        //check if grocery id is existing
        GroceryList groceryList = groceryListRepository.getGroceryListById(groceryId);
        if(groceryList== null){
            throw new CustomNotFoundException("Grocery id: "+ groceryId +" doesn't exit ");
        }

        //check current user of grocery list
        if(!groceryList.getUserId().equals(currentUserId)){
            throw new CustomNotFoundException("You are not the owner of this grocery list");
        }

        groceryListRepository.updateIngredientStatus(ingredientId, groceryId, isBought);
    }

//    @Override
//    public void updateIngredientStatus(UUID ingredientId, UUID groceryId, GroceryIngredientRequest groceryIngredientRequest, UUID currentUserId) {
//
//    }

//    @Transactional
//    public void updateIngredientStatus(UUID ingredientId, UUID groceryId, GroceryListRequest groceryIngredientRequest, UUID currentUserId) {
//        // Check if the ingredient exists in the specified grocery
//        Ingredient ingredient = groceryListRepository.findIngredientInRecipe(groceryId, ingredientId);
//        if (ingredient == null) {
//            throw new CustomNotFoundException("Ingredient ID: " + ingredientId + " doesn't exist in grocery ID: " + groceryId);
//        }
//
//        GroceryList groceryList = groceryListRepository.findGroceryListByGroceryId(groceryId);
//        if (groceryList == null) {
//            throw new CustomNotFoundException("Grocery ID: " + groceryId + " is not found.");
//        }
//
//        groceryListRepository.updateIngredientStatus(ingredientId, groceryId, groceryIngredientRequest);
//    }

}
