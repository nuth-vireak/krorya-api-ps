package com.kshrd.krorya.convert;

import com.kshrd.krorya.model.dto.AppUserDTO;
import com.kshrd.krorya.model.dto.RecipeDTO;
import com.kshrd.krorya.model.entity.AppUser;
import com.kshrd.krorya.model.entity.CustomUserDetail;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AppUserConvertor {
    private ModelMapper modelMapper;


    public AppUserDTO toDTO(AppUser appUser) {
        System.out.println(appUser + " in convertor");
        return modelMapper.map(appUser, AppUserDTO.class);
    }

    public AppUser toEntity(AppUserDTO appUserDTO) {
        return modelMapper.map(appUserDTO, AppUser.class);
    }

    public List<AppUserDTO> toListDTO(List<AppUser> userList) {
        return userList.stream().map(user -> modelMapper.map(user, AppUserDTO.class)).collect(Collectors.toList());
    }
    public List<AppUser> toListEntity(List<AppUserDTO> userDTOList){
        return userDTOList.stream().map(userDto -> modelMapper.map(userDto,AppUser.class)).collect(Collectors.toList());
    }

    public CustomUserDetail toCustomUserDetail(AppUser appUser){
        return modelMapper.map(appUser, CustomUserDetail.class);
    }

}
