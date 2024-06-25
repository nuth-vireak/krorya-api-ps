package com.kshrd.krorya.repository.sqlProvider;

import java.util.UUID;

public class GroceryListProvider {
    public String getGroceryListById(UUID id) {
        return "SELECT gl.grocery_id, gl.title, COUNT(gr.recipe_id) AS num_recipes, r.title as recipe_name, r.cooking_level, r.cooking_time " +
                "FROM grocery_lists gl " +
                "LEFT JOIN grocery_recipe gr ON gl.grocery_id = gr.grocery_id " +
                "LEFT JOIN recipes r ON gr.recipe_id = r.recipe_id " +
                "WHERE gl.grocery_id = #{id} " +
                "GROUP BY gl.grocery_id, gl.title, r.title, r.cooking_level, r.cooking_time";
    }
}
