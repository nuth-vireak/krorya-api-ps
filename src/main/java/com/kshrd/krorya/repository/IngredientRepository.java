package com.kshrd.krorya.repository;

import com.kshrd.krorya.configuration.UUIDTypeHandler;
import com.kshrd.krorya.model.dto.IngredientDTO;
import com.kshrd.krorya.model.entity.Ingredient;
import com.kshrd.krorya.model.request.IngredientRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface IngredientRepository {

    @Select("SELECT * FROM ingredients WHERE user_id IS NULL")
    @Results(id = "ingredientMapping", value = {
            @Result(property = "ingredientId", column = "ingredient_id", javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "ingredientName", column = "name"),
            @Result(property = "ingredientIcon", column = "icon"),
            @Result(property = "ingredientType", column = "type")
    })
    List<Ingredient> getIngredientsWithNullUserId();

    @Select("""
            SELECT qty FROM recipe_ingredient WHERE ingredient_id = #{ingredientId}
            """)
    String getQtyByIngredientId(UUID ingredientId);

    @Select("SELECT * FROM ingredients WHERE name = #{name} AND user_id IS NULL")
    @ResultMap("ingredientMapping")
    Optional<Ingredient> findByNameAndUserIdIsNull(@Param("name") String name);

    @Select("SELECT * FROM ingredients WHERE name = #{name} AND user_id IS NOT NULL")
    Optional<Ingredient> findByNameAndUserIdNotNull(@Param("name") String name);

    @Insert("""
            INSERT INTO ingredients (user_id, icon, name, type)
            VALUES (#{ingredient.userId}, #{ingredient.ingredientIcon}, #{ingredient.ingredientName}, #{ingredient.ingredientType})
            """)
    @ResultMap("ingredientMapping")
    void insertIngredient(@Param("ingredient") Ingredient ingredient);

    @Select("SELECT * FROM ingredients WHERE name = #{name} AND user_id = #{currentUserId}")
    @ResultMap("ingredientMapping")
    Optional<Ingredient> findByNameAndUserId(String name, UUID currentUserId);

    @Select("""
                SELECT i.* , qty FROM ingredients i 
                INNER JOIN recipe_ingredient ri ON i.ingredient_id = ri.ingredient_id
                WHERE ri.recipe_id = #{recipeId}
            """)
    @Result(property = "ingredientId", column = "ingredient_id", javaType = UUID.class, typeHandler = UUIDTypeHandler.class)
    @Result(property = "userId", column = "user_id")
    @Result(property = "ingredientName", column = "name")
    @Result(property = "ingredientIcon", column = "icon")
    @Result(property = "ingredientType", column = "type")
    @Result(property = "quantity", column = "qty")
    List<Ingredient> findIngredientByRecipeId(@Param("recipeId") UUID recipeId);

    @Select("""
            SELECT * 
            FROM ingredients
            WHERE ingredient_id = #{ingredientId}
            """)
    @ResultMap("ingredientMapping")
    Ingredient getIngredientByid(@Param("ingredientId") UUID ingredientId);

    @Select("""
    INSERT INTO ingredients(user_id, name, type, icon) VALUES (#{userId}, #{ingredient.ingredientName}, #{ingredient.ingredientType}, #{ingredient.ingredientIcon})
    RETURNING *
    """)
    @ResultMap("ingredientMapping")
    Ingredient createNewIngredient(@Param("ingredient") Ingredient ingredient, @Param("userId") UUID userId);

    @Select("""
    SELECT ingredient_id FROM recipe_ingredient WHERE recipe_id = #{recipeId} AND ingredient_id = #{ingredientId}
    """)
    UUID getIngredientIdByRecipeIdAndIngredientId(@Param("recipeId") UUID recipeId, @Param("ingredientId") UUID ingredientId);

    @Delete("""
    DELETE FROM recipe_ingredient WHERE recipe_id = #{recipeId}
""")
    void deleteRecipeIngredientsByRecipeId(UUID recipeId); }
