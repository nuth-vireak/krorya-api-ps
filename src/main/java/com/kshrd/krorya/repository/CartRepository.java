package com.kshrd.krorya.repository;

import com.kshrd.krorya.model.entity.Cart;
import com.kshrd.krorya.model.entity.CartWithFood;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface CartRepository {
    @Select("""
    INSERT INTO carts (buyer_id, seller_id)
    VALUES (#{buyerId}, #{sellerId})
    RETURNING *;
    """)
    @Results(id = "cartMapping", value = {
            @Result(property = "cartId", column = "cart_id"),
            @Result(
                    property = "buyer", column = "buyer_id",
                    one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.getUserById")
            ),
            @Result(
                    property = "seller", column = "seller_id",
                    one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.getUserById")
            ),
            @Result(property = "isOrder", column = "is_order"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    Cart insertCart(@Param("buyerId") UUID buyerId, @Param("sellerId") UUID sellerId);

    @Select("""
    SELECT * FROM carts WHERE buyer_id = #{buyerId} AND is_order = false;
    """)
    @ResultMap("cartMapping")
    List<Cart> getAllCartByBuyerId(@Param("buyerId") UUID buyerId);

    @Select("""
    SELECT * FROM carts WHERE seller_id = #{sellerId};
    """)
    @ResultMap("cartMapping")
    Cart getCartBySellerId(@Param("sellerId") UUID sellerId);

    @Select("""
    SELECT * FROM carts WHERE seller_id = #{sellerId} AND buyer_id = #{userId};
    """)
    @ResultMap("cartMapping")
    Cart getCartBySellerIdAndCurrentUserID(@Param("sellerId") UUID sellerId, @Param("userId") UUID userId);

    @Select("""
    SELECT *
    FROM carts 
    WHERE cart_id = #{cartId} 
    AND is_order = false
    AND buyer_id = #{currentUser}
    """)
    @ResultMap("cartMapping")
    Cart getCartByCartId(@Param("cartId") UUID cartId, UUID currentUser);

    @Select("""
    UPDATE carts SET is_order = true WHERE cart_id = #{cartId} AND is_order = false RETURNING *;
    """)
    @ResultMap("cartMapping")
    Cart makeCardOrdered(@Param("cartId") UUID cartId);

    @Select("""
    SELECT * FROM carts WHERE buyer_id = #{buyerId};
    """)
    @Results(id = "cartWithFoodMapping", value = {
            @Result(property = "cartId", column = "cart_id"),
            @Result(
                    property = "buyer", column = "buyer_id",
                    one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.getUserById")
            ),
            @Result(
                    property = "seller", column = "seller_id",
                    one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.getUserById")
            ),
            @Result(
                    property = "foods", column = "cart_id",
                    many = @Many(select = "com.kshrd.krorya.repository.CartFoodRepository.getAllFoodByCartId")
            ),
            @Result(property = "isOrder", column = "is_order"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    List<CartWithFood> getAllCartIdByBuyerId(@Param("buyerId") UUID buyerId);

    @Select("""
    SELECT * FROM carts WHERE seller_id = #{sellerId};
    """)
    @ResultMap("cartWithFoodMapping")
    List<CartWithFood> getAllCartIdBySellerId(@Param("sellerId") UUID sellerId);

    @Select("""
    DELETE FROM carts WHERE cart_id = #{cartId} RETURNING *;
    """)
    @ResultMap("cartMapping")
    Cart deleteCartId(@Param("cartId") UUID cartId);



    ////////////////////////
    @Select("""
    SELECT *
    FROM carts
    WHERE cart_id = #{cartId}
    """)
    @ResultMap("cartMapping")
    Cart getCartByCartIdNoCurrentUser(@Param("cartId") UUID cartId);

    //////////////////////


}
