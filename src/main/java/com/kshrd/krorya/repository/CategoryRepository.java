package com.kshrd.krorya.repository;

import com.kshrd.krorya.model.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface CategoryRepository {

    @Select("""
    SELECT * FROM categories;
    """)
    @Results(id = "categoryMapping", value = {
            @Result(property = "categoryId", column = "category_id"),
            @Result(property = "categoryName", column = "name"),
            @Result(property = "categoryIcon", column = "icon")
    })
    List<Category> getAllCategories();

    @Select("""
    SELECT * FROM categories WHERE category_id = #{categoryId}
    """)
    @ResultMap("categoryMapping")
    Category getCategoryById(UUID categoryId);
}
