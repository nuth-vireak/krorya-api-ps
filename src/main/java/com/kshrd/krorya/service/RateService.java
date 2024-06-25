package com.kshrd.krorya.service;

import com.kshrd.krorya.model.entity.Rate;
import com.kshrd.krorya.model.enumeration.RatingStar;

import java.util.List;
import java.util.UUID;

public interface RateService {
    Rate createFoodRate(UUID foodId, RatingStar ratingStar, UUID userId);
    List<Rate> getRateByFoodId(UUID foodId);
}
