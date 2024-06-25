package com.kshrd.krorya.service;

import com.kshrd.krorya.model.dto.IngredientDTO;
import com.kshrd.krorya.model.request.IngredientRequest;

import java.util.List;
import java.util.UUID;

public interface IngredientService {
    List<IngredientDTO> getIngredients();
    IngredientDTO getIngredientByNameOrCreate(String name);

    IngredientDTO createNewIngredient(IngredientRequest ingredientRequest, UUID userId);
}
