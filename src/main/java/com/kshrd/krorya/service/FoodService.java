package com.kshrd.krorya.service;

import com.kshrd.krorya.model.dto.FoodDTO;
import com.kshrd.krorya.model.request.FoodRequest;

import java.util.List;
import java.util.UUID;

public interface FoodService {

    List<FoodDTO> getAllFoods(int page, int size, UUID currentUser);
    List<FoodDTO> getTopRatedFoods();
//    List<FoodDTO> getLatestFoods();
    List<FoodDTO> getAllFoodsByCategoryId(int page, int size, UUID categoryId);
    FoodDTO getFoodById(UUID id);
    FoodDTO insertFood(FoodRequest foodRequest, UUID userId);
    FoodDTO updateFoodById(UUID id, FoodRequest foodRequest, UUID currentUserId);
    List<FoodDTO> getCurrentFoodsByUserId(UUID userId);


    FoodDTO updateFoodBookmarked(UUID foodId, boolean bookmarked, UUID currentUser);

    List<FoodDTO> getFoodByBookmarked(UUID currentUser);

    FoodDTO deleteFood(UUID id, UUID currentUserId);
}
