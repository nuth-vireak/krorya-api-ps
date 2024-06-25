package com.kshrd.krorya.repository.sqlProvider;

import java.util.List;
import java.util.Map;

public class RecipeSqlProvider {
    public String getRecipesByIngredients(Map<String, Object> parameters) {
        List<String> ingredients = (List<String>) parameters.get("ingredients");

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT r.* ")
                .append("FROM recipes r ")
                .append("INNER JOIN recipe_ingredient ri on r.recipe_id = ri.recipe_id ")
                .append("INNER JOIN ingredients i on i.ingredient_id = ri.ingredient_id ")
                .append("WHERE i.name IN (");

        StringBuilder ingredientsBuilder = new StringBuilder();
        for (int i = 0; i < ingredients.size(); i++) {
            String ingredientParam = "ingredient" + i;

            if (i > 0) {
                ingredientsBuilder.append(", ");
            }

            ingredientsBuilder.append("#{").append(ingredientParam).append("}");
            parameters.put(ingredientParam, ingredients.get(i));
        }

        sqlBuilder.append(ingredientsBuilder).append(")");

        return sqlBuilder.toString();
    }


}
