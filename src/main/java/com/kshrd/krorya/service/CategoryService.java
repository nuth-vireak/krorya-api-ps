package com.kshrd.krorya.service;

import com.kshrd.krorya.model.dto.CategoryDTO;
import com.kshrd.krorya.model.entity.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
}
