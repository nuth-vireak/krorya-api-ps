package com.kshrd.krorya.service.serviceImplementation;

import com.kshrd.krorya.convert.FoodDTOConvertor;
import com.kshrd.krorya.exception.CustomNotFoundException;
import com.kshrd.krorya.exception.ForbiddenException;
import com.kshrd.krorya.exception.SearchNotFoundException;
import com.kshrd.krorya.model.dto.FoodDTO;
import com.kshrd.krorya.model.entity.Food;
import com.kshrd.krorya.model.request.FoodRequest;
import com.kshrd.krorya.repository.AppUserRepository;
import com.kshrd.krorya.repository.CategoryRepository;
import com.kshrd.krorya.repository.FoodRepository;
import com.kshrd.krorya.service.FoodService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FoodServiceImpl implements FoodService {
    private static final Logger log = LoggerFactory.getLogger(FoodServiceImpl.class);
    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;
    private final AppUserRepository appUserRepository;
    private final FoodDTOConvertor foodDTOConvertor;

    @Override
    public List<FoodDTO> getAllFoods(int page, int size, UUID currentUser) {
        List<Food> foods;
        if (currentUser == null) {
            foods = foodRepository.getAllFoodsWithoutBookmark(page, size);
        } else {
            foods = foodRepository.getAllFoods(page, size, currentUser);
        }
        List<FoodDTO> foodList = foodDTOConvertor.toListDTO(foods);

        if (foodList.isEmpty()) {
            throw new CustomNotFoundException("Food list is empty!");
        }

        return foodList;
    }

    @Override
    public List<FoodDTO> getTopRatedFoods() {
        return foodRepository.getTopRatedFoods();
    }

    @Override
    public List<FoodDTO> getAllFoodsByCategoryId(int page, int size, UUID categoryId) {
        if (categoryRepository.getCategoryById(categoryId) == null) {
            throw new SearchNotFoundException("Category with ID " + categoryId + " does not exist!");
        }
        List<FoodDTO> foodList = foodDTOConvertor.toListDTO(foodRepository.getFoodsByCategoryId(page, size, categoryId));
        if (foodList.isEmpty()) {
            throw new CustomNotFoundException("Food list is empty!");
        }
        return foodList;
    }

    @Override
    public List<FoodDTO> getCurrentFoodsByUserId(UUID userId) {
        if (appUserRepository.findUsernameByUserid((userId)) == null) {
            throw new CustomNotFoundException("This user does not exist!");
        }
        List<FoodDTO> foodList = foodDTOConvertor.toListDTO(foodRepository.getLatestFoodsByUserId(userId));
        if (foodList.isEmpty()) {
            throw new CustomNotFoundException("Food list is empty!");
        }
        return foodList;
    }

    @Override
    public FoodDTO updateFoodBookmarked(UUID foodId, boolean bookmarked, UUID currentUser) {

        boolean isBookmarked = foodRepository.isBookmarked(currentUser, foodId);

        if (bookmarked && isBookmarked) {
            throw new IllegalArgumentException("Food is already bookmarked by the user.");
        } else if (!bookmarked && !isBookmarked) {
            throw new IllegalArgumentException("Food is not bookmarked by the user.");
        }


        if (bookmarked) {
            foodRepository.addBookmark(foodId, currentUser);
        } else {
            foodRepository.removeBookmark(foodId, currentUser);
        }

        Food UpdatedFood = foodRepository.updateFoodBookmarked(foodId, bookmarked);
        return foodDTOConvertor.toDto(UpdatedFood);
    }

    @Override
    public List<FoodDTO> getFoodByBookmarked(UUID currentUser) {
        List<Food> foods = foodRepository.findFoodByBookmarked(currentUser);
        if (foods.isEmpty()) {
            throw new IllegalArgumentException("No bookmarked foods found for the user.");
        }
        return foodDTOConvertor.toListDTO(foods);
    }
    @Override
    public FoodDTO getFoodById(UUID id) {
        Food food = foodRepository.getFoodById(id);
        if (food == null) {
            throw new SearchNotFoundException("Food with ID " + id + " does not exist");
        }
        return foodDTOConvertor.toDto(food);
    }

    @Override
    public FoodDTO insertFood(FoodRequest foodRequest, UUID userId) {
        UUID categoryId = foodRepository.getCategoryId(foodRequest.getCategoryId());
        if (categoryId == null) {
            throw new SearchNotFoundException("Category with ID " + foodRequest.getCategoryId() + " does not exist!");
        }
        return foodDTOConvertor.toDto(foodRepository.insertFood(foodRequest, userId));
    }

    @Override
    public FoodDTO updateFoodById(UUID id, FoodRequest foodRequest, UUID currentUserId) {

        // Retrieve the food item by its ID
        Food food = foodRepository.getFoodById(id);
        if (food == null) {
            throw new SearchNotFoundException("Food with ID " + id + " does not exist");
        }

        // Retrieve the category ID from the request and validate it exists
        UUID categoryId = foodRepository.getCategoryId(foodRequest.getCategoryId());
        if (categoryId == null) {
            throw new CustomNotFoundException("Category with ID " + foodRequest.getCategoryId() + " does not exist!");
        }

        // Check if the current user is the owner of the food item
        UUID foodOwnerId = foodRepository.isFoodOwner(id, currentUserId);
        log.info("Food Owner ID: {}", foodOwnerId);
        log.info("Current User ID: {}", currentUserId);
        if (foodOwnerId == null || !foodOwnerId.equals(currentUserId)) {
            throw new ForbiddenException("Current user is not the owner of the food item and cannot update it");
        }

        // Proceed with the update if the user is the owner
        Food updatedFood = foodRepository.updateFood(id, foodRequest, currentUserId);

        // Convert the updated food entity to a DTO and return it
        return foodDTOConvertor.toDto(updatedFood);
    }

    @Override
    public FoodDTO deleteFood(UUID id, UUID currentUserId) {
        // Retrieve the food item by its ID
        Food food = foodRepository.getFoodById(id);
        if (food == null) {
            throw new SearchNotFoundException("Food with ID " + id + " does not exist");
        }

        // Check if the current user is the owner of the food item
        UUID foodOwnerId = foodRepository.isFoodOwner(id, currentUserId);
        log.info("Food Owner ID: {}", foodOwnerId);
        log.info("Current User ID: {}", currentUserId);
        if (foodOwnerId == null || !foodOwnerId.equals(currentUserId)) {
            throw new ForbiddenException("Current user is not the owner of the food item and cannot delete it");
        }

        // Proceed with the deletion if the user is the owner
        Food deletedFood = foodRepository.deleteFood(id);

        // Convert the deleted food entity to a DTO and return it
        return foodDTOConvertor.toDto(deletedFood);
    }


}
