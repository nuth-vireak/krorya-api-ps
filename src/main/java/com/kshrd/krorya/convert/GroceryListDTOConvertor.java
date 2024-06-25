package com.kshrd.krorya.convert;


import com.kshrd.krorya.model.dto.GroceryListDTO;
import com.kshrd.krorya.model.dto.RecipeDTO;
import com.kshrd.krorya.model.entity.GroceryList;
import com.kshrd.krorya.model.entity.Recipe;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class GroceryListDTOConvertor {
    private ModelMapper modelMapper;

    public GroceryListDTO convertGroceryListToGroceryListDTO(GroceryList groceryList) {
        return modelMapper.map(groceryList, GroceryListDTO.class);
    }

    public GroceryList convertGroceryListDTOGroceryList(GroceryListDTO groceryListDTO) {
        return modelMapper.map(groceryListDTO, GroceryList.class);
    }

    public List<GroceryListDTO> convertListOfGroceryListToGroceryListDTOList(List<GroceryList> listOfGroceryList) {
        return listOfGroceryList.stream()
                .map(groceryList -> modelMapper.map(groceryList, GroceryListDTO  .class))
                .collect(Collectors.toList());
    }

    public List<GroceryList> convertGroceryListDTOGroceryList(List<GroceryListDTO> listOfGroceryList) {
        return listOfGroceryList.stream()
                .map( groceryListDTO-> modelMapper.map(groceryListDTO, GroceryList.class))
                .collect(Collectors.toList());
    }

    public GroceryListDTO toDto(GroceryList groceryList) {
        return modelMapper.map(groceryList, GroceryListDTO.class);
    }

    public List<GroceryListDTO> toListDTO(List<GroceryList> groceryLists) {
        return groceryLists.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
