package com.kshrd.krorya.repository;

import com.kshrd.krorya.model.entity.CartFood;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface CartFoodRepository {
    @Select("""
    INSERT INTO cart_foods (cart_id, food_id, qty)
    VALUES (#{cartId}, #{foodId}, 1)
    RETURNING *;
    """)
    @Results(id = "cartFoodMapping", value = {
            @Result(property = "cartFoodId", column = "cart_food_id"),
            @Result(property = "cartId", column = "cart_id"),
            @Result(
                    property = "food", column = "food_id",
                    one = @One(select = "com.kshrd.krorya.repository.FoodRepository.getFoodById")
            ),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    CartFood insertCartFood(@Param("cartId") UUID cartId, @Param("foodId") UUID foodId);

    @Select("""
    SELECT * FROM
    cart_foods 
    WHERE cart_id = #{cartId}
    And cart_id = (SELECT cart_id FROM carts WHERE buyer_id = #{currentUser} AND is_order = false)
    """)
    @ResultMap("cartFoodMapping")
    List<CartFood> getAllCartFoodByCartId(@Param("cartId") UUID cartId, UUID currentUser);

    @Select("""
    SELECT * FROM cart_foods WHERE cart_id = #{cartId};
    """)
    @ResultMap("cartFoodMapping")
    List<CartFood> getAllFoodByCartId(@Param("cartId") UUID cartId);

    @Select("""
    DELETE FROM
     cart_foods 
       WHERE cart_food_id = #{cartFoodId}
      And cart_id = (SELECT cart_id FROM carts WHERE buyer_id = #{currentUser} AND is_order = false)
      RETURNING *;
    """)
    @ResultMap("cartFoodMapping")
    CartFood deleteFoodFromCartId(@Param("cartFoodId") UUID cartFoodId, UUID currentUser);

    @Select("""
        UPDATE cart_foods SET qty = #{qty} WHERE cart_food_id = #{cartFoodId} RETURNING *
    """)
    @ResultMap("cartFoodMapping")
    CartFood updateFoodQtyByCartFoodId(@Param("cartFoodId") UUID cartFoodId, @Param("qty") Integer qty);

    @Select("""
        SELECT * FROM cart_foods WHERE cart_id = #{cartId} AND food_id = #{foodId}
    """)
    @ResultMap("cartFoodMapping")
    CartFood getCartFoodByFoodIdAndCartId(@Param("cartId") UUID cartId, @Param("foodId") UUID foodId);
}
