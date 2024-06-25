package com.kshrd.krorya.service.serviceImplementation;

import com.kshrd.krorya.convert.CartDTOConvertor;
import com.kshrd.krorya.convert.CartWithFoodConvertor;
import com.kshrd.krorya.exception.CustomNotFoundException;
import com.kshrd.krorya.exception.ForbiddenException;
import com.kshrd.krorya.exception.SearchNotFoundException;
import com.kshrd.krorya.model.dto.CartDTO;
import com.kshrd.krorya.model.entity.*;
import com.kshrd.krorya.repository.AppUserRepository;
import com.kshrd.krorya.repository.CartRepository;
import com.kshrd.krorya.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.kshrd.krorya.model.dto.CartWithFoodDTO;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartWithFoodConvertor cartWithFoodConvertor;
    private final CartDTOConvertor cartDTOConvertor;
    private final AppUserRepository appUserRepository;

    @Override
    public Cart insertCart(UUID buyerId, UUID sellerId) {
        Cart cart = cartRepository.insertCart(buyerId, sellerId);
        System.out.println("This my service cart: "+ cart);
        return cart;
    }

    @Override
    public List<CartWithFoodDTO> getAllCartByCurrentUserId() {
        UUID currentUserId = getCurrentUserId();
        List<CartWithFood> carts = cartRepository.getAllCartIdByBuyerId(currentUserId);
        if (carts.isEmpty()){
            throw new SearchNotFoundException("List of cart food is empty");
        }
        return cartWithFoodConvertor.convertListCartWithFoodToListCartWithFoodDTO(carts);
    }


    @Override
    public List<CartWithFoodDTO> getAllCartBySellerId(UUID sellerId) {
        if( appUserRepository.getUserById(sellerId)==null){
            throw new CustomNotFoundException("Seller id : (" + sellerId + ") not found!");
        }

        List<CartWithFood> carts = cartRepository.getAllCartIdBySellerId(sellerId);
        if (carts.isEmpty()){
            throw new SearchNotFoundException("List of cart food is empty");
        }
        return cartWithFoodConvertor.convertListCartWithFoodToListCartWithFoodDTO(carts);
    }

    @Override
    public Cart getCartBySellerId(UUID sellerId) {
        Cart getCart = cartRepository.getCartBySellerId(sellerId);
        if(getCart == null){
            throw new CustomNotFoundException("Seller id : (" + sellerId + ") not found!");
        };
        return cartRepository.getCartBySellerId(sellerId);
    }

    public Cart getCartBySellerIdAndCurrentUserId(UUID sellerId, UUID currentUserId) {
        return cartRepository.getCartBySellerIdAndCurrentUserID(sellerId, currentUserId);
    }

    @Override
    public CartDTO deleteCartByCartId(UUID cartId) {
        Cart cartToDelete = cartRepository.getCartByCartId(cartId, getCurrentUserId());
        if (cartToDelete == null){
            throw new CustomNotFoundException("Cart id : (" + cartId + ") doesn't have in your carts list!");
        }
        getCartByCartId(cartId);
        Cart deleteCart = cartRepository.deleteCartId(cartId);
        return cartDTOConvertor.convertCartToCartDTO(deleteCart);
    }

    @Override
    public CartDTO getCartByCartId(UUID cartId) {
        Cart getCart = cartRepository.getCartByCartId(cartId, getCurrentUserId());
        if(getCart == null){
            throw new CustomNotFoundException("Cart id : (" + cartId + ") doesn't have in your carts list!");
        }
        return cartDTOConvertor.convertCartToCartDTO(getCart);
    }

    private UUID getCurrentUserId() {
        CustomUserDetail userDetails = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getAppUser().getUserId();
    }
}
