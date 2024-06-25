package com.kshrd.krorya.controller;

import com.kshrd.krorya.exception.SearchNotFoundException;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.entity.Food;
import com.kshrd.krorya.model.entity.Rate;
import com.kshrd.krorya.model.enumeration.RatingStar;
import com.kshrd.krorya.model.response.ApiResponse;
import com.kshrd.krorya.repository.FoodRepository;
import com.kshrd.krorya.service.RateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/rates")
@RequiredArgsConstructor
@Tag(name = "Rate Controller", description = "Endpoints for managing rates")
public class RateController {
    private final RateService rateService;
    private final FoodRepository foodRepository;

    @SecurityRequirement(name="bearerAuth")
    @Operation(summary = "Create food rating")
    @PostMapping("{foodId}")
    public ResponseEntity<ApiResponse<Rate>> createRateOnFood(@Parameter(description = "Food ID to rate", example = "d290f1ee-6c54-4b01-90e6-d701748f0851") @PathVariable UUID foodId, @RequestParam RatingStar ratingStar){
        UUID userId = getCurrentUserId();
        Rate rate = rateService.createFoodRate(foodId, ratingStar, userId);
        return ResponseEntity.ok(
                ApiResponse.<Rate>builder()
                        .message("A rate is created successfully!")
                        .status(HttpStatus.CREATED)
                        .code(201)
                        .payload(rate)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }

    @Operation(summary = "Get all food rating by food id")
    @GetMapping("{foodId}")
    public ResponseEntity<ApiResponse<List<Rate>>> getRateByFoodId(@Parameter(description = "Food ID to retrieving related rates", example = "d290f1ee-6c54-4b01-90e6-d701748f0851") @PathVariable UUID foodId){
        Food food = foodRepository.getFoodById(foodId);
        if (food == null){
            throw new SearchNotFoundException("Food with ID " + foodId +" does not exist");
        }
        List<Rate> rateList = rateService.getRateByFoodId(foodId);
        return ResponseEntity.ok(
                ApiResponse.<List<Rate>>builder()
                        .message("All rates of food ID " + foodId +" are fetched successfully!")
                        .status(HttpStatus.OK)
                        .code(200)
                        .payload(rateList)
                        .localDateTime(LocalDateTime.now())
                        .build()
        );
    }


    private UUID getCurrentUserId() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getAppUser().getUserId();
    }
}
