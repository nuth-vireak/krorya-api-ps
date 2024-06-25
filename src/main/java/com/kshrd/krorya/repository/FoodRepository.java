package com.kshrd.krorya.repository;

import com.kshrd.krorya.configuration.UUIDTypeHandler;
import com.kshrd.krorya.model.dto.FoodDTO;
import com.kshrd.krorya.model.entity.Food;
import com.kshrd.krorya.model.request.FoodRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface FoodRepository {

    @Select("""
            SELECT f.*
            FROM foods f
            WHERE is_bookmarked = 'false'
            ORDER BY f.created_at DESC
            LIMIT #{size}
            OFFSET #{size} * (#{page} - 1);
            """)
    @Results(id = "foodMapping", value = {
            @Result(column = "food_id", property = "foodId", javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(column = "category_id", property = "categoryId", javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(column = "food_name", property = "foodName"),
            @Result(column = "description", property = "foodDescription"),
            @Result(column = "image", property = "foodImage"),
            @Result(column = "price", property = "foodPrice"),
            @Result(column = "star_average", property = "starAverage"),
            @Result(column = "total_rater", property = "totalRater"),
            @Result(column = "created_at", property = "createAt"),
            @Result(column = "is_bookmarked", property = "isBookmarked"),
            @Result(column = "seller_id", property = "sellerInfo",
                    one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.getSimpleAppUserById"))
    })
    List<Food> getAllFoodsWithoutBookmark(@Param("page") int page, @Param("size") int size);

    @Select("""
            SELECT f.*,
                   CASE WHEN fb.user_id IS NOT NULL THEN TRUE ELSE FALSE END AS is_bookmarked
            FROM foods f
                     LEFT JOIN food_bookmark fb ON f.food_id = fb.food_id AND fb.user_id = #{currentUser}
            ORDER BY f.created_at DESC
            LIMIT #{size}
            OFFSET #{size} * (#{page} - 1);
            """)
    @Results(id = "foodMappingWithBookmark", value = {
            @Result(column = "food_id", property = "foodId", javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(column = "category_id", property = "categoryId", javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(column = "food_name", property = "foodName"),
            @Result(column = "description", property = "foodDescription"),
            @Result(column = "image", property = "foodImage"),
            @Result(column = "price", property = "foodPrice"),
            @Result(column = "star_average", property = "starAverage"),
            @Result(column = "total_rater", property = "totalRater"),
            @Result(column = "created_at", property = "createAt"),
            @Result(column = "is_bookmarked", property = "isBookmarked"),
            @Result(column = "seller_id", property = "sellerInfo",
                    one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.getSimpleAppUserById"))
    })
    List<Food> getAllFoods(@Param("page") int page, @Param("size") int size, @Param("currentUser") UUID currentUser);

    @Select("""
            SELECT *
            FROM foods
            ORDER BY star_average DESC
            LIMIT 3
            """)
    @ResultMap("foodMapping")
    List<FoodDTO> getTopRatedFoods();

    @Select("""
            SELECT f.*
            FROM foods f JOIN categories c ON f.category_id = c.category_id
            WHERE c.category_id = #{categoryId}
            ORDER BY created_at DESC
            LIMIT #{size}
            OFFSET #{size} * (#{page} - 1);
            """)
    @ResultMap(value = "foodMapping")
    @Result(property = "foodList", column = "category_id",
            many = @Many(select = "getCategoryById"))
    List<Food> getFoodsByCategoryId(int page, int size, UUID categoryId);

    @Select("""
            SELECT category_id FROM categories
            WHERE category_id = #{categoryId}
            """)
    @Result(property = "categoryId", column = "category_id")
    UUID getCategoryId(UUID categoryId);

    @Select("""
            SELECT *
            FROM foods WHERE food_id = #{id}
            --AND is_bookmarked = false
            --OR is_bookmarked = true;
            """)
    @ResultMap(value = "foodMapping")
    Food getFoodById(UUID id);

    @Select("""
            INSERT INTO foods (food_name, seller_id, description, price, image, category_id)
            VALUES (#{food.foodName}, #{userId}, #{food.foodDescription}, #{food.foodPrice}, #{food.foodImage}, #{food.categoryId})
            RETURNING *;
            """)
    @ResultMap(value = "foodMapping")
    Food insertFood(@Param("food") FoodRequest foodRequest, UUID userId);

    @Select("""
            UPDATE foods
            SET food_name = #{food.foodName}, description = #{food.foodDescription}, price = CAST(#{food.foodPrice} AS NUMERIC), image = #{food.foodImage}
            WHERE food_id = #{id}
            AND seller_id = #{currentUserId}
            RETURNING *;
            """)
    @ResultMap(value = "foodMapping")
    Food updateFood(UUID id, @Param("food") FoodRequest foodRequest, @Param("currentUserId") UUID currentUserId);

    @Select("""
            DELETE FROM foods WHERE food_id = #{id}
            RETURNING *;
            """)
    @ResultMap(value = "foodMapping")
    Food deleteFood(UUID id);

    @Select("""
            SELECT *
            FROM foods f JOIN users u ON f.seller_id = u.user_id
            WHERE f.seller_id = #{userId}
            ORDER BY f.created_at DESC
            LIMIT 4;
            """)
    @ResultMap(value = "foodMapping")
    List<Food> getLatestFoodsByUserId(@Param("userId") UUID userId);

    @Select("""
            UPDATE foods
            SET is_bookmarked = #{bookmarked}
            WHERE food_id = #{foodId}
            RETURNING *
            """)
    @ResultMap("foodMapping")
    Food updateFoodBookmarked(@Param("foodId") UUID foodId, @Param("bookmarked") boolean bookmarked);

    @Select("""
            SELECT *
            FROM foods
            WHERE is_bookmarked = TRUE
            AND seller_id = #{currentUser}
            """)
    @ResultMap("foodMapping")
    List<Food> getFoodByBookmarked(@Param("currentUser") UUID currentUser);

    @Select("""
            SELECT seller_id FROM foods
            WHERE food_id = #{foodId} AND seller_id = #{currentUserId}
            """)
    UUID isFoodOwner(@Param("foodId") UUID foodId, @Param("currentUserId") UUID currentUserId);


    @Insert("INSERT INTO food_bookmark (user_id, food_id) VALUES (#{userId}, #{foodId})")
    void addBookmark(@Param("foodId") UUID foodId, @Param("userId") UUID userId);

    @Delete("DELETE FROM food_bookmark WHERE user_id = #{userId} AND food_id = #{foodId}")
    void removeBookmark(@Param("foodId") UUID foodId, @Param("userId") UUID userId);

    @Select("SELECT f.* FROM foods f INNER JOIN food_bookmark fb ON f.food_id = fb.food_id WHERE fb.user_id = #{userId}")
    @ResultMap("foodMapping")
    List<Food> findFoodByBookmarked(@Param("userId") UUID userId);

    @Select("SELECT COUNT(*) > 0 FROM food_bookmark WHERE user_id = #{userId} AND food_id = #{foodId}")
    boolean isBookmarked(@Param("userId") UUID userId, @Param("foodId") UUID foodId);
}
