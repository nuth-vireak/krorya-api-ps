package com.kshrd.krorya.convert;

import com.kshrd.krorya.model.dto.CartDTO;
import com.kshrd.krorya.model.dto.CartWithFoodDTO;
import com.kshrd.krorya.model.entity.CartWithFood;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CartWithFoodConvertor {
    private final ModelMapper modelMapper;

    public CartWithFoodDTO convertCartWithFoodToCartWithFoodDTO(CartWithFood cartWithFood) {
        return modelMapper.map(cartWithFood, CartWithFoodDTO.class);
    }
    public List<CartWithFoodDTO> convertListCartWithFoodToListCartWithFoodDTO(List<CartWithFood> cartWithFoods){
        return cartWithFoods.stream().map(this::convertCartWithFoodToCartWithFoodDTO).collect(Collectors.toList());
    }
}
