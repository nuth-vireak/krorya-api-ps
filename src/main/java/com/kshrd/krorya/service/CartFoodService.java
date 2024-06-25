package com.kshrd.krorya.service;

import com.kshrd.krorya.model.dto.CartFoodDTO;
import com.kshrd.krorya.model.dto.CartWithFoodDTO;
import com.kshrd.krorya.model.entity.CartFood;
import com.kshrd.krorya.model.request.CartFoodRequest;

import java.util.List;
import java.util.UUID;

public interface CartFoodService {
    CartFoodDTO insertCartFood(UUID foodId);

    List<CartFoodDTO> getAllCartFoodByCartId(UUID cartId);

    CartFoodDTO deleteFoodFromCart(UUID cartFoodId);

    CartFoodDTO updateFoodQtyByCartFoodId(UUID cartFoodId, CartFoodRequest cartFoodRequest);
}
