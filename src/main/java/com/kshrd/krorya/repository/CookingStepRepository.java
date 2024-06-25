package com.kshrd.krorya.repository;


import com.kshrd.krorya.model.entity.CookingSteps;
import com.kshrd.krorya.model.request.CookingStepRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface CookingStepRepository {
    @Results(id = "cookingStepMapping", value = {
            @Result(property = "cookingStepId", column = "cooking_step_id"),
            @Result(property = "stepNumber", column = "step_number")
    })
    void test();

    @Select("SELECT cooking_step_id, step_number, image, description FROM cooking_steps WHERE recipe_id = #{recipeId}")
    @Results({
            @Result(property = "cookingStepId", column = "cooking_step_id"),
            @Result(property = "stepNumber", column = "step_number")
    })
    List<CookingSteps> selectCookingStepsByRecipeId(@Param("recipeId") UUID recipeId);


    @Select("""
      INSERT into cooking_steps (recipe_id, step_number, image, description) 
      values (#{recipeId},#{cookingStep.stepNumber},#{cookingStep.image}, #{cookingStep.description})   
    """)
    void saveCookingStepRequest(UUID recipeId, @Param("cookingStep") CookingStepRequest cookingStepRequest);


    @Delete("DELETE FROM cooking_steps WHERE recipe_id = #{id}")
    void deleteCookingStepByRecipeId(UUID id);
}
