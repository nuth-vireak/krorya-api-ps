package com.kshrd.krorya.convert;

import com.kshrd.krorya.exception.SearchNotFoundException;
import com.kshrd.krorya.model.dto.FoodDTO;
import com.kshrd.krorya.model.dto.TagDTO;
import com.kshrd.krorya.model.entity.Food;
import com.kshrd.krorya.model.entity.Tag;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TagDTOConvertor {
    private ModelMapper modelMapper;

    public TagDTO toDto(Tag tag) {
        return modelMapper.map(tag, TagDTO.class);
    }

    public List<TagDTO> toListDTO(List<Tag> tags) {
        return tags.stream().map(this::toDto).collect(Collectors.toList());
    }
}
