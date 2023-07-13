package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.model.Category;
import com.github.mpacala00.forum.model.dto.category.CategoryDTO;
import com.github.mpacala00.forum.model.dto.category.CategoryPostsDTO;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getAllCategories();
    Category findById(Long id);
    CategoryPostsDTO findByIdMapToDTO(Long id);
    Category save(Category category);
}
