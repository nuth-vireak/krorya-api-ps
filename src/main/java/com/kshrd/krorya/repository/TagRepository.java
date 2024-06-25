package com.kshrd.krorya.repository;

import com.kshrd.krorya.configuration.UUIDTypeHandler;
import com.kshrd.krorya.model.entity.Tag;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.UUID;

@Mapper
public interface TagRepository {
    @Results(id = "tags", value = {
            @Result(property = "tagId", column = "tag_id", jdbcType = JdbcType.OTHER, javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(property = "tagName", column = "name")
    })
    @Select("""
                SELECT * FROM tags
            """)
    List<Tag> getTags();


    @Select("""
            SELECT t.name FROM tags t 
            INNER JOIN recipe_tag rt ON t.tag_id = rt.tag_id 
            WHERE rt.recipe_id = #{recipeId}
            """)
    List<String> findTagNameByRecipeId(UUID recipeId);


    @Select("""
      select name from tags
      where tag_id = #{tagId}
   """)
//    @ResultMap("tags")
    String getTagById(@Param("tagId") UUID tagId);


    @Select("""
            SELECT tag_id FROM recipe_tag WHERE recipe_id = #{recipeId} AND tag_id = #{tagId}
            """)
    UUID getTagIdByRecipeIdAndTagId(UUID recipeId, UUID tagId);

    @Delete("""
            DELETE FROM recipe_tag WHERE recipe_id = #{recipeId}
            """)
    void deleteRecipeTagsByRecipeId(UUID recipeId);
}
