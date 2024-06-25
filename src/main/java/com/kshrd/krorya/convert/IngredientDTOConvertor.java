package com.kshrd.krorya.convert;

import com.kshrd.krorya.model.dto.IngredientDTO;
import com.kshrd.krorya.model.entity.Ingredient;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class IngredientDTOConvertor {

    private final ModelMapper modelMapper;

    public IngredientDTO convertIngredientToIngredientDTO(Ingredient ingredient) {
        return modelMapper.map(ingredient, IngredientDTO.class);
    }

    public List<IngredientDTO> convertIngredientListToIngredientDTOList(List<Ingredient> ingredients) {
        return ingredients.stream()
                .map(ingredient -> modelMapper.map(ingredient, IngredientDTO.class))
                .collect(Collectors.toList());
    }
}
