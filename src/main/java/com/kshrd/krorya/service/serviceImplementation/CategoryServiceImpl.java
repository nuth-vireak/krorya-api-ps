package com.kshrd.krorya.service.serviceImplementation;

import com.kshrd.krorya.convert.CategoryDTOConvertor;
import com.kshrd.krorya.exception.CustomNotFoundException;
import com.kshrd.krorya.model.dto.CategoryDTO;
import com.kshrd.krorya.model.entity.Category;
import com.kshrd.krorya.repository.CategoryRepository;
import com.kshrd.krorya.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryDTOConvertor categoryDTOConvertor;
    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categoryList = categoryRepository.getAllCategories();
        if (categoryList.isEmpty()){
            throw new CustomNotFoundException("There is no category to show!");
        }
        List<CategoryDTO> categoryDTOList = categoryDTOConvertor.toListDTO(categoryList);
        return categoryDTOList;
    }
}
