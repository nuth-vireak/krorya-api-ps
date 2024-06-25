package com.kshrd.krorya.repository;

import com.kshrd.krorya.configuration.UUIDTypeHandler;
import com.kshrd.krorya.model.entity.GroceryIngredient;
import com.kshrd.krorya.model.entity.GroceryList;
import com.kshrd.krorya.model.entity.Ingredient;
import com.kshrd.krorya.model.request.GroceryListRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface GroceryListRepository {


    @Select("""
        SELECT gl.grocery_id, gl.title, COUNT(gr.recipe_id) AS num_recipes
        FROM grocery_lists gl
        LEFT JOIN grocery_recipe gr ON gl.grocery_id = gr.grocery_id
        WHERE gl.user_id = #{currentUserId}
        GROUP BY gl.grocery_id, gl.title
        ORDER BY gl.grocery_id
        LIMIT #{size} OFFSET #{size} * (#{page} - 1)
    """)
    @Results(id="groceryListMapping", value = {
            @Result(property = "groceryListId", column = "grocery_id", javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(property = "groceryListTitle", column = "title"),
             @Result(property = "totalOfRecipes", column = "num_recipes") }
    )
    List<GroceryList> getALLGroceryList(@Param("page") int page,@Param("size") int size, @Param("currentUserId") UUID currentUserId);

    @Select("""
        INSERT INTO grocery_lists(title, user_id)
        VALUES (#{groceryList.title}, #{currentUserId})
        Returning *
    """)
    @ResultMap("groceryListMapping")
    GroceryList createGroceryList(@Param("groceryList") GroceryListRequest groceryListRequest, UUID currentUserId);

    @Select("""
      select * from grocery_lists where grocery_id = #{groceryId}
      """)
    @Results(id = "groceryListWithRecipesMapping", value = {
            @Result(property = "groceryListId", column = "grocery_id", javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(property = "groceryListTitle", column = "title"),
            @Result(property = "userId", column = "user_id", javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(property = "totalOfRecipes", column = "num_recipes"),
            @Result(property = "recipes", column = "grocery_id", many = @Many(select = "com.kshrd.krorya.repository.RecipeRepository.getRecipesByGroceryId"))
    })
    GroceryList getGroceryListById(UUID groceryId);


    @Select("""
          UPDATE grocery_lists
          SET title = #{groceryList.title}
          WHERE grocery_id = #{groceryId}
         And user_id = #{currentUserId}
       RETURNING *
   """)
    @ResultMap("groceryListMapping")
    GroceryList updateGroceryList(UUID groceryId, @Param("groceryList") GroceryListRequest groceryListRequest, @Param("currentUserId") UUID currentUserId);


    @Delete("""
        DELETE FROM grocery_lists
        WHERE grocery_id = #{id}
        AND user_id = #{currentUserId}
    """)
    void deleteGroceryList(UUID id, UUID currentUserId);

    @Select("""
            INSERT INTO grocery_recipe(grocery_id, recipe_id)
            VALUES(#{groceryId}, #{recipeId})
            RETURNING *
            """)
    @ResultMap("groceryListMapping")
    GroceryList addToGroceryList(UUID groceryId, UUID recipeId);

    @Select("""
        SELECT ri.*
        FROM grocery_recipe gr
        JOIN recipe_ingredient ri ON gr.recipe_id = ri.recipe_id
        WHERE gr.grocery_id = #{groceryId} AND ri.ingredient_id = #{ingredientId}
    """)
    Ingredient findIngredientInRecipe(@Param("groceryId") UUID groceryId, @Param("ingredientId") UUID ingredientId);

    @Insert("""
        INSERT INTO grocery_ingredient(grocery_id, ingredient_id, is_bought)
        VALUES(#{groceryId}, #{ingredientId}, false)
      """)
    void addIngredientToGroceryList(UUID groceryId, UUID ingredientId);


    @Select("""
    SELECT * FROM grocery_ingredient
        WHERE grocery_id = #{groceryId}
        AND ingredient_id = #{ingredientId}
    """)
    @Results(id = "groceryIngredientMapping", value = {
            @Result(property = "groceryId", column = "grocery_id", javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(property = "ingredientId", column = "ingredient_id", javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(property = "isBought", column = "is_bought")
    })
    GroceryIngredient findIngredientInGroceryIngredient(UUID groceryId, UUID ingredientId);

    @Update("""
        UPDATE grocery_ingredient
        SET is_bought = #{isBought}
        WHERE ingredient_id = #{ingredientId}
        AND grocery_id = #{groceryId}
    """)
    void updateIngredientStatus(UUID ingredientId, UUID groceryId, boolean isBought);

    @Select("""
        SELECT * FROM grocery_lists
        WHERE grocery_id = #{groceryId}
        And user_id = #{currentUserId}
    """)
    GroceryList checkUserOfGroceryList(UUID groceryId, UUID currentUserId);
}
