package com.kshrd.krorya.convert;

import com.kshrd.krorya.model.dto.CartDTO;
import com.kshrd.krorya.model.dto.CartFoodDTO;
import com.kshrd.krorya.model.entity.Cart;
import com.kshrd.krorya.model.entity.CartFood;
import com.kshrd.krorya.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CartFoodDTOConvertor {
    private final ModelMapper modelMapper;

    public CartFoodDTO convertCartFoodToCartFoodDTO(CartFood cartFood) {
        return modelMapper.map(cartFood, CartFoodDTO.class);
    }

    public List<CartFoodDTO> convertListCartFoodToListCartFoodDTO(List<CartFood> cartFoods){
        return cartFoods.stream().map(this::convertCartFoodToCartFoodDTO).collect(Collectors.toList());
    }
}
