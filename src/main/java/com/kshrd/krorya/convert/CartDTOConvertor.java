package com.kshrd.krorya.convert;

import com.kshrd.krorya.model.dto.CartDTO;
import com.kshrd.krorya.model.dto.FollowDTO;
import com.kshrd.krorya.model.dto.ReportDTO;
import com.kshrd.krorya.model.entity.Cart;
import com.kshrd.krorya.model.entity.Follow;
import com.kshrd.krorya.model.entity.Report;
import com.kshrd.krorya.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CartDTOConvertor {
    private final ModelMapper modelMapper;

    public CartDTO convertCartToCartDTO(Cart cart) {
        return modelMapper.map(cart, CartDTO.class);
    }

    public List<CartDTO> convertListCartToListCartDTO(List<Cart> carts){
        return carts.stream().map(this::convertCartToCartDTO).collect(Collectors.toList());
    }
}
