package com.kshrd.krorya.repository;
import com.kshrd.krorya.configuration.UUIDTypeHandler;
import com.kshrd.krorya.model.entity.Recipe;
import com.kshrd.krorya.model.entity.Tag;
import com.kshrd.krorya.model.enumeration.CookingLevelEnum;
import com.kshrd.krorya.model.enumeration.CookingTimeEnum;
import com.kshrd.krorya.model.enumeration.IngredientsEnum;
import com.kshrd.krorya.model.request.RecipeRequest;
import com.kshrd.krorya.repository.sqlProvider.RecipeSqlProvider;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mapper
public interface RecipeRepository {

    @Results(id = "recipeMapping", value = {
            @Result(property = "recipeId", column = "recipe_id", javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(property = "recipeTitle", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "creatorInfo", column = "creator", one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.getSimpleAppUserById")),
            @Result(property = "isDraft", column = "is_draft"),
            @Result(property = "isPublic", column = "is_public"),
            @Result(property = "isBookmarked", column = "is_bookmarked"),
            @Result(property = "servingSizes", column = "serving_number"),
            @Result(property = "cookingLevel", column = "cooking_level"),
            @Result(property = "cookingTime", column = "cooking_time"),
            @Result(property = "cuisineName", column = "cuisine_id", one = @One(select = "com.kshrd.krorya.repository.CuisineRepository.selectCuisineNameById")),
            @Result(property = "ingredients", column = "recipe_id", many = @Many(select = "com.kshrd.krorya.repository.IngredientRepository.findIngredientByRecipeId")),
            @Result(property = "cookingSteps", column = "recipe_id", many = @Many(select = "com.kshrd.krorya.repository.CookingStepRepository.selectCookingStepsByRecipeId")),
            @Result(property = "tagName", column = "recipe_id", many = @Many(select = "com.kshrd.krorya.repository.TagRepository.findTagNameByRecipeId")),
            @Result(property = "recipeImage", column = "image"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    @Select("""
            SELECT *
            FROM recipes
            WHERE is_draft = FALSE AND is_public = TRUE
            ORDER BY created_at DESC
            """)
    @ResultMap("recipeMapping")
    List<Recipe> getRecipes();

    @Select("SELECT * FROM recipes WHERE recipe_id IN (SELECT recipe_id FROM grocery_recipe WHERE grocery_id = #{groceryId})")
    @ResultMap("recipeMapping")
    List<Recipe> getRecipesByGroceryId(UUID groceryId);

    @Select("""
            SELECT * FROM recipes WHERE recipe_id = #{recipeId}
            """)
    @Results(id = "recipeMapping", value = {
            @Result(property = "recipeId", column = "recipe_id", javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(property = "recipeTitle", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "creatorInfo", column = "creator", one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.getSimpleAppUserById")),
            @Result(property = "isDraft", column = "is_draft"),
            @Result(property = "isPublic", column = "is_public"),
            @Result(property = "isBookmarked", column = "is_bookmarked"),
            @Result(property = "servingSizes", column = "serving_number"),
            @Result(property = "cookingLevel", column = "cooking_level"),
            @Result(property = "cookingTime", column = "cooking_time"),
            @Result(property = "cuisineName", column = "cuisine_id", one = @One(select = "com.kshrd.krorya.repository.CuisineRepository.selectCuisineNameById")),
            @Result(property = "ingredients", column = "recipe_id", many = @Many(select = "com.kshrd.krorya.repository.IngredientRepository.findIngredientByRecipeId")),
            @Result(property = "cookingSteps", column = "recipe_id", many = @Many(select = "com.kshrd.krorya.repository.CookingStepRepository.selectCookingStepsByRecipeId")),
            @Result(property = "tagName", column = "recipe_id", many = @Many(select = "com.kshrd.krorya.repository.TagRepository.findTagNameByRecipeId")),
            @Result(property = "recipeImage", column = "image"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    Recipe getRecipeById(UUID recipeId);

    @Select("""
                INSERT INTO recipes
                VALUES (DEFAULT, #{recipe.cuisineId}, #{recipe.recipeImage},
                #{recipe.description},#{recipe.cookingLevel}, #{recipe.cookingTime},
                #{recipe.servingSizes}, #{recipe.recipeTitle},
                #{recipe.isDraft} , #{recipe.isPublic},DEFAULT, #{userId}, DEFAULT )
                RETURNING recipe_id
            """)
    UUID addRecipe(@Param("recipe") RecipeRequest recipeRequest, UUID userId);


    @Results(id = "tag", value = {
            @Result(property = "tagId", column = "tag_id", javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(property = "tagName", column = "name"),
            @Result(property = "icons", column = "icons")
    })


    @Insert("""
                INSERT INTO recipe_tag (recipe_id, tag_id)
                VALUES (#{recipeId}, #{tagId})
            """)
    void insertRecipeTag(@Param("recipeId") UUID recipeId, @Param("tagId") UUID tagId);

    @Select("""
                SELECT t.tag_id, t.name FROM tags t
                JOIN recipe_tag rt ON t.tag_id = rt.tag_id
                WHERE rt.recipe_id = #{recipeId}
            """)
    List<Tag> getTagsByRecipeId(UUID recipeId);


    @Select("""
                  UPDATE recipes SET cuisine_id = #{recipe.cuisineId}, image = #{recipe.recipeImage}, description = #{recipe.description},
                             cooking_level = #{recipe.cookingLevel}, cooking_time = #{recipe.cookingTime}, serving_number = #{recipe.servingSizes},
                             title = #{recipe.recipeTitle},
                             is_draft = #{recipe.isDraft}, is_public= #{recipe.isPublic}
                             WHERE recipe_id = #{id} RETURNING *
            """)
    @ResultMap("recipeMapping")
    Recipe updateRecipe(UUID id, @Param("recipe") RecipeRequest recipeRequest);


    @Delete("""
                    DELETE FROM recipes
                    WHERE recipe_id = #{id};
            """)
    void deleteRecipeById(UUID id);

    @Select("SELECT * FROM recipes WHERE title LIKE CONCAT('%', #{name}, '%')")
    @ResultMap("recipeMapping")
    List<Recipe> getRecipeByName(@Param("name") String name);


    @Select("""
            SELECT *
            FROM recipes
            WHERE is_draft = FALSE AND is_public = TRUE
                    ORDER BY RANDOM()
                    LIMIT 5
            """)
    @ResultMap("recipeMapping")
    List<Recipe> getRecipesByRandom();

    @Select("""
                SELECT *
                FROM recipes
                WHERE creator = #{userId}
                AND is_draft = FALSE AND is_public = TRUE
            """)
    @ResultMap("recipeMapping")
    List<Recipe> findRecipesByUserId(@Param("userId") UUID userId);


    @Select("""
               SELECT * 
               FROM recipes
               WHERE is_bookmarked = TRUE
               AND creator = #{currentUser}
            """)
    @ResultMap("recipeMapping")
    List<Recipe> getRecipeByBookmarked(@Param("currentUser") UUID currentUser);

    @Insert("""
            INSERT INTO recipe_ingredient (recipe_id, ingredient_id, qty)
            VALUES (#{recipeId}, #{ingredientId}, #{quantity})
            """)
    void addRecipeIngredient(@Param("recipeId") UUID recipeId, @Param("ingredientId") UUID ingredientId, String quantity);

    @Insert("""
            INSERT INTO recipe_tag (recipe_id, tag_id)
            VALUES (#{recipeId}, #{tagId})
            """)
    void addRecipeTag(@Param("recipeId") UUID recipeId, @Param("tagId") UUID tagId);


    @SelectProvider(type = RecipeSqlProvider.class, method = "getRecipesByIngredients")
    @Result(property = "recipeId", column = "recipe_id", javaType = UUID.class, typeHandler = UUIDTypeHandler.class)
    @Result(property = "recipeTitle", column = "title")
//    @Result(property = "creatorInfo", column = "creator")
    @Result(property = "creatorInfo", column = "creator", one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.getSimpleAppUserById"))
    @Result(property = "isPublic", column = "is_public")
    @Result(property = "isBookmarked", column = "is_bookmarked")
    @Result(property = "cookingLevel", column = "cooking_level")
    @Result(property = "cookingTime", column = "cooking_time")
    @Result(property = "recipeImage", column = "image")
    @Result(property = "ingredients", column = "recipe_id", many = @Many(select = "com.kshrd.krorya.repository.IngredientRepository.findIngredientByRecipeId"))
    List<Recipe> getRecipeByIngredients(Map<String, Object> parameters);

    @Select("""
            UPDATE recipes
               SET is_bookmarked = #{bookmarked}
               WHERE recipe_id = #{recipeId}
               RETURNING *
            """)
    @ResultMap("recipeMapping")
    Recipe updateRecipeBookmarked(@Param("recipeId") UUID recipeId, @Param("bookmarked") boolean bookmarked);


    @Select("""
            SELECT * FROM recipes
            WHERE cooking_level = #{cookingLevelEnum}
            AND is_draft = FALSE AND is_public = TRUE
            """)
    @ResultMap("recipeMapping")
    List<Recipe> getRecipeByCookingLevel(@Param("cookingLevelEnum") String cookingLevelEnum);


    @Select("""
            SELECT r.*
            FROM recipes r
            JOIN cuisines c ON r.cuisine_id = c.cuisine_id
            WHERE c.name = #{cuisineName}
            AND r.is_draft = FALSE AND r.is_public = TRUE
            """)
    @ResultMap("recipeMapping")
    List<Recipe> getRecipeByCuisine(@Param("cuisineName") String cuisineName);

    @Select("""
            SELECT *
            FROM recipes
            WHERE
                (cooking_time <= 15 AND cooking_time > 0 AND #{cookingTimeEnum} = 'UNDER_15_MINUTES') OR
                (cooking_time > 15 AND cooking_time <= 30 AND #{cookingTimeEnum} = 'UNDER_30_MINUTES') OR
                (cooking_time > 30 AND cooking_time <= 60 AND #{cookingTimeEnum} = 'UNDER_60_MINUTES')
              """)
    @ResultMap("recipeMapping")
    List<Recipe> getRecipeByCookingTime(@Param("cookingTimeEnum") CookingTimeEnum cookingTimeEnum);


    @Select("""
              SELECT * FROM recipes
              WHERE is_public = FALSE
              AND creator = #{currentUser}
            """)
    @ResultMap("recipeMapping")
    List<Recipe> findRecipeByPublicOrPrivate(UUID currentUser);


    @Select("""
              SELECT r.*
              FROM recipes r
              JOIN recipe_ingredient ri ON r.recipe_id = ri.recipe_id
              JOIN ingredients i ON ri.ingredient_id = i.ingredient_id
              GROUP BY r.recipe_id
              HAVING COUNT(i.ingredient_id) < 
              CASE 
                  WHEN #{ingredientsEnum} = 'LESS_THAN_5' THEN 5
                  WHEN #{ingredientsEnum} = 'LESS_THAN_10' THEN 10
                  WHEN #{ingredientsEnum} = 'LESS_THAN_15' THEN 15
                  ELSE 1000
              END
            """)
    @ResultMap("recipeMapping")
    List<Recipe> getRecipeByNumberOfIngredients(IngredientsEnum ingredientsEnum);


    @Select("""
              SELECT * FROM recipes
              WHERE is_draft = TRUE
              AND creator = #{userId}
            """)
    @ResultMap("recipeMapping")
    List<Recipe> getAllDrafts(@Param("userId") UUID currentUser);


    @Select("""
              SELECT * FROM recipes
              WHERE is_draft = TRUE
              AND recipe_id = #{recipeId}
              AND creator = #{userId}
            """)
    @ResultMap("recipeMapping")
    Recipe findDraftsByRecipeId(@Param("recipeId") UUID recipeId, @Param("userId") UUID currentUser);

    @Select("""
            SELECT *
            FROM recipes
            WHERE creator = #{userId}
            """)
    @ResultMap("recipeMapping")
    List<Recipe> getRecipeByCurrentUserId(@Param("userId") UUID userId);

    @Select("""
            UPDATE recipes
            SET is_public = #{isPublic}
            WHERE recipe_id = #{recipeId}
            RETURNING *
            """)
    @ResultMap("recipeMapping")
    Recipe updateRecipeVisibility(UUID recipeId, boolean isPublic);

    @Select("""
            SELECT creator
            FROM recipes
            WHERE recipe_id = #{id}
            """)
    UUID getRecipeOwnerId(UUID id);

    @Select("SELECT r.* FROM recipes r INNER JOIN recipe_bookmark rb ON r.recipe_id = rb.recipe_id WHERE rb.user_id = #{userId}")
    @Result(property = "recipeId", column = "recipe_id", javaType = UUID.class, typeHandler = UUIDTypeHandler.class)
    @Result(property = "recipeTitle", column = "title")
    @Result(property = "description", column = "description")
    @Result(property = "creatorInfo", column = "creator", one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.getSimpleAppUserById"))
    @Result(property = "isDraft", column = "is_draft")
    @Result(property = "isPublic", column = "is_public")
    @Result(property = "isBookmarked", column = "is_bookmarked")
    @Result(property = "servingSizes", column = "serving_number")
    @Result(property = "cookingLevel", column = "cooking_level")
    @Result(property = "cookingTime", column = "cooking_time")
    @Result(property = "cuisineName", column = "cuisine_id", one = @One(select = "com.kshrd.krorya.repository.CuisineRepository.selectCuisineNameById"))
    @Result(property = "ingredients", column = "recipe_id", many = @Many(select = "com.kshrd.krorya.repository.IngredientRepository.findIngredientByRecipeId"))
    @Result(property = "cookingSteps", column = "recipe_id", many = @Many(select = "com.kshrd.krorya.repository.CookingStepRepository.selectCookingStepsByRecipeId"))
    @Result(property = "tagName", column = "recipe_id", many = @Many(select = "com.kshrd.krorya.repository.TagRepository.findTagNameByRecipeId"))
    @Result(property = "recipeImage", column = "image")
    @Result(property = "createdAt", column = "created_at")
    @Result(property = "updatedAt", column = "updated_at")
    List<Recipe> findRecipeByBookmarked(@Param("userId") UUID userId);

    @Insert("INSERT INTO recipe_bookmark (user_id, recipe_id) VALUES (#{userId}, #{recipeId})")
    void addBookmark(@Param("recipeId") UUID recipeId, @Param("userId") UUID userId);

    @Delete("DELETE FROM recipe_bookmark WHERE user_id = #{userId} AND recipe_id = #{recipeId}")
    void removeBookmark(@Param("recipeId") UUID recipeId, @Param("userId") UUID userId);

    @Select("SELECT COUNT(*) > 0 FROM recipe_bookmark WHERE user_id = #{userId} AND recipe_id = #{recipeId}")
    boolean isBookmarked(@Param("userId") UUID userId, @Param("recipeId") UUID recipeId);

    @Select("SELECT * FROM recipes WHERE recipe_id = #{recipeId}")
    Recipe findRecipeById(@Param("recipeId") UUID recipeId);


    @Select("SELECT r.* FROM recipes r INNER JOIN recipe_bookmark rb ON r.recipe_id = rb.recipe_id WHERE rb.user_id = #{userId}")
    @Result(property = "recipeId", column = "recipe_id", javaType = UUID.class, typeHandler = UUIDTypeHandler.class)
    @Result(property = "recipeTitle", column = "title")
    @Result(property = "description", column = "description")
    @Result(property = "creatorInfo", column = "creator", one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.getSimpleAppUserById"))
    @Result(property = "isDraft", column = "is_draft")
    @Result(property = "isPublic", column = "is_public")
    @Result(property = "isBookmarked", column = "is_bookmarked")
    @Result(property = "servingSizes", column = "serving_number")
    @Result(property = "cookingLevel", column = "cooking_level")
    @Result(property = "cookingTime", column = "cooking_time")
    @Result(property = "cuisineName", column = "cuisine_id", one = @One(select = "com.kshrd.krorya.repository.CuisineRepository.selectCuisineNameById"))
    @Result(property = "ingredients", column = "recipe_id", many = @Many(select = "com.kshrd.krorya.repository.IngredientRepository.findIngredientByRecipeId"))
    @Result(property = "cookingSteps", column = "recipe_id", many = @Many(select = "com.kshrd.krorya.repository.CookingStepRepository.selectCookingStepsByRecipeId"))
    @Result(property = "tagName", column = "recipe_id", many = @Many(select = "com.kshrd.krorya.repository.TagRepository.findTagNameByRecipeId"))
    @Result(property = "recipeImage", column = "image")
    @Result(property = "createdAt", column = "created_at")
    @Result(property = "updatedAt", column = "updated_at")
    List<Recipe> findRecipeByBookmarkedV1(UUID userId);


    @Select("""
   
            select count(*) from  grocery_recipe where grocery_id = #{groceryListId}
   """)
    int getTotalOfRecipesByGroceryListId(UUID groceryListId);




    @Select("""
    SELECT r.*
    FROM recipes r
    LEFT JOIN recipe_ingredient ri ON r.recipe_id = ri.recipe_id
    LEFT JOIN ingredients i ON ri.ingredient_id = i.ingredient_id
    LEFT JOIN cuisines c ON r.cuisine_id = c.cuisine_id
    WHERE (
        (r.cooking_time <= 15 AND r.cooking_time > 0 AND #{cookingTimeEnum} = 'UNDER_15_MINUTES') OR
        (r.cooking_time > 15 AND r.cooking_time <= 30 AND #{cookingTimeEnum} = 'UNDER_30_MINUTES') OR
        (r.cooking_time > 30 AND r.cooking_time <= 60 AND #{cookingTimeEnum} = 'UNDER_60_MINUTES') OR
        (c.name = #{cuisineEnumName} AND r.is_draft = FALSE AND r.is_public = TRUE) OR
        (r.cooking_level = #{cookingLevelEnum} AND r.is_draft = FALSE AND r.is_public = TRUE)
    )
    GROUP BY r.recipe_id
    HAVING COUNT(i.ingredient_id) <
        CASE
            WHEN #{ingredientsEnum} = 'LESS_THAN_5' THEN 5
            WHEN #{ingredientsEnum} = 'LESS_THAN_10' THEN 10
            WHEN #{ingredientsEnum} = 'LESS_THAN_15' THEN 15
        END
    """)
    @ResultMap("recipeMapping")
    List<Recipe> getRecipeByMultiTags(
            @Param("cookingLevelEnum") CookingLevelEnum cookingLevelEnum,
            @Param("cookingTimeEnum") CookingTimeEnum cookingTimeEnum,
            @Param("ingredientsEnum") IngredientsEnum ingredientsEnum,
            @Param("cuisineEnumName") String cuisineEnumName
    );







}




