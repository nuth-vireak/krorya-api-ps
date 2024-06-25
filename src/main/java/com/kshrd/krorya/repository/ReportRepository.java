package com.kshrd.krorya.repository;

import com.kshrd.krorya.configuration.UUIDTypeHandler;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.Recipe;
import com.kshrd.krorya.model.entity.Report;
import com.kshrd.krorya.model.request.ReportOtherUserRequest;
import com.kshrd.krorya.model.request.ReportRecipeRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface ReportRepository {

    @Select("""
    INSERT INTO reports (recipe_id, reporter, user_id, description, type)
    VALUES (#{recipeReport.recipeId}, #{currentUserId}, NULL, #{recipeReport.description}, 'recipe')
    RETURNING *;
    """)
    @Results(id = "reportMapping", value = {
            @Result(property = "reportId", column = "report_id", javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(property = "recipe", column = "recipe_id", javaType = UUID.class, typeHandler = UUIDTypeHandler.class,
                    one = @One(select = "getReportedRecipeById")),
            @Result(property = "reporterInfo", column = "reporter",
                    one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.getSimpleAppUserById")),
            @Result(property = "reporteeInfo", column = "user_id",
                    one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.getSimpleAppUserById")),
            @Result(property = "reportDate", column = "created_at"),
            @Result(property = "reportType", column = "type")
    })
    Report createRecipeReport(@Param("recipeReport") ReportRecipeRequest reportRecipeRequest, @Param("currentUserId") UUID currentUserId);

    @Select("""
    SELECT * FROM recipes WHERE recipe_id = #{recipeId}
    """)
    @Result(property = "recipeId", column = "recipe_id")
    @Result(property = "recipeTitle", column = "title")
    @Result(property = "creatorInfo", column = "creator", one = @One(select = "com.kshrd.krorya.repository.AppUserRepository.getSimpleAppUserById"))
    @Result(property = "recipeImage", column = "image")
    Recipe getReportedRecipeById(UUID recipeId);


    @Select("""
    SELECT u.*
    FROM users u JOIN recipes r ON u.user_id = r.creator
    WHERE r.recipe_id = #{recipeId}
    """)
    @Result(property = "userId", column = "user_id")
    @Result(property = "profileImage", column = "profile_image")
    AppUser getRecipeOwnerId(UUID recipeId);

    @Select("""
    SELECT * FROM reports WHERE reporter = #{currentUserId} AND recipe_id = #{recipeId};
    """)
    @ResultMap("reportMapping")
    Report findReportByUserIdAndRecipeId(@Param("currentUserId") UUID currentUserId, @Param("recipeId") UUID recipeId);

    @Select("""
    SELECT * FROM reports WHERE report_id = #{recipeReportId} AND type = 'recipe';
    """)
    @ResultMap("reportMapping")
    Report getReportedRecipeByRecipeReportId(UUID recipeReportId);

    @Select("""
    SELECT * FROM reports WHERE type = 'recipe';
    """)
    @ResultMap("reportMapping")
    List<Report> getAllReportedRecipes();

    @Select("""
    DELETE FROM recipes WHERE recipe_id = #{recipeIdToDelete}
    --RETURNING *;
    """)
    @ResultMap("reportMapping")
    void deleteReportedRecipe(UUID recipeReportId, UUID recipeIdToDelete);

    @Select("""
    SELECT * FROM reports WHERE reporter = #{currentUserId} AND user_id = #{reporteeId};
    """)
    @ResultMap("reportMapping")
    Report findReportByCurrentUserIdAndReporteeId(@Param("currentUserId") UUID currentUserId, @Param("reporteeId") UUID reporteeId);

    @Select("""
    INSERT INTO reports (recipe_id, reporter, user_id, description, type)
    VALUES (NULL, #{currentUserId}, #{userReport.reporteeId}, #{userReport.description}, 'user')
    RETURNING *;
    """)
    @ResultMap("reportMapping")
    Report createOtherUserReport(@Param("userReport") ReportOtherUserRequest reportOtherUserRequest, @Param("currentUserId") UUID currentUserId);

    @Select("""
    SELECT * FROM reports WHERE type = 'user';
    """)
    @ResultMap("reportMapping")
    List<Report> getAllReportedUser();

    @Select("""
    SELECT * FROM reports WHERE report_id = #{userReportId} AND type = 'user';
    """)
    @ResultMap("reportMapping")
    Report getReportedUserByUserReportId(UUID userReportId);

    @Delete("""
    UPDATE users SET is_deactivated = 'true' WHERE user_id = #{userReportId};
    """)
    @ResultMap("reportMapping")
    void banReportedUser(UUID userReportId);
    @Delete("""
    DELETE FROM reports WHERE report_id = #{reportId};
    """)
    void deleteReportedUserReport(UUID reportId);

    //get profile owner
    @Select("""
    SELECT user_id FROM users
    WHERE user_id = #{reporteeId}
    """)
    @Result(property = "userId", column = "user_id")
    UUID getProfileOwnerId(UUID reporteeId);

    @Select("""
    SELECT COUNT(*) FROM foods;
    """)
    Integer countAllFoods();

    @Select("""
    SELECT COUNT(*) FROM recipes;
    """)
    Integer countAllRecipes();

    @Select("""
    SELECT COUNT(*) FROM users;
    """)
    Integer countAllUsers();

    @Select("""
    SELECT COUNT(*) FROM reports WHERE type = 'recipe';
    """)
    Integer countReportedRecipe();

    @Select("""
    SELECT COUNT(*) FROM reports WHERE type = 'user';
    """)
    Integer countReportedUser();

}




