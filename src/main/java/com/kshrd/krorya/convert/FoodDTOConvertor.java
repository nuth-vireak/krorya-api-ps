package com.kshrd.krorya.convert;

import com.kshrd.krorya.exception.SearchNotFoundException;
import com.kshrd.krorya.model.dto.FoodDTO;
import com.kshrd.krorya.model.entity.Food;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Component
@AllArgsConstructor
public class FoodDTOConvertor {
    private final ModelMapper modelMapper;

    public FoodDTO toDto(Food food) {
//        if (food == null) {
//            throw new SearchNotFoundException("Food with id does not exist");
//        }
        return modelMapper.map(food, FoodDTO.class);
    }

    public Food toEntity(FoodDTO foodDTO) {
        if (foodDTO == null) {
            throw new SearchNotFoundException("resource is null");
        }
        return modelMapper.map(foodDTO, Food.class);
    }

    public List<FoodDTO> toListDTO(List<Food> foods) {
        return foods.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Food> toListEntity(List<FoodDTO> foodDTOS) {
        return foodDTOS.stream().map(this::toEntity).collect(Collectors.toList());
    }

}
