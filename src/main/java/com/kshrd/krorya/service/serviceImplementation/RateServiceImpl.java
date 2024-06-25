package com.kshrd.krorya.service.serviceImplementation;

import com.kshrd.krorya.exception.CustomBadRequestException;
import com.kshrd.krorya.exception.SearchNotFoundException;
import com.kshrd.krorya.model.entity.Food;
import com.kshrd.krorya.model.entity.Rate;
import com.kshrd.krorya.model.enumeration.RatingStar;
import com.kshrd.krorya.repository.FoodRepository;
import com.kshrd.krorya.repository.RateRepository;
import com.kshrd.krorya.service.RateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RateServiceImpl implements RateService {
    private final RateRepository rateRepository;
    private final FoodRepository foodRepository;

    @Override
    public Rate createFoodRate(UUID foodId, RatingStar ratingStar, UUID userId) {
        Food food = foodRepository.getFoodById(foodId);
        System.out.println(food);
        if (food == null){
            throw new SearchNotFoundException("The food with ID " + foodId + " does not exist");
        }
        UUID ownerFoodId = rateRepository.getUserIdByFoodId(foodId);
        if (userId.equals(ownerFoodId)){
            throw new CustomBadRequestException("You cannot rate your own food");
        }

        Rate existingRateByUser = rateRepository.findByUserIdAndFoodId(userId, foodId);
        if (existingRateByUser != null) {
            throw new CustomBadRequestException("You have already rated this food, cannot rate again. ");
        }
        return rateRepository.createFoodRate(foodId, ratingStar.getValue(), userId);
    }

    @Override
    public List<Rate> getRateByFoodId(UUID foodId) {
        List<Rate> rateList = rateRepository.getRateByFoodId(foodId);
        if (rateList.isEmpty()){
            throw new SearchNotFoundException("This food has not been rated yet");
        }
        return rateList;
    }


}
