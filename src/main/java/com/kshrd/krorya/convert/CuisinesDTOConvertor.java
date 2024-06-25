package com.kshrd.krorya.convert;

import com.kshrd.krorya.model.dto.CuisinesDTO;
import com.kshrd.krorya.model.dto.RecipeDTO;
import com.kshrd.krorya.model.entity.Cuisines;
import com.kshrd.krorya.model.entity.Recipe;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
@AllArgsConstructor
public class CuisinesDTOConvertor {
    private final ModelMapper modelMapper;

    public CuisinesDTO toDTO(Cuisines cuisines) {
        return modelMapper.map(cuisines, CuisinesDTO.class);
    }

    public Cuisines toEntity(CuisinesDTO cuisinesDTO) {
        return modelMapper.map(cuisinesDTO, Cuisines.class);
    }

    public List<CuisinesDTO> toDTO(List<Cuisines> cuisines) {
        return cuisines.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<Cuisines> toEntity(List<CuisinesDTO> cuisinesDTO) {
        return cuisinesDTO.stream().map(this::toEntity).collect(Collectors.toList());
    }

}
