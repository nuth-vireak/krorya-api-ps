package com.kshrd.krorya.service;

import com.kshrd.krorya.model.dto.CartDTO;
import com.kshrd.krorya.model.dto.CartWithFoodDTO;
import com.kshrd.krorya.model.entity.Address;
import com.kshrd.krorya.model.entity.Cart;
import com.kshrd.krorya.model.request.AddressRequest;
import com.kshrd.krorya.model.request.CartRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

public interface CartService {
    Cart insertCart(UUID buyerId, UUID sellerId);

    List<CartWithFoodDTO> getAllCartByCurrentUserId();

    Cart getCartBySellerId(UUID sellerId);

    CartDTO deleteCartByCartId(UUID cartId);

    Cart getCartBySellerIdAndCurrentUserId(UUID sellerId, UUID currentUserId);

    List<CartWithFoodDTO> getAllCartBySellerId(UUID sellerId);

    CartDTO getCartByCartId(UUID cartId);
}
