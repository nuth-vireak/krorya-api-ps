package com.kshrd.krorya.service.serviceImplementation;

import com.kshrd.krorya.convert.CartFoodDTOConvertor;
import com.kshrd.krorya.exception.CustomBadRequestException;
import com.kshrd.krorya.model.dto.*;
import com.kshrd.krorya.model.entity.Cart;
import com.kshrd.krorya.model.entity.CartFood;
import com.kshrd.krorya.model.entity.CartWithFood;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import com.kshrd.krorya.model.request.CartFoodRequest;
import com.kshrd.krorya.repository.CartFoodRepository;
import com.kshrd.krorya.repository.CartRepository;
import com.kshrd.krorya.service.AppUserService;
import com.kshrd.krorya.service.CartFoodService;
import com.kshrd.krorya.service.CartService;
import com.kshrd.krorya.service.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartFoodServiceImpl implements CartFoodService {
    private final CartFoodRepository cartFoodRepository;
    private final CartService cartService;
    private final CartFoodDTOConvertor cartFoodDTOConvertor;
    private final AppUserService appUserService;
    private final FoodService foodService;

    @Override
    public CartFoodDTO insertCartFood(UUID foodId) {
        FoodDTO foodDTO = foodService.getFoodById(foodId);
        if (foodDTO == null){
            throw new CustomBadRequestException("Food not found with id " + foodId);
        }
        UUID sellerId = foodDTO.getSellerInfo().getUserId();
        UUID currentUser = getCurrentUserId();

        Cart cart = cartService.getCartBySellerIdAndCurrentUserId(sellerId, currentUser);
        System.out.println("Food cart: " + cart);

        CartFood cartFood;
        //  if cart already created
        if (cart != null){
            CartFood cartFoodRes = cartFoodRepository.getCartFoodByFoodIdAndCartId(cart.getCartId(), foodId);
            if (cartFoodRes != null){
                Integer qty = cartFoodRes.getQty() + 1;
                cartFood = cartFoodRepository.updateFoodQtyByCartFoodId(cartFoodRes.getCartFoodId(), qty);
                return cartFoodDTOConvertor.convertCartFoodToCartFoodDTO(cartFood);
            }
            cartFood = cartFoodRepository.insertCartFood(cart.getCartId(), foodId);
        }
        //  if cart has not created
        else {
            Cart newCart = cartService.insertCart(currentUser, sellerId);
            cartFood = cartFoodRepository.insertCartFood(newCart.getCartId(), foodId);
        }
        return cartFoodDTOConvertor.convertCartFoodToCartFoodDTO(cartFood);
    }

    @Override
    public List<CartFoodDTO> getAllCartFoodByCartId(UUID cartId) {
        List<CartFood> cartFoods = cartFoodRepository.getAllCartFoodByCartId(cartId, getCurrentUserId());
        if (cartFoods.isEmpty()){
            throw new CustomBadRequestException("CartFood with cart id (" + cartId +" ) not found in your carts");
        }
        return cartFoodDTOConvertor.convertListCartFoodToListCartFoodDTO(cartFoods);
    }

    @Override
    public CartFoodDTO deleteFoodFromCart(UUID cartFoodId) {
        CartFood cartFood = cartFoodRepository.deleteFoodFromCartId(cartFoodId, getCurrentUserId());
        if (cartFood == null){
            throw new CustomBadRequestException("CartFood with id (" + cartFoodId + " not found in your carts");
        }
        List<CartFood> cartFoods = cartFoodRepository.getAllCartFoodByCartId(cartFood.getCartId(), getCurrentUserId());
        if (cartFoods.isEmpty()) {
            cartService.deleteCartByCartId(cartFood.getCartId());
        }
        return cartFoodDTOConvertor.convertCartFoodToCartFoodDTO(cartFood);
    }

    @Override
    public CartFoodDTO updateFoodQtyByCartFoodId(UUID cartFoodId, CartFoodRequest cartFoodRequest) {
        CartFood cartFood = cartFoodRepository.updateFoodQtyByCartFoodId(cartFoodId, cartFoodRequest.getQty());
        if (cartFood == null){
            throw new CustomBadRequestException("CartFood not found with id : ( " + cartFoodId + " )");
        }
        return cartFoodDTOConvertor.convertCartFoodToCartFoodDTO(cartFood);
    }

    private UUID getCurrentUserId() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getAppUser().getUserId();
    }
}
