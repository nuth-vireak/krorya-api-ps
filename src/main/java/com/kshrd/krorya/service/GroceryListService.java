package com.kshrd.krorya.service;

import com.kshrd.krorya.model.dto.GroceryListDTO;
import com.kshrd.krorya.model.request.GroceryListRequest;

import java.util.List;
import java.util.UUID;

public interface GroceryListService {
    List<GroceryListDTO> getAllGroceryList(int page, int size,UUID currentUserId);

    GroceryListDTO createGroceryList(GroceryListRequest groceryListRequest, UUID currentUserId);

    GroceryListDTO updateGroceryList(UUID groceryId, GroceryListRequest groceryListRequest, UUID currentUserId);

    void deleteGroceryList(UUID id, UUID currentUserId);

    GroceryListDTO getGroceryListById(UUID groceryId, UUID currentUserId);

    GroceryListDTO addToGroceryList(UUID groceryId, UUID recipeId, UUID currentUserId);

    void updateIngredientStatus(UUID ingredientId, UUID groceryId, boolean isBought, UUID currentUserId);

}
