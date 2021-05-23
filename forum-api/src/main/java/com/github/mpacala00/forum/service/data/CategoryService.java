package com.github.mpacala00.forum.service.data;

import com.github.mpacala00.forum.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();
    Category findById(Long id);
    Category save(Category category);
}
