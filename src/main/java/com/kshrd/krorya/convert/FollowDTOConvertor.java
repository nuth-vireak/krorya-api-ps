package com.kshrd.krorya.convert;

import com.kshrd.krorya.model.dto.AppUserDTO;
import com.kshrd.krorya.model.dto.FollowDTO;
import com.kshrd.krorya.model.dto.FoodDTO;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.Follow;
import com.kshrd.krorya.model.entity.Food;
import com.kshrd.krorya.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class FollowDTOConvertor {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public FollowDTO convertFollowToFollowerDTO(Follow follow) {
        return modelMapper.map(follow, FollowDTO.class);
    }

    public FollowDTO toDto(Follow follow) {
        return modelMapper.map(follow, FollowDTO.class);
    }

    public List<FollowDTO> toListDTO(List<Follow> followList) {
        return followList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
