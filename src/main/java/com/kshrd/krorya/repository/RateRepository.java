package com.kshrd.krorya.repository;

import com.kshrd.krorya.model.entity.Rate;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface RateRepository {

    @Select("""
    INSERT INTO rates (food_id, rater, star) VALUES (#{foodId}, #{userId}, #{ratingStar})
    RETURNING *
    """)
    @Results(id = "rateMapping", value = {
            @Result(property = "rateId", column = "rate_id"),
            @Result(property = "foodName", column = "food_id",
            one = @One(select = "getFoodNameByFoodId")),
            @Result(property = "rateStar", column = "star"),
            @Result(property = "raterName", column = "rater",
            one = @One(select = "getUserNameByUserId"))
    })
    Rate createFoodRate(@Param("foodId") UUID foodId, Integer ratingStar, @Param("userId") UUID userId);
    @Select("""
    SELECT username from users WHERE user_id = #{userId}
    """)
    String getUserNameByUserId(UUID userId);

    @Select("""
    SELECT food_name FROM foods WHERE food_id = #{foodId}
    """)
    String getFoodNameByFoodId(UUID foodId);

    @Select("SELECT * FROM rates WHERE rater = #{userId} AND food_id = #{foodId}")
    @ResultMap("rateMapping")
    Rate findByUserIdAndFoodId(@Param("userId") UUID userId, @Param("foodId") UUID foodId);

    @Select("""
    SELECT *
    FROM rates r JOIN foods f ON r.food_id = f.food_id
    WHERE f.food_id = #{foodId}
    """)
    @ResultMap("rateMapping")
    List<Rate> getRateByFoodId(UUID foodId);

    @Select("""
    SELECT user_id
    FROM users u JOIN foods f ON u.user_id = f.seller_id
    WHERE food_id = #{foodId}
    """)
    UUID getUserIdByFoodId(UUID fooId);
}

