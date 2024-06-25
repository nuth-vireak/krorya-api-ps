package com.kshrd.krorya.convert;

import com.kshrd.krorya.model.dto.CategoryDTO;
import com.kshrd.krorya.model.entity.Category;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CategoryDTOConvertor {
    private final ModelMapper modelMapper;

    public CategoryDTO toDTO(Category category){
        return modelMapper.map(category, CategoryDTO.class);
    }
    public Category toEntity(CategoryDTO categoryDTO){
        return modelMapper.map(categoryDTO, Category.class);
    }
    public List<CategoryDTO> toListDTO(List<Category> categories){
        return categories.stream().map(this::toDTO).collect(Collectors.toList());
    }
    public List<Category> toListEntity(List<CategoryDTO> categoryDTOs){
        return categoryDTOs.stream().map(this::toEntity).collect(Collectors.toList());
    }

}
